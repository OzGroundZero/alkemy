package com.groundzero.alkemy.objects;

import android.content.Context;
import android.database.Cursor;

import com.groundzero.alkemy.database.ADbContract;

/**
 * Created by lwdthe1 on 9/24/2015.
 */
public class Config extends ADbObject {

    private String value;
    private String key;

    public Config(Context context, Cursor cursor){
        super(context, cursor);
        if(cursor.getCount() <1 ) this.id = CODE_BAD_OBJECT;
        else {
            int id = cursor.getInt(cursor.getColumnIndex(ADbContract.ScannedObjectLinkEntry._ID));
            String value = cursor.getString(cursor.getColumnIndex(ADbContract.ConfigEntry.COLUMN_NAME_VALUE));
            String key = cursor.getString(cursor.getColumnIndex(ADbContract.ConfigEntry.COLUMN_NAME_KEY));
            String createdAt = cursor.getString(cursor.getColumnIndex(ADbContract.ScannedObjectLinkEntry.COLUMN_NAME_CREATED_AT));
            String updatedAt = cursor.getString(cursor.getColumnIndex(ADbContract.ScannedObjectLinkEntry.COLUMN_NAME_UPDATED_AT));

            this.id = id;
            this.key = key;
            this.value = value;
            this.createdAt = createdAt;
            this.updatedAt = updatedAt;
        }
    }

    public String getValue() {
        return value;
    }
}
