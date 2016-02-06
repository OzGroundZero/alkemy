package com.groundzero.alkemy.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.groundzero.alkemy.database.ADbContract;
import com.groundzero.alkemy.database.AQuery;
import com.groundzero.alkemy.objects.Config;
import com.groundzero.alkemy.services.seen.SeenRecallService;
import com.groundzero.alkemy.utils.AConstants;
import com.groundzero.alkemy.utils.ATime;

import java.util.Date;

public class ScreenOnReceiver extends BroadcastReceiver {
    private static final String TAG = "ScreenOnReceiver";

    public ScreenOnReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        //Log.v(TAG, "SCREEN ON");

        AQuery aQuery = new AQuery(context, ADbContract.ConfigEntry.TABLE_NAME);
        Config seenLastRecalledAt = (Config) aQuery.getConfigByKey(AConstants.Date.SHARED_PREFS_SEEN_RECALLED_AT);

        String seenLastRecalledAtValue = seenLastRecalledAt.getValue();
        long seenRecalledAt = -1;

        if(seenLastRecalledAtValue != null) {
            Log.d(TAG,"seenLastRecalledAtValue = " + seenLastRecalledAtValue);
            seenRecalledAt = Long.parseLong(
                    seenLastRecalledAtValue);

            long seenLastRecalledAgo = 0;
            seenLastRecalledAgo = ATime.timeAgo(ATime.ETime.AGO_MINUTE, ATime.formatDate(seenRecalledAt, ATime.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS));


            Log.d(TAG,"Seen last recalled at : " + seenRecalledAt);
            if(seenLastRecalledAgo >= 10){
                Log.d(TAG, "inside if statement");
                updateSeenRecalledAt(context);
                runSeenRecallService(context);
            } else {
                Log.d(TAG, "Seen last recall : " + seenLastRecalledAgo + "min ago ");
            }
        } else if (seenRecalledAt == -1){
            Log.d(TAG, "first time");
            insertNewSeenRecalledAt(context);
            runSeenRecallService(context);
        }
    }

    private void runSeenRecallService(Context context) {
        Log.d(TAG, "inside runSeenRecallService");
        Intent i = new Intent(context,SeenRecallService.class);
        context.startService(i);
    }

    private void insertNewSeenRecalledAt(Context context) {
        long currentTime = ATime.getCurrentTime();
        AQuery aQuery = new AQuery(context, ADbContract.ConfigEntry.TABLE_NAME);
        aQuery.addCV(ADbContract.ConfigEntry.COLUMN_NAME_KEY, AConstants.Date.SHARED_PREFS_SEEN_RECALLED_AT);
        aQuery.addCV(ADbContract.ConfigEntry.COLUMN_NAME_VALUE, currentTime + "");
        aQuery.insert();
    }

    private void updateSeenRecalledAt(Context context) {
        long currentTime = ATime.getCurrentTime();
        AQuery aQuery = new AQuery(context, ADbContract.ConfigEntry.TABLE_NAME);
        aQuery.addCV(ADbContract.ConfigEntry.COLUMN_NAME_VALUE, currentTime + "");
        aQuery.updateConfigByKey(AConstants.Date.SHARED_PREFS_SEEN_RECALLED_AT);
    }

    public static long getCurrentTime() {
        return new Date().getTime();
    }
}
