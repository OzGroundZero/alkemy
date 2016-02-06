package com.groundzero.alkemy.config;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.SystemClock;
import android.util.Log;

import com.groundzero.alkemy.receivers.OnBootCompleteReceiver;
import com.groundzero.alkemy.services.ScreenStateReceiverActivatorService;
import com.groundzero.alkemy.services.event.EventService;
import com.groundzero.alkemy.services.event.seen.AISeenService;
import com.groundzero.alkemy.utils.ATime;

/**
 * Created by lwdthe1 on 10/10/2015.
 */
public class Config {
    private static String TAG = "Config.class";

    public static void setupAfterBootComplete(Context context) {
        scheduleReceiversAndAlarms(context);
    }

    public static void scheduleReceiversAndAlarms(Context context) {
        // Schedule it now so this user's greetings will be sent automatically
        enableReceivers(context);
        scheduleAlarms(context);
    }

    private static void enableReceivers(Context context) {
        enableBootReceiver(context);
    }

    /////////////////RECEIVERS/////////////////////RECEIVERS////////////////////////////
    public static void enableBootReceiver(Context context) {
        ComponentName receiver = new ComponentName(context, OnBootCompleteReceiver.class);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }

    ///////////ALARMS/////////////////////ALARMS///////////////////////////////ALARMS//////////
    public static void scheduleAlarms(Context context) {
        cancelEventServiceAlarms(context);
        scheduleEventServiceAlarms(context);
        scheduleScreenStateReceiverActivatorAlarm(context);
    }
    /**
     * Schedule a repeating alarm on the interval
     * to start the MessengerAlarmService.class service
     * which will run calculations to send greetings that are due.
     * @param context
     */
    public static PendingIntent scheduleEventServiceAlarms(Context context) {
        String TAG = "OnBootCompleteReceiver#scheduleEventServiceAlarms()";
        AlarmManager alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent alarmPendingIntent = getEventServiceAlarmPendingIntent(context);

        //Wake up the device to fire the alarm in 3 minutes, and every 15 minutes after that:
        alarmMgr.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                AlarmManager.INTERVAL_HOUR,
                AlarmManager.INTERVAL_HOUR, alarmPendingIntent);
        //alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, SystemClock.elapsedRealtime()
        // + LTime.MILLISEC_MINUTE, LTime.MILLISEC_MINUTE*3, alarmPendingIntent);
        return alarmPendingIntent;
    }

    /**
     * Schedule a repeating alarm on the interval
     * to start the MessengerAlarmService.class service
     * which will run calculations to send greetings that are due.
     * @param context
     */
    public static PendingIntent scheduleAISeenServiceAlarms(Context context) {
        String TAG = "OnBootCompleteReceiver#scheduleAISeenServiceAlarms()";
        AlarmManager alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent alarmPendingIntent = getAISeenServiceAlarmPendingIntent(context);

        //Wake up the device to fire the alarm in 30 minutes, and every 30 minutes after that:
        alarmMgr.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                AlarmManager.INTERVAL_HALF_HOUR,
                AlarmManager.INTERVAL_HALF_HOUR, alarmPendingIntent);
        //alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, SystemClock.elapsedRealtime()
        // + LTime.MILLISEC_MINUTE, LTime.MILLISEC_MINUTE*3, alarmPendingIntent);
        return alarmPendingIntent;
    }

    public static PendingIntent scheduleScreenStateReceiverActivatorAlarm(Context context){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent alarmPendingIntent = getScreenStateReceiverActivatorAlarmPendingIntent(context);
        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                //set the alarm to trigger for the first time after a day
                SystemClock.elapsedRealtime() + 1000*60*1*24,
                1000 * 60 * 1, alarmPendingIntent);
        return alarmPendingIntent;
    }

    public static void cancelEventServiceAlarms(Context context) {
        Log.v(TAG, "Canceling EventService alarms");
        AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent alarmPendingIntent = getEventServiceAlarmPendingIntent(context);

        //Wake up the device to fire the alarm in 15 minutes
        alarmMgr.cancel(alarmPendingIntent);
    }

    public static void cancelAISeenServiceAlarms(Context context) {
        Log.v(TAG, "Canceling AISeenService alarms");
        AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent alarmPendingIntent = getAISeenServiceAlarmPendingIntent(context);

        //Wake up the device to fire the alarm in 15 minutes
        alarmMgr.cancel(alarmPendingIntent);
    }

    private static PendingIntent getEventServiceAlarmPendingIntent(Context context) {
        Intent intent = new Intent(context, EventService.class);
        return PendingIntent.getService(context, Alarms.ALARM_ID_EVENT_SERVICE,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private static PendingIntent getAISeenServiceAlarmPendingIntent(Context context) {
        Intent intent = new Intent(context, AISeenService.class);
        return PendingIntent.getService(context, Alarms.ALARM_ID_AISEEN_SERVICE,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private static PendingIntent getScreenStateReceiverActivatorAlarmPendingIntent(Context context){
        Intent intent = new Intent(context, ScreenStateReceiverActivatorService.class);
        return PendingIntent.getService(context, Alarms.ALARM_ID_SCREEN_STATE_RECEIVER_ACTIVATOR_SERVICE,
        intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public class Alarms {
        /**
         * final Default values for
         * the time span in which people are likely asleep,
         * thus, greetings should not be sent
         */
        public static final int ALARM_ID_EVENT_SERVICE = 1420;
        public static final int ALARM_ID_SCREEN_STATE_RECEIVER_ACTIVATOR_SERVICE = 2200;
        public static final int ALARM_ID_AISEEN_SERVICE = 1421;
    }

    public static class Scanner {
        public static final String API_KEY = "RSqxYRJHYj5Ik2T37il2hg";
    }
}
