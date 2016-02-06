package com.groundzero.alkemy.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.groundzero.alkemy.config.Config;

public class OnBootCompleteReceiver extends BroadcastReceiver {
    public OnBootCompleteReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        Config.setupAfterBootComplete(context);
    }
}
