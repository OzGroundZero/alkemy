package com.groundzero.alkemy.database;

import android.provider.BaseColumns;

/**
 * Created by lwdthe1 on 9/20/2015.
 */
public class AEntry implements BaseColumns {
    public static String DEFAULT_SORT = _ID + " DESC";
    public static final String COLUMN_NAME_CREATED_AT = "createdAt";//time of insertion
    public static final String COLUMN_NAME_UPDATED_AT = "updatedAt";//time of insertion
}
