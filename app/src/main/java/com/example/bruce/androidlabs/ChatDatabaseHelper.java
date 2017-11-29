package com.example.bruce.androidlabs;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static android.content.ContentValues.TAG;

/**
 * Created by bruce on 2017-10-09.
 */

public class ChatDatabaseHelper extends SQLiteOpenHelper {

    public static String DATABASE_NAME = "Message.db";
    public static String TABLE_NAME = "message_table";
    public static int VERSION_NUM = 105;

    public static final String KEY_ID = "_id";
    public static final String KEY_MESSAGE = "message";

    public static String[] MESSAGE_FIELDS = new String[]{
            KEY_ID,
            KEY_MESSAGE,
    };

    private static String CREATE_TABLE_MESSAGE =
            "CREATE TABLE " + TABLE_NAME + "( "
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_MESSAGE + " text);";

    public ChatDatabaseHelper(Context ctx){
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_MESSAGE);
        Log.i("ChatDatabaseHelper", "Calling onCreate");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {
        Log.w(TAG, "Upgrading database from version " + oldVer + " to " + newVer + ", which will destroy all old data,");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL(CREATE_TABLE_MESSAGE);
        Log.i("ChatDatabaseHelper","Calling onUpgrade, oldVersion=" + oldVer + "newVersion=" +newVer);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVer, int newVer) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL(CREATE_TABLE_MESSAGE);
        Log.i("ChatDatabaseHelper","Calling downUpgrade, oldVersion=" + oldVer + "newVersion=" +newVer);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        Log.i("Database ", "onOpen was called");
    }

    public void DeleteMessage (String id){
        this.getWritableDatabase().execSQL("DELETE FROM "+TABLE_NAME+" WHERE "+KEY_ID+" = "+id);
    }
}
