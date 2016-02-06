package com.groundzero.alkemy.activity.seen;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.FileObserver;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;

import com.google.android.glass.touchpad.Gesture;
import com.google.android.glass.touchpad.GestureDetector;


import com.groundzero.alkemy.AlkemyApplication;
import com.groundzero.alkemy.R;
import com.groundzero.alkemy.activity.AActivity;
import com.groundzero.alkemy.database.ADbContract;
import com.groundzero.alkemy.database.AQuery;
import com.groundzero.alkemy.database.parse.ParseConstants;
import com.groundzero.alkemy.objects.AObject;
import com.groundzero.alkemy.utils.ALocation;
import com.groundzero.alkemy.utils.AConnection;
import com.groundzero.alkemy.utils.AConstants;
import com.groundzero.alkemy.utils.AHandler;
import com.groundzero.alkemy.utils.LToast;
import com.groundzero.alkemy.utils.ParseUtils;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;

import com.google.android.glass.content.Intents;

import com.groundzero.alkemy.utils.CameraUtils;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class SeenCameraActivity extends AActivity {
    private static final String TAG = "SeenCameraActivity";
    Context mActivityContext;
    Intent mOriginIntent;
    Bundle mOriginIntentExtras;
    String mOriginIntentAction, mCapturedBy = "user";
    private boolean mIsForScannerActivityResult;

    private GestureDetector mGestureDetector;
    String thumbnailPath;
    private boolean notUploaded = true;
    ALocation mALocation;

    // BaseListener  = Receives detection results,
    private final GestureDetector.BaseListener mBaseListener = new GestureDetector.BaseListener() {
        @Override
        public boolean onGesture(Gesture gesture) {
            switch (gesture) {
                case SWIPE_LEFT:
                    takePhoto();
                    return true;
                case TAP:
                    LToast.showL(mActivityContext,getLocationString());
                    return true;
                case SWIPE_RIGHT:
                    requestLocationUpdates();
                    return true;
                default:
                    return false;
            }

        }
    };
    private AHandler mUiHandler;
    private String mImageFilePath;


    // OnCreate is where you initialize your activity, set contentview
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        mActivityContext = this;

        mUiHandler = new AHandler() {
            @Override
            public void handleTheMessage(Message msg) {
                switch (msg.what) {
                    case Messages.Seen.FINISH_ACTIVITY:
                        finish();
                }
            }
        };

        mGestureDetector = new GestureDetector(this).setBaseListener(mBaseListener);
        setContentView(R.layout.activity_seen_camera);
        mALocation = new ALocation(mActivityContext);


        mOriginIntent = getIntent();
        if (mOriginIntent != null) {
            mOriginIntentAction = mOriginIntent.getAction();
            //Log.v(TAG, "mOriginIntentAction = " + mOriginIntentAction);
            mOriginIntentExtras = mOriginIntent.getExtras();
            if (mOriginIntentExtras != null) {
                if (mOriginIntentAction.equals(AConstants.Seen.ACTION_AIS_RUN_SEEN)) {
                    String runType = mOriginIntentExtras.getString(AConstants.Seen.KEY_RUN_TYPE);
                    if (runType.equals(AConstants.Seen.VALUE_RUN_TYPE_ALKEMY_EVENT_SERVICE)) {
                        mCapturedBy = AConstants.Seen.VALUE_CAPTURED_BY_SYSTEM;
                    } else if (runType.equals(AConstants.Seen.VALUE_RUN_TYPE_MYO_SERVICE)) {
                        mCapturedBy = AConstants.Seen.VALUE_CAPTURED_BY_MYO;
                    }
                } else if (mOriginIntentAction.equals(AConstants.Scanner.ACTION_SCANNER_RUN_SEEN_FOR_IMAGE_URL)) {
                    mIsForScannerActivityResult = true;
                    //Log.v(TAG, "mIsForScannerActivityResult = " + mIsForScannerActivityResult);
                }
            }
        }

        takePhoto();
    }



    public void saveSeenData(String seenId, String location, String capturedAt, String capturedBy) {
        AQuery query = new AQuery(mActivityContext, ADbContract.SeenEntry.TABLE_NAME);
        query.addCV(ADbContract.SeenEntry.COLUMN_NAME_SEEN_ID, seenId);
        query.addCV(ADbContract.SeenEntry.COLUMN_NAME_LOCATION, location);
        query.addCV(ADbContract.SeenEntry.COLUMN_NAME_CAPTURED_BY, capturedBy);
        query.addCV(ADbContract.SeenEntry.COLUMN_NAME_CAPTURED_AT, capturedAt);
        long insertionId = query.insert();
    }

    //Called when a generic motion is not handled by any of thew views, override the activity's ongeneric motion events to the gesture detector's onMotionEvent
    public boolean onGenericMotionEvent(MotionEvent event) {
        if (mGestureDetector != null){
            return mGestureDetector.onMotionEvent(event);
        }
        return false;
    }

    public String getLocationString(){
        AObject location = mALocation.getLastKnownLocation();
        return location.get(ALocation.LATITUDE) + " : " + location.get(ALocation.LONGITUDE);
    }
    public ParseGeoPoint getParseGeoPoint(){
        return ParseUtils.createParseGeoPoint(mALocation.getLastKnownLocation());
    }

    private void requestLocationUpdates() {
        mALocation.requestUpdates(mActivityContext, mUiHandler);
        Toast.makeText(this,"done updates", Toast.LENGTH_SHORT).show();
    }

    //
    private void takePhoto() {
        //create an intent to launch the phone's camera to take a picture
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        Log.d("TakePhoto()", "method ");
        // Used to launch an activity for which you would like a result when it finished.
        startActivityForResult(intent, CameraUtils.TAKE_PHOTO_REQUEST);

    }

    // called when activity is finished/exits
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CameraUtils.TAKE_PHOTO_REQUEST && resultCode == RESULT_OK) {
            //thumbnailPath = data.getStringExtra(Intents.EXTRA_THUMBNAIL_FILE_PATH);

            // getting the picture's path
            String picturePath = data.getStringExtra(Intents.EXTRA_PICTURE_FILE_PATH);
//            Log.d("onActivityResult ()", picturePath);
            String thumbnailPath = data.getStringExtra(Intents.EXTRA_THUMBNAIL_FILE_PATH);
            Log.v(TAG, "THUMB PATH: " + thumbnailPath);
            processPicture(picturePath, thumbnailPath);

        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    private void processPicture(final String picturePath, String thumbnailPath) {
        final File pictureFile = new File(picturePath);
        if (notUploaded) {
            if (pictureFile.exists()) {
                mImageFilePath = picturePath;
                //Log.d("processPicture", "the file exists! -- processing image");
                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                Bitmap bitmap = BitmapFactory.decodeFile(pictureFile.getAbsolutePath(), bmOptions);

                // Convert it to byte
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                // Compress image to lower quality scale 1 - 100
                bitmap.compress(Bitmap.CompressFormat.JPEG, 85, stream);
                byte[] image = stream.toByteArray();

                ParseFile pFile = new ParseFile(ParseConstants.VALUE_IMAGE_URL_SUFFIX, image);
                pFile.saveInBackground();

                // Create a New Class called "ImageUpload" in Parse
                String name;
                DateFormat dateFormat = new SimpleDateFormat(AConstants.Date.FORMAT);
                Date date = new Date();
                final String capturedAt = dateFormat.format(date);
                name = "imgUpload : " + capturedAt;

                final ParseObject imgUpload = new ParseObject(ParseConstants.CLASS_IMAGE_UPLOAD);
                // Create a column named "ImageName" and set the string
                imgUpload.put(ParseConstants.KEY_IMAGE_NAME, name);

                // Create a column named "ImageFile" and insert the image
                imgUpload.put(ParseConstants.KEY_IMAGE_FILE, pFile);
                final ParseGeoPoint parseGeoPoint = getParseGeoPoint();
                String location = getLocationString();
                imgUpload.put(ParseConstants.KEY_IMAGE_LOCATION_GEO_POINT, parseGeoPoint);
                imgUpload.put(ParseConstants.KEY_GLASS_PRIMARY_EMAIL, AlkemyApplication.getGlassPrimaryEmail(this));
                // Create the class and the columns
                uploadPicture(capturedAt, imgUpload, location, thumbnailPath);

                notUploaded = false;
                //Log.d("processPicture", "Is now uploaded" + notUploaded);

            } else {
                createPictureFile(picturePath, pictureFile);
            }
        }

    }

    private void createPictureFile(final String picturePath, File pictureFile) {
        //Log.d("processPicture", "the file does not exist! attempting to create");
        final File parentDirectory = pictureFile.getParentFile();
        FileObserver observer = new FileObserver(parentDirectory.getPath(), FileObserver.CLOSE_WRITE | FileObserver.MOVED_TO) {
            private boolean isFileWritten = false;

            @Override
            public void onEvent(int event, String path) {
                if (!isFileWritten) {
                    stopWatching();
                    // Now that the file is ready, recursively call
                    // processPicture again (on the UI thread).
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            processPicture(picturePath, thumbnailPath);
                        }
                    });
                }
            }
        };
        observer.startWatching();
    }

    private void uploadPicture(final String capturedAt, final ParseObject imgUpload, final String location, final String thumbnailPath) {
        if (!mIsForScannerActivityResult) {
            mUiHandler.obtainMessage(AHandler.Messages.Seen.FINISH_ACTIVITY).sendToTarget();
            //save in background so Seen can close out
            imgUpload.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    doAfterSave(imgUpload, location, capturedAt, thumbnailPath);
                }
            });
        } else {
            //save sequentially, so Seen doesn't close prematurely
            try {
                if (AConnection.isNetworkAvailable(mActivityContext)) {
                    imgUpload.save();
                    doAfterSave(imgUpload, location, capturedAt, thumbnailPath);
                    mUiHandler.obtainMessage(AHandler.Messages.Seen.FINISH_ACTIVITY).sendToTarget();
                } else {
                    Log.v(TAG, "No internet connection...");
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }


    private void doAfterSave(ParseObject imgUpload, String location, String capturedAt, String localImageThumbnailPath) {
        ParseFile parseFile = (ParseFile) imgUpload.get(ParseConstants.KEY_IMAGE_FILE);
        String imageUrl = parseFile.getUrl();

        String insertionId = imgUpload.getObjectId();
        //Log.d("InsertionId",insertionId + " tg ");
        saveSeenData(insertionId, location, capturedAt, mCapturedBy);
        if (mIsForScannerActivityResult) {
            returnResultToOriginIntent(imageUrl, insertionId, localImageThumbnailPath);
        }
    }

    private void returnResultToOriginIntent(String imageUrl, String imageId, String localImageThumbnailPath) {
        Intent data = mOriginIntent;
        if (!imageUrl.isEmpty()) {
            data.putExtra(CameraUtils.INTENT_EXTRA_KEY_IMAGE_URL, imageUrl);
            data.putExtra(CameraUtils.INTENT_EXTRA_KEY_IMAGE_ID, imageId);
            data.putExtra(CameraUtils.INTENT_EXTRA_KEY_IMAGE_FILE_PATH, mImageFilePath);
            data.putExtra(CameraUtils.INTENT_EXTRA_KEY_IMAGE_THUMB_PATH, localImageThumbnailPath);
            setResult(Activity.RESULT_OK, data);
        } else {
            setResult(Activity.RESULT_CANCELED, data);
        }
        finish();
    }

}
