package com.groundzero.alkemy.utils;

/**
 * Created by lwdthe1 on 10/16/2015.
 */
public class AConstants {
    public static class Myo {
        public static final String BROADCAST_ACTION_HANDLE_POSE = "com.groundzero.alkemy.myo.action.HANDLE_POSE";
    }
    public static class Seen {
        public static final String ACTION_AIS_RUN_SEEN = "com.groundzero.seen.action.ACTION_AIS_RUN_SEEN";
        public static final String ACTION_MYO_RUN_SEEN = "com.groundzero.seen.action.ACTION_MYO_RUN_SEEN";
        public static final String KEY_RUN_TYPE = "runType";

        public static final String VALUE_RUN_TYPE_ALKEMY_EVENT_SERVICE = "runType_alkemyEventService";
        public static final String VALUE_RUN_TYPE_MYO_SERVICE = "runType_myoService";

        public static final String VALUE_CAPTURED_BY_SYSTEM = "system";
        public static final String VALUE_CAPTURED_BY_MYO = "user_myo";
    }

    public class Date {
        public static final String FORMAT = "yyyy-MM-dd HH:mm:ss";
        public static final String SHARED_PREFS_SEEN_RECALLED_AT = "seenRecalledAt";
    }

    public class Scanner {
        public static final String ACTION_SCANNER_RUN_SEEN_FOR_IMAGE_URL = "com.groundzero.scanner.action.RUN_SEEN_FOR_IMAGE_URL";
    }

    public class Intent {
        public static final String EXTRA_KEY_DUMMY = "extraKeyDummy";
        public static final String EXTRA_VALUE_DUMMY = "extraValueDummy";
    }

    public class Config {
        public class Keys {
            public static final String SEEN_RECALL_ALARM_CREATED = "seenRecallAlarmCreated";
        }
    }
}
