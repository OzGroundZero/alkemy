package com.groundzero.alkemy.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.groundzero.alkemy.objects.ADbObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by lwdthe1 on 9/20/2015.
 */
public class AQuery {
    public static final String TAG = "AQuery";
    /**
     * Created by Lwdthe1 on 6/29/2015.
     * A helper to manipulate sqlite database
     */

    private Context context;
    private ADbHelper dbHelper;
    private SQLiteDatabase db;

    private String tableName;
    //map of values, where column names are keys
    ContentValues values;

    public AQuery(Context context, String tableName) {
        this.context = context;

        this.tableName = tableName;
        this.values = new ContentValues();
    }

    public SQLiteDatabase getWritableDb() {
        dbHelper = new ADbHelper(this.context);
        return dbHelper.getWritableDatabase();
    }

    public SQLiteDatabase getReadableDb() {
        dbHelper = new ADbHelper(this.context);
        return dbHelper.getReadableDatabase();
    }

    /**
     * Add a value to ContentValue to the list of content values to be used in this query
     *
     * @param column the column to associate with the value in the database table
     * @param value  the value to be associated with the provided column in this query
     */
    public AQuery addCV(String column, String value) {
        values.put(column, value);
        return this;
    }

    /**
     * Adds all the provided values and their associated columns into the list of ContentValue pairs
     *
     * @param columns list of columns to associate with their values in the database table
     * @param values  list of values to be associated with the provided query in this query
     * @return
     */
    public int addAllCV(Collection columns, Collection values) {
        if (columns.size() < 1 || values.size() < 1)
            return 0;//one or more of the collections are empty
        if (columns.size() != values.size()) return -1;//columns and values not equal in length

        Object[] columnsArr = columns.toArray();
        Object[] valuesArr = values.toArray();
        for (int i = 0; i < columnsArr.length; i++) {
            this.addCV(columnsArr[i].toString(), valuesArr[i].toString());
        }
        return 1; // success
    }

    /**
     * @return whether or not the provided column exists in the table
     */
    public boolean columnExists(String columnName) {
        boolean columnExists;
        SQLiteDatabase db = getReadableDb();
        try {
            Cursor cursor = db.rawQuery("SELECT " + columnName + " FROM " + tableName + " LIMIT 0,1", null);
            cursor.close();
            db.close();
            return true;
        } catch (SQLiteException e) {
            db.close();
            return false;
        }
    }

    /**
     * Inserts the new record into the specified table
     * only if there are values to be inserted
     *
     * @return
     */
    public long insert() {
        if (values.size() < 1) return -1;
        SQLiteDatabase db = getWritableDb();

        long id = 0;

        id = db.insert(tableName, null, values);
        db.close();
        return id;
    }

    /**
     * Updates the column in the table with the associated id
     * only if there are values to update the table with
     *
     * @param id
     * @return
     */
    public int updateById(long id) {
        if (values.size() < 1) return -1;
        values.put(AEntry.COLUMN_NAME_UPDATED_AT, ADbContract.VALUE_CURRENT_LOCAL_TIMESTAMP);

        SQLiteDatabase db = getWritableDb();

        String selection = AEntry._ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};

        return db.update(tableName, values, selection, selectionArgs);
    }


    public int deleteById(long id) {
        SQLiteDatabase db = getWritableDb();

        String selection = AEntry._ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};

        return db.delete(tableName, selection, selectionArgs);
    }

    public ADbObject getById(long id) {
        SQLiteDatabase db = getReadableDb();

        String selection = AEntry._ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};

        Cursor cursor = db.query(tableName, null, selection, selectionArgs, null, null, null);
        ADbObject objectsFromCursor = getObjectFromCursor(cursor);
        return closeAndReturnObject(db, cursor, objectsFromCursor);
    }

    public List<ADbObject> fetchAll(){
        SQLiteDatabase db = getReadableDb();
        Cursor cursor = db.query(tableName, null, null, null, null, null, null);
        List<ADbObject> objectsFromCursor = getObjectsFromCursor(cursor);
        return closeAndReturnObjects(db, cursor, objectsFromCursor);
    }

    /**
     * @param cursor
     * @return
     */
    private ADbObject getObjectFromCursor(Cursor cursor) {
        cursor.moveToFirst();

        ADbObject aDbObject = new ADbObject(context, cursor).constructObject(tableName);
        return aDbObject;
    }

    /**
     * @param cursor
     * @return
     */
    private List<ADbObject> getObjectsFromCursor(Cursor cursor) {
        List<ADbObject> aDbObjects = new ArrayList<>();
        ADbObject aDbObject;

        while (cursor.moveToNext()) {
            aDbObject = new ADbObject(context, cursor);
            if (aDbObject != null){
                aDbObject = aDbObject.constructObject(tableName);
                if(aDbObject.isGood()) aDbObjects.add(aDbObject);
            }
        }
        return aDbObjects;
    }

    public ADbObject closeAndReturnObject(SQLiteDatabase db, Cursor cursor, ADbObject object) {
        db.close();
        cursor.close();
        return object;
    }

    public List<ADbObject> closeAndReturnObjects(SQLiteDatabase db, Cursor cursor, List<ADbObject> objects) {
        db.close();
        cursor.close();
        return objects;
    }

    public ADbObject getConfigByKey(String key) {
        SQLiteDatabase db = getReadableDb();

        String selection = ADbContract.ConfigEntry.COLUMN_NAME_KEY + " = ?";
        String[] selectionArgs = {String.valueOf(key)};

        Cursor cursor = db.query(tableName, null, selection, selectionArgs, null, null, null);
        ADbObject objectsFromCursor = getObjectFromCursor(cursor);
        return closeAndReturnObject(db, cursor, objectsFromCursor);
    }

    public int updateConfigByKey(String key) {
        if (values.size() < 1) return -1;
        values.put(AEntry.COLUMN_NAME_UPDATED_AT, ADbContract.VALUE_CURRENT_LOCAL_TIMESTAMP);

        SQLiteDatabase db = getWritableDb();

        String selection = ADbContract.ConfigEntry.COLUMN_NAME_KEY + " = ?";
        String[] selectionArgs = {String.valueOf(key)};

        return db.update(tableName, values, selection, selectionArgs);
    }
}

