package com.groundzero.alkemy.objects;

import android.content.Context;
import android.database.Cursor;

import com.groundzero.alkemy.database.ADbContract;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lwdthe1 on 9/24/2015.
 */
public class ScannedObject extends ADbObject{

    private String location;
    private String description;
    private String category;
    private String title;
    private List<String> links;

    public ScannedObject(Context context, Cursor cursor){
        super(context, cursor);
        if(cursor.getCount() <1 ) this.id = CODE_BAD_OBJECT;
        else {
            int id = cursor.getInt(cursor.getColumnIndex(ADbContract.ScannedObjectEntry._ID));
            String description = cursor.getString(cursor.getColumnIndex(ADbContract.ScannedObjectEntry.COLUMN_NAME_DESCRIPTION));
            String location = cursor.getString(cursor.getColumnIndex(ADbContract.ScannedObjectEntry.COLUMN_NAME_LOCATION));
            String category = cursor.getString(cursor.getColumnIndex(ADbContract.ScannedObjectEntry.COLUMN_NAME_CATEGORY));
            String title = cursor.getString(cursor.getColumnIndex(ADbContract.ScannedObjectEntry.COLUMN_NAME_TITLE));
            String createdAt = cursor.getString(cursor.getColumnIndex(ADbContract.ScannedObjectEntry.COLUMN_NAME_CREATED_AT));
            String updatedAt = cursor.getString(cursor.getColumnIndex(ADbContract.ScannedObjectEntry.COLUMN_NAME_UPDATED_AT));

            this.id = id;
            this.description = description;
            this.location = location;
            this.category = category;
            this.title = title;
            this.createdAt = createdAt;
            this.updatedAt = updatedAt;
        }
    }


}
