package com.groundzero.alkemy;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.groundzero.alkemy.config.Config;
import com.groundzero.alkemy.database.ADbContract;
import com.groundzero.alkemy.database.AQuery;
import com.groundzero.alkemy.database.parse.ParseConstants;
import com.groundzero.alkemy.utils.AConstants;
import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.ParseUser;

/**
 * Created by lwdthe1 on 10/16/2015.
 */
public class AlkemyApplication extends Application {

    private static final String TAG = "AlkemyApplication";
    private Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "UxkZAm1ZGbbMETbbGiUiJEpOnxpzN9agWwZHBlh1", "1ziE1a6qKlD4vEF7q8U75ijJxKVpUtcqQv5ZF4x9");

        runOneTimeAlarmsSetup();
    }

    private void runOneTimeAlarmsSetup() {
        createSeenRecallAlarm();
    }

    private void createSeenRecallAlarm() {
        AQuery seenRecallAlarmQuery = new AQuery(mContext, ADbContract.ConfigEntry.TABLE_NAME);
        com.groundzero.alkemy.objects.Config seenLastRecalledAt =
                (com.groundzero.alkemy.objects.Config)
                        seenRecallAlarmQuery.getConfigByKey(AConstants.Config.Keys.SEEN_RECALL_ALARM_CREATED);

        String seenRecallAlarmCreated = seenLastRecalledAt.getValue();
        //Log.v(TAG, "seenRecallAlarmCreated = " + seenRecallAlarmCreated);
        if(seenRecallAlarmCreated == null) {
            Config.scheduleScreenStateReceiverActivatorAlarm(mContext);
            AQuery aQuery = new AQuery(mContext, ADbContract.ConfigEntry.TABLE_NAME);
            aQuery.addCV(ADbContract.ConfigEntry.COLUMN_NAME_KEY, AConstants.Config.Keys.SEEN_RECALL_ALARM_CREATED);
            aQuery.addCV(ADbContract.ConfigEntry.COLUMN_NAME_VALUE,  ADbContract.STORAGE_BOOLEAN_TRUE);
            aQuery.insert();
        } else {
            //do nothing. all is well
        }
    }

    public static void updateParseInstallation(ParseUser user){
        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
        installation.put(ParseConstants.KEY_USER_ID, user.getObjectId());
        installation.saveInBackground();
    }

    public static String getGlassPrimaryEmail(Context context) {
        Account[] glassAccount = AccountManager.get(context).getAccountsByType("com.google");
        String glassPrimaryEmail = "";
        if(glassAccount.length > 0) {
            glassPrimaryEmail = glassAccount[0].name;
        }
        return glassPrimaryEmail;
    }
}
