package com.groundzero.alkemy.utils;

import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.groundzero.alkemy.objects.AObject;
import com.groundzero.alkemy.utils.AHandler;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by al3x901 on 9/20/2015.
 */
public class ALocation {

    private static final String TAG = "SeenLocation";
    public static final String LONGITUDE = "longitude";
    public static final String LATITUDE = "latitude";
    private Context mContext;
    boolean canGetLocation = true;

    Location location;

    double latitude = -1001;
    double longitude = -1001;
    /**
     * The maximum age of a location retrieved from the passive location provider before it is
     * considered too old to use when the compass first starts up.
     */
    private static final long MAX_LOCATION_AGE_MILLIS = TimeUnit.MINUTES.toMillis(5);
    /**
     * The minimum distance desired between location notifications.
     */
    private static final long METERS_BETWEEN_LOCATIONS = 1;

    /**
     * The minimum elapsed time desired between location notifications.
     */

    private static final long MILLIS_BETWEEN_LOCATIONS = 3000;

    private LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location loc) {
            makeUseOfNewLocation(loc);
        }

        @Override
        public void onProviderDisabled(String provider) {
            // Don't need to do anything here.
        }

        @Override
        public void onProviderEnabled(String provider) {
            // Don't need to do anything here.
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            // Don't need to do anything here.
        }
    };

    protected volatile LocationManager mLocationManager;
    Criteria mCriteria;

    public ALocation(Context context){
        mContext = context;
        initLocationManager(context);
    }

    private void initLocationManager(Context context) {
        mLocationManager = (LocationManager) context.getSystemService(context.LOCATION_SERVICE);
    }

    public ALocation(){}

    public void setCriteria(){
        mCriteria = new Criteria();
        mCriteria.setAccuracy(Criteria.ACCURACY_FINE);
        mCriteria.setBearingRequired(false);
        mCriteria.setSpeedRequired(false);
        Log.d(TAG+"setcriteria", "done");
    }

    public void requestUpdates(final Context context ,AHandler uiHandler){
        if(mCriteria == null){ Log.d(TAG+"criteria","set");
            setCriteria();}
        List<String> providers = mLocationManager.getProviders(mCriteria, true);
        for (String provider : providers) {
            Log.d(TAG+":const", provider + " Started");
            uiHandler.post(new Runnable() {
                public void run() {
                    Toast.makeText(context,"updates started",Toast.LENGTH_LONG).show();
                }
            });
            mLocationManager.requestLocationUpdates(provider,
                    MILLIS_BETWEEN_LOCATIONS, METERS_BETWEEN_LOCATIONS, mLocationListener,
                    Looper.getMainLooper());
        }
    }


    private void makeUseOfNewLocation(Location location) {
        Log.d(TAG, "#makeUseOfNewLocation() location changed");
        if(location != null){
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            String locationString = latitude + " : " + longitude;
            Toast.makeText(mContext,locationString,Toast.LENGTH_LONG);
            Log.d(TAG, "#makeUseOfNewLocation "+locationString);
        }
    }

    public AObject getLastKnownLocation(){
        if(mLocationManager != null) {
            Location lastLocation = mLocationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
            Log.d(TAG + "", " #getLastKnownLocation() lastlocation created");
            if (lastLocation != null) {
                long locationAge = lastLocation.getTime() - System.currentTimeMillis();
                if (locationAge < MAX_LOCATION_AGE_MILLIS) {
                    Log.d(TAG, "getLastKnownLocation" + " lastlocation !old");
                    location = lastLocation;
                    latitude = lastLocation.getLatitude();
                    longitude = lastLocation.getLongitude();
                }
            }
        }

        AObject aLocation = new AObject();
        aLocation.set(ALocation.LATITUDE, latitude);
        aLocation.set(ALocation.LONGITUDE, longitude);
        return aLocation;
    }



    public void stopUsingGPS() {
        if(mLocationManager != null) {
            Log.d(TAG,"#StopUsingGPS hit");
            mLocationManager.removeUpdates(mLocationListener);
        }
    }

    public double getLatitude() {
        if(location != null) {
            latitude = location.getLatitude();
        }
        return latitude;
    }

    public double getLongitude() {
        if(location != null) {
            longitude = location.getLongitude();
        }

        return longitude;
    }

    public double getTime() {
        double time = location.getTime();
        return time;
    }

    public boolean canGetLocation() {
        return this.canGetLocation;
    }


    public void showToast(String msg){
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
    }
}