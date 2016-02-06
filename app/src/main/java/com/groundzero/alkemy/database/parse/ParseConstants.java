package com.groundzero.alkemy.database.parse;

/**
 * Created by keithmartin on 3/12/15.
 */
public class ParseConstants {
    // Class (database table) name
    public static final String CLASS_COMMENTS = "Comments";

    public static final String KEY_IMAGE_NAME = "imageName";
    public static final String KEY_IMAGE_FILE = "imageFile";

    // Field names
    public static final String KEY_ID = "objectId";
    public static final String KEY_USER_ID = "userId";
    public static final String KEY_EMAIL = "email";
    //used solely for the Users class (table) in Parse.com queries. unlike the others, it is not camelcase
    public static final String KEY_USERNAME = "username";

    public static final String KEY_CREATED_AT = "createdAt";
    public static final String KEY_UPDATED_AT = "updatedAt";

    //miscellaneous values
    public static final String TYPE_IMAGE = "image";
    public static final String TYPE_VIDEO = "video";
    public static final String TYPE_TEXT = "text";


    public static final String KEY_FILE_PATH = "filePath";
    public static final String KEY_FILE_BYTES = "fileBytes";
    public static final String KEY_IMAGE_LOCATION_GEO_POINT = "imageLocationGeoPoint";
    public static final String CLASS_IMAGE_UPLOAD = "ImageUpload";
    public static final String KEY_GLASS_PRIMARY_EMAIL = "glassPrimaryEmail";
    public static final String KEY_IMAGE_URL = "imageUrl";
    public static final String VALUE_IMAGE_URL_SUFFIX = "alkemySeen.png";
}

