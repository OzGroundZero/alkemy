package com.groundzero.alkemy.services;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

import com.groundzero.alkemy.receivers.ScreenOnReceiver;

public class ScreenStateReceiverActivatorService extends Service {
    private static final String TAG = "ScreenStateReceiverActivatorService";

    public ScreenStateReceiverActivatorService() {
    }

    public static ScreenOnReceiver screenStateReceiver;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG.substring(0,23), "onCreate()");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        registerScreeStateReceiver();
        return START_NOT_STICKY;
    }

    private void registerScreeStateReceiver(){
        if(screenStateReceiver == null) {
            screenStateReceiver = new ScreenOnReceiver();

            IntentFilter screenStateFilter = new IntentFilter();
            screenStateFilter.addAction(Intent.ACTION_SCREEN_ON);
            //screenStateFilter.addAction(Intent.ACTION_SCREEN_OFF);
            registerReceiver(screenStateReceiver, screenStateFilter);
        }
    }

    private void unregisterScreenStateReceiver() {
        try {
            if(screenStateReceiver != null) unregisterReceiver(screenStateReceiver);
        } catch (IllegalStateException ise) {
            // Do Nothing. Receiver has already been unregistered.
        }
    }

    @Override
    public void onDestroy() {
        unregisterScreenStateReceiver();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
