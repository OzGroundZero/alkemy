package com.groundzero.alkemy.activity.scanner;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.groundzero.alkemy.R;
import com.groundzero.alkemy.activity.AActivity;
import com.groundzero.alkemy.activity.seen.SeenCameraActivity;
import com.groundzero.alkemy.cloudsight.CSApi;
import com.groundzero.alkemy.cloudsight.CSGetResult;
import com.groundzero.alkemy.cloudsight.CSPostConfig;
import com.groundzero.alkemy.cloudsight.CSPostResult;
import com.groundzero.alkemy.config.Config;
import com.groundzero.alkemy.database.parse.ParseConstants;
import com.groundzero.alkemy.utils.AConnection;
import com.groundzero.alkemy.utils.AConstants;
import com.groundzero.alkemy.utils.AHandler;
import com.groundzero.alkemy.utils.CameraUtils;
import com.groundzero.alkemy.utils.LToast;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.parse.ParseObject;

import java.io.File;
import java.io.IOException;

public class ScannerActivity extends AActivity {

    static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    static final JsonFactory JSON_FACTORY = new JacksonFactory();
    private static final String TAG = "ScannerActivity";
    private AHandler mUiHandler;
    private CSPostResult mPostResult;
    private CSApi mCSApi;

    protected Uri mMediaUriForDisplay;
    protected ImageLoader mImageLoader = ImageLoader.getInstance();
    private ImageView mObjectThumbImageView;
    private LinearLayout mContainerObjectInfo;
    private TextView mObjectNameTextView;
    private TextView mObjectDescription;
    private TextView mObjectWebResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);

        mUiHandler = new AHandler() {
            @Override
            public void handleTheMessage(Message msg) {
                switch(msg.what){
                    case Messages.Scanner.FINISH_SCANNING_OBJECT:
                        try {
                            mPostResult = (CSPostResult) msg.obj;
                            getObjectInfo(mUiHandler, mCSApi, mPostResult);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    case Messages.Scanner.GOT_IMAGE:
                        CSGetResult scoredResult = (CSGetResult) msg.obj;
                        try {
                            showObjectInfo(scoredResult);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                }
            }
        };

        //Log.v(TAG, "IN SCANNER ACTIVITY");

        mObjectNameTextView = (TextView) findViewById(R.id.object_name);
        mObjectDescription = (TextView) findViewById(R.id.object_description);
        mObjectThumbImageView = (ImageView) findViewById(R.id.object_thumb);
        new Thread(new Runnable() {
            @Override
            public void run() {
                mImageLoader.init(ImageLoaderConfiguration.createDefault(mActivityContext));
            }
        }).start();
        if(AConnection.isNetworkAvailable(mActivityContext)) {
            Intent intent = new Intent(this, SeenCameraActivity.class);
            intent.setAction(AConstants.Scanner.ACTION_SCANNER_RUN_SEEN_FOR_IMAGE_URL);
            intent.putExtra(AConstants.Intent.EXTRA_KEY_DUMMY, AConstants.Intent.EXTRA_VALUE_DUMMY);
            LToast.showS(mActivityContext, "Scan Object");
            startActivityForResult(intent, CameraUtils.TAKE_PHOTO_FOR_SCANNER_ACTIVITY_RESULT);
        } else {
            LToast.showL(mActivityContext, "No internet connection...");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Log.v(TAG, "RETURNED FROM SEEEN ACTIVITY");
        //Log.v(TAG, "RESULT CODE = " + resultCode);
        //Log.v(TAG, "REQUEST CODE = " + requestCode);
        if (requestCode == CameraUtils.TAKE_PHOTO_FOR_SCANNER_ACTIVITY_RESULT && resultCode == RESULT_OK) {
            String csAPIimageUrl = data.getStringExtra(CameraUtils.INTENT_EXTRA_KEY_IMAGE_URL);
            String imageLocalFilePath = data.getStringExtra(CameraUtils.INTENT_EXTRA_KEY_IMAGE_FILE_PATH);
            String imageLocalThumbnailPath = data.getStringExtra(CameraUtils.INTENT_EXTRA_KEY_IMAGE_THUMB_PATH);
            if(imageLocalThumbnailPath != null){
                //use the thumbpath
                Log.v(TAG, "using thumbpath");
                mMediaUriForDisplay = Uri.fromFile(new File(imageLocalThumbnailPath));;
            } else if(imageLocalFilePath != null){
                //use the local file path
                Log.v(TAG, "using file path");
                mMediaUriForDisplay = Uri.fromFile(new File(imageLocalFilePath));;
            }  else {
                //use the uri from online
                Log.v(TAG, "using online thumbpath");
                mMediaUriForDisplay = Uri.parse(csAPIimageUrl);
            }

            String imageId = data.getStringExtra(CameraUtils.INTENT_EXTRA_KEY_IMAGE_ID);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    mUiHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mImageLoader.displayImage(String.valueOf(mMediaUriForDisplay), mObjectThumbImageView);
                        }
                    });
                }
            }).start();
            scanObject(mUiHandler, csAPIimageUrl, imageId);
        }
    }

    /**
     * Scan the object the user is looking at by the provided imageUrl
     * then attempt to delete the picture from our database
     * @param mUiHandler
     * @param imageUrl
     * @param imageId
     */
    public void scanObject(AHandler mUiHandler, String imageUrl, String imageId) {
        mCSApi = new com.groundzero.alkemy.cloudsight.CSApi(
                HTTP_TRANSPORT,
                JSON_FACTORY,
                Config.Scanner.API_KEY
        );
        Log.v(TAG, "SCANNING OBJECT");
        com.groundzero.alkemy.cloudsight.CSPostConfig imageToPost = CSPostConfig.newBuilder()
                .withRemoteImageUrl(imageUrl)
                .build();

        mCSApi.postImage(mUiHandler, imageToPost);
        //deleteSeenPicFromOnlineDatabase(imageId);
    }

    private void getObjectInfo(AHandler mUiHandler, CSApi api, CSPostResult postResult) throws InterruptedException, IOException {
        toggleLoading(true);
        api.getImageInfo(mUiHandler, postResult);
    }

    private void toggleLoading(boolean b) {
        //show loader or not
    }

    private void showObjectInfo(CSGetResult result) throws IOException {
        toggleLoading(false);
        ((TextView)findViewById(R.id.object_name_label)).setText("Name");
        //Log.v(TAG, String.valueOf(result));

        //use Android-Universal-Image-Loader to display the image
        String resultPrettyString = result.toPrettyString();
        Log.v(TAG, resultPrettyString);
        mObjectNameTextView.setText(result.getName());
    }

    /**
     * Method deletes picture from database
     * after being used for Object Recognition
     * with the given imageId because this is not a Seen picture
     * @param imageId
     */
    private void deleteSeenPicFromOnlineDatabase(String imageId) {
        ParseObject.createWithoutData(ParseConstants.CLASS_IMAGE_UPLOAD, imageId).deleteEventually();
    }
}
