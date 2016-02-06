package com.groundzero.alkemy.services.seen;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.groundzero.alkemy.AlkemyApplication;
import com.groundzero.alkemy.database.parse.ParseConstants;
import com.groundzero.alkemy.objects.AObject;
import com.groundzero.alkemy.utils.ALocation;
import com.groundzero.alkemy.utils.ParseUtils;
import com.parse.CountCallback;
import com.parse.FindCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class SeenRecallService extends Service {
    private static final String TAG = "SeenRecallService";
    private Context mContext;
    ALocation mALocation;


    public SeenRecallService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "oncreate()");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mContext = this;
        mALocation = new ALocation(mContext);


        String currentUserEmail = AlkemyApplication.getGlassPrimaryEmail(mContext);
        ParseQuery<ParseObject> query = ParseQuery.getQuery(ParseConstants.CLASS_IMAGE_UPLOAD);
        query.whereEqualTo(ParseConstants.KEY_GLASS_PRIMARY_EMAIL, currentUserEmail);
        query.whereNear(ParseConstants.KEY_IMAGE_LOCATION_GEO_POINT,
                ParseUtils.createParseGeoPoint(mALocation.getLastKnownLocation()));
        final int[] sizeOfQuery = new int[1];

        query.countInBackground(new CountCallback() {
            @Override
            public void done(int i, com.parse.ParseException e) {
                if (e == null) {
                    sizeOfQuery[0] = i;
                } else {
                    sizeOfQuery[0] = 0;
                }
            }
        });

        Log.d(TAG, "before findinbackground");
//       query.findInBackground(new FindCallback<ParseObject>() {
//           @Override
//           public void done(List<ParseObject> list, com.parse.ParseException e) {
//
//           }
//       });
        Log.d(TAG, "after findinbackground");

        ALocation location = new ALocation(mContext);
        if(location != null) {
            AObject lastKnownLocation = location.getLastKnownLocation();

            String currentLocation = lastKnownLocation.get(ALocation.LATITUDE) + " : " + lastKnownLocation.get(ALocation.LONGITUDE);

            ParseUser currentUser= ParseUser.getCurrentUser();

            Log.d(TAG, "current location andres: " + currentLocation);

            Log.d(TAG, "Number of pictures" + sizeOfQuery[0]);
        }

        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
