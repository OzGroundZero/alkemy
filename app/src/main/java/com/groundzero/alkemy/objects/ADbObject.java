package com.groundzero.alkemy.objects;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.groundzero.alkemy.database.ADbContract;
import com.groundzero.alkemy.database.AQuery;

import java.util.AbstractCollection;

/**
 * Created by lwdthe1 on 9/24/2015.
 */
public class ADbObject extends AObject {
    private static final String TAG = "ADbObject";

    protected String createdAt;
    Context context;
    Cursor cursor;

    public ADbObject(Context context, Cursor cursor) {
        this.context = context;
        this.cursor = cursor;
        if(cursor.getCount() < 1) this.id = CODE_BAD_OBJECT;
    }

    public ADbObject(Context context, Cursor cursor, int id){
        this.context = context;
        this.cursor = cursor;
        this.id = CODE_BAD_OBJECT;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    protected String updatedAt;

    public String getCreatedAt() {
        return createdAt;
    }

    public boolean isGood() {
        return id > 0;
    }

    public ADbObject constructObject(String tableName){
        switch (tableName){
            case ADbContract.SeenEntry.TABLE_NAME:
                return new Seen(context, cursor);
            case ADbContract.ScannedObjectEntry.TABLE_NAME:
                return new ScannedObject(context, cursor);
            case ADbContract.ScannedObjectLinkEntry.TABLE_NAME:
                return new ScannedObjectLink(context, cursor);
            case ADbContract.ConfigEntry.TABLE_NAME:
                return new Config(context, cursor);
            default:
                ADbObject badObject = new ADbObject(context, cursor, CODE_BAD_OBJECT);
                return badObject;
        }
    }
}
