package com.groundzero.alkemy.database;

/**
 * Created by lwdthe1 on 9/20/2015.
 */
public class ADbContract {
    public static final String STORAGE_BOOLEAN_TRUE = "1";
    public static final String STORAGE_BOOLEAN_FALSE = "0";

    private static final String STORAGE_CLASS_TEXT = "TEXT";
    private static final String STORAGE_CLASS_INT = "INTEGER";
    private static final String STORAGE_CLASS_NULL = "NULL";
    private static final String STORAGE_CLASS_REAL = "REAL";
    private static final String STORAGE_CLASS_BLOB = "BLOB";

    protected static final String COLUMN_AFFINITY_TEXT = "TEXT"; // prefers to store text, null, or blob storage classes
    protected static final String COLUMN_AFFINITY_NUM = "NUMERIC"; // prefers to store data of all 5 storage classes
    private static final String COLUMN_AFFINITY_INT = "INTEGER";// prefers to store data of all 5 storage classes
    private static final String COLUMN_AFFINITY_NONE = "NONE"; //  has no preference in storage class of data it stores
    private static final String COLUMN_AFFINITY_REAL = "REAL";// prefers to store data of all 5 storage classes
    protected static final String COLUMN_AFFINITY_DATETIME = "DATETIME";// prefers to store data of all 5 storage classes

    private static final String COLUMN_DEF_PRIMARY = "PRIMARY KEY";
    private static final String COLUMN_CONSTRAINT_AUTO_INCREMENT = "AUTOINCREMENT";
    private static final String COLUMN_CONSTRAINT_UNIQUE = "UNIQUE";
    protected static final String COLUMN_CONSTRAINT_DEFAULT = "DEFAULT";
    protected static final String COLUMN_CONSTRAINT_NOT_NULL = "NOT NULL";

    protected static final String COMMA_SEP = ",";
    protected static final String SPACE = " ";
    private static final String CONTINUE = COMMA_SEP + SPACE;
    public static final String VALUE_CURRENT_LOCAL_TIMESTAMP = "(DATETIME('now', 'localtime'))";
    private static final String CREATE_TABLE = "CREATE TABLE";

    //To prevent someone from accidentally instantiating the contract class,
    //give it an empty constructor
    public ADbContract() {}

    public static String createConfigTable() {
        return ConfigEntry.getCreateTable();
    }
    public static String createSeenTable() {
        return SeenEntry.getCreateTable();
    }
    public static String createScannedObjectTable() {
        return ScannedObjectEntry.getCreateTable();
    }
    public static String createScannedObjectLinkTable() {
        return ScannedObjectLinkEntry.getCreateTable();
    }

    public static String deleteSeenTable() {
        return SeenEntry.getDeleteTable();
    }
    public static String deleteConfigTable() {
        return ConfigEntry.getDeleteTable();
    }

    public static abstract class SeenEntry extends AEntry {
        public static final String TABLE_NAME = "SeenEntry";
        //the id of the Seen picture in the online database
        public static final String COLUMN_NAME_SEEN_ID = "seenId";
        //wgps location of the picture from Seen app
        public static final String COLUMN_NAME_LOCATION = "location";
        //who captured the picture: the user or the system
        public static final String COLUMN_NAME_CAPTURED_BY = "capturedBy";
        //the date and time the picture was captured in Seen. must not be confused with CREATED_AT
        public static final String COLUMN_NAME_CAPTURED_AT = "captureAt";

        private static final String SQL_CREATE_SEEN =
                CREATE_TABLE + SPACE + TABLE_NAME + "("
                + _ID + SPACE + COLUMN_AFFINITY_INT + SPACE
                + COLUMN_DEF_PRIMARY + SPACE + COLUMN_CONSTRAINT_AUTO_INCREMENT
                + CONTINUE

                + COLUMN_NAME_SEEN_ID + SPACE + COLUMN_AFFINITY_INT
                + SPACE + COLUMN_CONSTRAINT_NOT_NULL
                + CONTINUE

                + COLUMN_NAME_LOCATION + SPACE + COLUMN_AFFINITY_TEXT
                + SPACE + COLUMN_CONSTRAINT_NOT_NULL
                + CONTINUE

                + COLUMN_NAME_CAPTURED_BY + SPACE + COLUMN_AFFINITY_TEXT
                + SPACE + COLUMN_CONSTRAINT_NOT_NULL
                + CONTINUE

                + COLUMN_NAME_CAPTURED_AT + SPACE + COLUMN_AFFINITY_DATETIME
                + SPACE + COLUMN_CONSTRAINT_NOT_NULL
                + CONTINUE

                + COLUMN_NAME_CREATED_AT + SPACE + COLUMN_AFFINITY_DATETIME
                + SPACE + COLUMN_CONSTRAINT_DEFAULT + SPACE + VALUE_CURRENT_LOCAL_TIMESTAMP
                + CONTINUE

                + COLUMN_NAME_UPDATED_AT + SPACE + COLUMN_AFFINITY_DATETIME
                + SPACE + COLUMN_CONSTRAINT_DEFAULT + SPACE + VALUE_CURRENT_LOCAL_TIMESTAMP
                + ")";

        private static final String SQL_DELETE_TABLE_SEEN =
                "DROP TABLE IF EXISTS" + SPACE + TABLE_NAME;

        private static String getCreateTable() {
            return SQL_CREATE_SEEN;
        }
        private static String getDeleteTable() {
            return SQL_DELETE_TABLE_SEEN;
        }
    }


    public static abstract class ScannedObjectEntry extends AEntry {

        public static final String TABLE_NAME = "ScannedObjectEntry";

        public static final String COLUMN_NAME_LOCATION = "location";

        public static final String COLUMN_NAME_DESCRIPTION = "description";

        public static final String COLUMN_NAME_CATEGORY = "category";

        public static final String COLUMN_NAME_TITLE = "title";

        private static final String SQL_CREATE_SCANNEDOBJECT =

                CREATE_TABLE + SPACE + TABLE_NAME + "("
                + _ID + SPACE + COLUMN_AFFINITY_INT + SPACE
                + COLUMN_DEF_PRIMARY + SPACE + COLUMN_CONSTRAINT_AUTO_INCREMENT
                + CONTINUE

                + COLUMN_NAME_LOCATION + SPACE + COLUMN_AFFINITY_TEXT
                + SPACE + COLUMN_CONSTRAINT_NOT_NULL
                + CONTINUE

                + COLUMN_NAME_DESCRIPTION + SPACE + COLUMN_AFFINITY_TEXT
                + SPACE + COLUMN_CONSTRAINT_NOT_NULL
                + CONTINUE

                + COLUMN_NAME_CATEGORY + SPACE + COLUMN_AFFINITY_TEXT
                + SPACE + COLUMN_CONSTRAINT_NOT_NULL
                + CONTINUE

                + COLUMN_NAME_TITLE+ SPACE + COLUMN_AFFINITY_TEXT
                + SPACE + COLUMN_CONSTRAINT_NOT_NULL
                + CONTINUE

                + COLUMN_NAME_CREATED_AT + SPACE + COLUMN_AFFINITY_DATETIME
                + SPACE + COLUMN_CONSTRAINT_DEFAULT + SPACE + VALUE_CURRENT_LOCAL_TIMESTAMP
                + CONTINUE

                + COLUMN_NAME_UPDATED_AT + SPACE + COLUMN_AFFINITY_DATETIME
                + SPACE + COLUMN_CONSTRAINT_DEFAULT + SPACE + VALUE_CURRENT_LOCAL_TIMESTAMP
                + ")";

        private static final String SQL_DELETE_TABLE_SCANNEDOBJECT =
                "DROP TABLE IF EXISTS" + SPACE + TABLE_NAME;

        private static String getCreateTable() {
            return SQL_CREATE_SCANNEDOBJECT;
        }
        private static String getDeleteTable() {
            return SQL_DELETE_TABLE_SCANNEDOBJECT;
        }
    }


    public static abstract class ScannedObjectLinkEntry extends AEntry {

        public static final String TABLE_NAME = "ScannedObjectLinkEntry";

        public static final String COLUMN_NAME_SCANNEDOBJECT_ID = "scannedObjectId";

        public static final String COLUMN_NAME_LINK = "link";

        private static final String SQL_CREATE_SCANNEDOBJECTLINK =

                CREATE_TABLE + SPACE + TABLE_NAME + "("
                        + _ID + SPACE + COLUMN_AFFINITY_INT + SPACE
                        + COLUMN_DEF_PRIMARY + SPACE + COLUMN_CONSTRAINT_AUTO_INCREMENT
                        + CONTINUE

                        + COLUMN_NAME_SCANNEDOBJECT_ID + SPACE + COLUMN_AFFINITY_INT
                        + SPACE + COLUMN_CONSTRAINT_NOT_NULL
                        + CONTINUE

                        + COLUMN_NAME_LINK + SPACE + COLUMN_AFFINITY_TEXT
                        + SPACE + COLUMN_CONSTRAINT_NOT_NULL
                        + CONTINUE

                        + COLUMN_NAME_CREATED_AT + SPACE + COLUMN_AFFINITY_DATETIME
                        + SPACE + COLUMN_CONSTRAINT_DEFAULT + SPACE + VALUE_CURRENT_LOCAL_TIMESTAMP
                        + CONTINUE

                        + COLUMN_NAME_UPDATED_AT + SPACE + COLUMN_AFFINITY_DATETIME
                        + SPACE + COLUMN_CONSTRAINT_DEFAULT + SPACE + VALUE_CURRENT_LOCAL_TIMESTAMP
                        + ")";

        private static final String SQL_DELETE_TABLE_SCANNEDOBJECTLINK =
                "DROP TABLE IF EXISTS" + SPACE + TABLE_NAME;

        private static String getCreateTable() {
            return SQL_CREATE_SCANNEDOBJECTLINK;
        }
        private String getDeleteTable() {
            return SQL_DELETE_TABLE_SCANNEDOBJECTLINK;
        }
    }

    /**
     *  Inner class that defines the Config table contents
     *  Used for system configuration values
     **/
    public static abstract class ConfigEntry extends AEntry {
        public static final String TABLE_NAME = "Config";
        public static final String COLUMN_NAME_KEY= "key";//NOT NULL
        public static final String COLUMN_NAME_VALUE = "value";//NOT NULL

        //define a projection tat specifies which columns from the database
        //you will actually use after queries
        private static final String[] projection = {
                _ID,
                COLUMN_NAME_KEY,
                COLUMN_NAME_VALUE,
                COLUMN_NAME_CREATED_AT,
                COLUMN_NAME_UPDATED_AT
        };

        public static String[] getQueryProjectionAll(){
            return projection;
        }

        public static String getQueryPrioritySortOrder() {
            return COLUMN_NAME_KEY + " ASC";
        }

        private static final String SQL_CREATE_CONFIGS =
                "CREATE TABLE " + TABLE_NAME + " ("
                        + _ID + SPACE + COLUMN_AFFINITY_INT + SPACE
                        + COLUMN_DEF_PRIMARY + SPACE + COLUMN_CONSTRAINT_AUTO_INCREMENT
                        + CONTINUE
                        + COLUMN_NAME_KEY + SPACE + COLUMN_AFFINITY_TEXT
                        + SPACE + COLUMN_CONSTRAINT_NOT_NULL
                        + CONTINUE
                        + COLUMN_NAME_VALUE + SPACE + COLUMN_AFFINITY_TEXT
                        + SPACE + COLUMN_CONSTRAINT_NOT_NULL
                        + CONTINUE
                        + COLUMN_NAME_CREATED_AT + SPACE + COLUMN_AFFINITY_DATETIME
                        + SPACE + COLUMN_CONSTRAINT_DEFAULT + SPACE + VALUE_CURRENT_LOCAL_TIMESTAMP
                        + CONTINUE
                        + COLUMN_NAME_UPDATED_AT + SPACE + COLUMN_AFFINITY_DATETIME
                        + SPACE + COLUMN_CONSTRAINT_DEFAULT + SPACE + VALUE_CURRENT_LOCAL_TIMESTAMP
                        + " )";

        private static final String SQL_DELETE_TABLE_COFIG =
                "DROP TABLE IF EXISTS" + SPACE + TABLE_NAME;

        public static String getCreateTable() {
            return SQL_CREATE_CONFIGS;
        }
        public static String getDeleteTable() {
            return SQL_DELETE_TABLE_COFIG;
        }
    }


}
