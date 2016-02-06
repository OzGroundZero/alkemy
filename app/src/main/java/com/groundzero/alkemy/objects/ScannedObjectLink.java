package com.groundzero.alkemy.objects;

import android.content.Context;
import android.database.Cursor;

import com.groundzero.alkemy.database.ADbContract;

/**
 * Created by lwdthe1 on 9/24/2015.
 */
public class ScannedObjectLink extends ADbObject {
    
    private int scannedObjectId;
    private String link;

    public ScannedObjectLink(Context context, Cursor cursor){
        super(context, cursor);
        if(cursor.getCount() <1 ) this.id = CODE_BAD_OBJECT;
        else {
            int id = cursor.getInt(cursor.getColumnIndex(ADbContract.ScannedObjectLinkEntry._ID));
            int scannedObjectId = cursor.getInt(cursor.getColumnIndex(ADbContract.ScannedObjectLinkEntry.COLUMN_NAME_SCANNEDOBJECT_ID));
            String link = cursor.getString(cursor.getColumnIndex(ADbContract.ScannedObjectLinkEntry.COLUMN_NAME_LINK));
            String createdAt = cursor.getString(cursor.getColumnIndex(ADbContract.ScannedObjectLinkEntry.COLUMN_NAME_CREATED_AT));
            String updatedAt = cursor.getString(cursor.getColumnIndex(ADbContract.ScannedObjectLinkEntry.COLUMN_NAME_UPDATED_AT));

            this.id = id;
            this.link = link;
            this.scannedObjectId = scannedObjectId;
            this.createdAt = createdAt;
            this.updatedAt = updatedAt;
        }
    }
    
}
