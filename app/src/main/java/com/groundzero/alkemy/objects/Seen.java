package com.groundzero.alkemy.objects;

import android.content.Context;
import android.database.Cursor;

import com.groundzero.alkemy.database.ADbContract;

/**
 * Created by Keith and Lincoln on 9/20/15
 * This is a final class and has no set methods
 */
public class Seen extends ADbObject{
    public static String VALUE_CAPTURED_BY_SYSTEM = "system";
    public static String VALUE_CAPTURED_BY_USER = "user";

    private int seenId; //the id of the Seen picture in the online database
    private String capturedAt;// the date and time the picture was captured in Seen app
    private String location;//wgps location of the picture from Seen app
    private String capturedBy;//who captured the picture: the user or the system


    public Seen(Context context, Cursor cursor){
        super(context, cursor);
        if(cursor.getCount() <1 ) this.id = CODE_BAD_OBJECT;
        else {
            int id = cursor.getInt(cursor.getColumnIndex(ADbContract.SeenEntry._ID));
            int seenId = cursor.getInt(cursor.getColumnIndex(ADbContract.SeenEntry.COLUMN_NAME_SEEN_ID));
            String capturedAt = cursor.getString(cursor.getColumnIndex(ADbContract.SeenEntry.COLUMN_NAME_CAPTURED_AT));
            String location = cursor.getString(cursor.getColumnIndex(ADbContract.SeenEntry.COLUMN_NAME_LOCATION));
            String capturedBy = cursor.getString(cursor.getColumnIndex(ADbContract.SeenEntry.COLUMN_NAME_CAPTURED_BY));
            String createdAt = cursor.getString(cursor.getColumnIndex(ADbContract.SeenEntry.COLUMN_NAME_CREATED_AT));
            String updatedAt = cursor.getString(cursor.getColumnIndex(ADbContract.SeenEntry.COLUMN_NAME_UPDATED_AT));

            this.id = id;
            this.seenId = seenId;
            this.capturedAt = capturedAt;
            this.location = location;
            this.capturedBy = capturedBy;
            this.createdAt = createdAt;
            this.updatedAt = updatedAt;
        }
    }

    public String getCapturedAt() {
        return capturedAt;
    }

    public String getCapturedBy() {
        return capturedBy;
    }

    public String getLocation() {
        return location;
    }



}
