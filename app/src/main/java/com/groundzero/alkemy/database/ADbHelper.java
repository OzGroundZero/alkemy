package com.groundzero.alkemy.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

import com.groundzero.alkemy.utils.AResult;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by lwdthe1 on 9/20/2015.
 */
public class ADbHelper  extends SQLiteOpenHelper{
    public static final int DATABASE_VERSION = 4;
    public static final String DATABASE_NAME = "Alkemy.db";
    public static final String BACKUP_DATABASE_NAME = "Alkemy_Backup.db";

    private Context mContext;

    public ADbHelper (Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.mContext = context;
    }
    /**
     * Called when the database is created for the first time. This is where the
     * creation of tables and the initial population of the tables should happen.
     *
     * @param db The database.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ADbContract.createSeenTable());
        db.execSQL(ADbContract.createScannedObjectTable());
        db.execSQL(ADbContract.createScannedObjectLinkTable());
        db.execSQL(ADbContract.createConfigTable());
    }

    /**
     * Called when the database needs to be upgraded. The implementation
     * should use this method to drop tables, add tables, or do anything else it
     * needs to upgrade to the new schema version.
     * <p/>
     * <p>
     * The SQLite ALTER TABLE documentation can be found
     * <a href="http://sqlite.org/lang_altertable.html">here</a>. If you add new columns
     * you can use ALTER TABLE to insert them into a live table. If you rename or remove columns
     * you can use ALTER TABLE to rename the old table, then create the new table and then
     * populate the new table with the contents of the old table.
     * </p><p>
     * This method executes within a transaction.  If an exception is thrown, all changes
     * will automatically be rolled back.
     * </p>
     *
     * @param db         The database.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //update database
        //add new tables
        //add new rows to tables
        //drop rows from tables
        //etc
        db.execSQL(ADbContract.createConfigTable());
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion){
        //downgrade database
        //add new tables
        //add new rows to tables
        //drop rows from tables
        //etc
    }

    public void close(){
        this.close();
    }

    public AResult exportDB(){
        AResult result = new AResult();
        final String inFileName = mContext.getDatabasePath(DATABASE_NAME).getPath();

        String outFileName = Environment.getExternalStorageDirectory()+ "/" + BACKUP_DATABASE_NAME;
        File dbFile = new File(inFileName);
        FileInputStream fileInputStream = null;
        OutputStream fileOutputStream = null;
        try {
            fileInputStream = new FileInputStream(dbFile);
            // Open the empty db as the output stream
            fileOutputStream = new FileOutputStream(outFileName);

            // Transfer bytes from the fileInputStream file to the fileOutputStream
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fileInputStream.read(buffer))>0){
                fileOutputStream.write(buffer, 0, length);
            }

            result.setCode(AResult.Codes.SUCCESS);
            result.setMessage("Exported DB\nfrom " + inFileName + "\nto " + outFileName);
        } catch (FileNotFoundException e) {
            result.setCode(AResult.Codes.ERROR);
            result.setMessage(e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            result.setCode(AResult.Codes.ERROR);
            result.setMessage(e.getMessage());
            e.printStackTrace();
        } finally {
            // Close the streams
            if(fileInputStream != null){
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(fileOutputStream != null){
                try {
                    fileOutputStream.flush();
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    public AResult importDB(){
        AResult result = new AResult();
        final String inFileName = Environment.getExternalStorageDirectory()+"/" + BACKUP_DATABASE_NAME;
        String outFileName = mContext.getDatabasePath(DATABASE_NAME).getPath();
        File dbFile = new File(inFileName);
        FileInputStream fileInputStream = null;
        OutputStream fileOutputStream = null;
        try {
            fileInputStream = new FileInputStream(dbFile);
            // Open the empty db as the output stream
            fileOutputStream = new FileOutputStream(outFileName);

            // Transfer bytes from the fileInputStream file to the fileOutputStream
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fileInputStream.read(buffer))>0){
                fileOutputStream.write(buffer, 0, length);
            }

            result.setCode(AResult.Codes.SUCCESS);
            result.setMessage("Imported DB\nfrom " + inFileName + "\nto " + outFileName);
            result.addExtra("inFile", inFileName);
            result.addExtra("outFile", outFileName);
        } catch (FileNotFoundException e) {
            result.setCode(AResult.Codes.ERROR);
            result.setMessage(e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            result.setCode(AResult.Codes.ERROR);
            result.setMessage(e.getMessage());
            e.printStackTrace();
        } finally {
            // Close the streams
            if(fileInputStream != null){
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(fileOutputStream != null){
                try {
                    fileOutputStream.flush();
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }


}
