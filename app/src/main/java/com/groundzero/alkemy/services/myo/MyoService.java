/*
 * Copyright (C) 2014 Thalmic Labs Inc.
 * Distributed under the Myo SDK license agreement. See LICENSE.txt for details.
 */
package com.groundzero.alkemy.services.myo;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.groundzero.alkemy.R;
import com.groundzero.alkemy.activity.scanner.ScannerActivity;
import com.groundzero.alkemy.activity.seen.SeenCameraActivity;
import com.thalmic.myo.AbstractDeviceListener;
import com.thalmic.myo.DeviceListener;
import com.thalmic.myo.Hub;
import com.thalmic.myo.Myo;
import com.thalmic.myo.Pose;

public class MyoService extends Service {
    private static final String TAG = "MyoService";
    private Toast mToast;
    private MyoService mContext;
    // Classes that inherit from AbstractDeviceListener can be used to receive events from Myo devices.
    // If you do not override an event, the default behavior is to do nothing.
    private DeviceListener mListener = new AbstractDeviceListener() {
        @Override
        public void onConnect(Myo myo, long timestamp) {
            showToast(getString(R.string.myo_connected));
            Log.v(TAG, "Myo Service onConnected");
        }
        @Override
        public void onAttach(Myo myo, long timestamp){
            showToast(getString(R.string.myo_attached));
            Log.v(TAG, "Myo Service onAttach");
        }
        @Override
        public void onDisconnect(Myo myo, long timestamp) {
            showToast(getString(R.string.myo_disconnected));
            Log.v(TAG, "Myo Service onDisconnected");
        }
        // onPose() is called whenever the Myo detects that the person wearing it has changed their pose, for example,
        // making a fist, or not making a fist anymore.
        @Override
        public void onPose(Myo myo, long timestamp, Pose pose) {
            // Show the name of the pose in a toast.
            showToast(getString(R.string.myo_pose, pose.toString()));
            switch (pose) {
                case REST:
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        //e.printStackTrace();
                    }
                    showToast("Done Resting");
                    break;
                case FINGERS_SPREAD:
                    showToast("Running Seen");
                    //create an intent to launch the phone's camera to take a picture
                    Intent seenIntent = new Intent(mContext, SeenCameraActivity.class);
                    seenIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    seenIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(seenIntent);
                    tempLock(myo,3);
                    break;
                case WAVE_IN:
                    showToast("Running Scanner");
                    //create an intent to launch the phone's camera to take a picture
                    Intent scannerIntent = new Intent(mContext, ScannerActivity.class);
                    scannerIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    scannerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(scannerIntent);
                    tempLock(myo, 3);
                    break;
                default:
                    showToast(getString(R.string.myo_pose, pose.toString()));
            }
            //Log.v(TAG, "Myo Service onPose");
        }
    };

    private void tempLock(Myo myo, int seconds) {
        seconds = seconds < 1? 3 : seconds;
        myo.lock();
        try {
            Thread.sleep(seconds*1000);
        } catch (InterruptedException e) {
            //e.printStackTrace();
        }
        myo.unlock(Myo.UnlockType.HOLD);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        // First, we initialize the Hub singleton with an application identifier.
        Hub hub = Hub.getInstance();
        Log.v(TAG, "Starting Myo Service");
        if (!hub.init(this, getPackageName())) {
            String text = "Couldn't initialize Hub";
            Log.v(TAG, text);
            showToast(text);
            stopSelf();
            return;
        }
        //Log.v(TAG, "After Hub init");
        // Disable standard Myo locking policy. All poses will be delivered.
        hub.setLockingPolicy(Hub.LockingPolicy.NONE);
        //Log.v(TAG, "After set Locking Policy");
        // Next, register for DeviceListener callbacks.
        hub.addListener(mListener);
        //Log.v(TAG, "After add Listener");
        // Finally, scan for Myo devices and connect to the first one found that is very near.
        hub.attachToAdjacentMyo();
        //Log.v(TAG, "After Attach to adjacent Myo");
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        // We don't want any callbacks when the Service is gone, so unregister the listener.
        Hub.getInstance().removeListener(mListener);
        Hub.getInstance().shutdown();
        Log.v(TAG, "Myo Service Destroyed");
    }
    private void showToast(String text) {
        Log.w(TAG, text);
        if (mToast == null) mToast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
        else mToast.setText(text);
        mToast.show();
    }

    private void processPose(String text) {

    }
}
