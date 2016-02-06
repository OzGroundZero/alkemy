package com.groundzero.alkemy.services.event.seen;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.groundzero.alkemy.activity.seen.SeenCameraActivity;
import com.groundzero.alkemy.utils.AConstants;

/**
 * Created by lwdthe1 on 10/16/2015.
 */
public class AISeenService extends IntentService {
    private static final String TAG = "AISeenService";
    private Intent mOriginIntent;
    private Bundle mOriginIntentExtras;
    private Context mContext;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public AISeenService(String name) {
        super(name);
    }

    /**
     * This method is invoked on the worker thread with a request to process.
     * Only one Intent is processed at a time, but the processing happens on a
     * worker thread that runs independently from other application logic.
     * So, if this code takes a long time, it will hold up other requests to
     * the same IntentService, but it will not hold up anything else.
     * When all requests have been handled, the IntentService stops itself,
     * so you should not call {@link #stopSelf}.
     *
     * @param intent The value passed to {@link
     *               Context#startService(Intent)}.
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        Log.v(TAG, "IN AISEEN SERVICE");
        mContext = this;

        Intent runCameraActivityIntent = new Intent(mContext, SeenCameraActivity.class);
        runCameraActivityIntent.setAction(AConstants.Seen.ACTION_AIS_RUN_SEEN);
        runCameraActivityIntent.putExtra(AConstants.Seen.KEY_RUN_TYPE, AConstants.Seen.VALUE_RUN_TYPE_ALKEMY_EVENT_SERVICE);
        runCameraActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(runCameraActivityIntent);
    }
}
