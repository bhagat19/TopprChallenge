package com.example.amit.android_topprchallenge.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by amit on 25-09-2016.
 */
public class EventsDatabase extends SQLiteOpenHelper {

    final String TAG = EventsDatabase.class.getSimpleName();

    private static final String DATABASE_NAME = "topprEvents.db";
    private static final int DATABASE_VERSION = 2;

    public EventsDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + EventsContract.PATH_EVENTS + " ("
                + EventsContract.EventsColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + EventsContract.EventsColumns.COLUMN_ID + " INTEGER NOT NULL, "
                + EventsContract.EventsColumns.COLUMN_NAME + " TEXT NOT NULL,"
                + EventsContract.EventsColumns.COLUMN_IMAGE + " TEXT NOT NULL,"
                + EventsContract.EventsColumns.COLUMN_CATEGORY + " TEXT NOT NULL,"
                + EventsContract.EventsColumns.COLUMN_DESCRIPTION + " TEXT NOT NULL,"
                + EventsContract.EventsColumns.COLUMN_EXPERIENCE + " TEXT NOT NULL "
                /*
                + " FOREIGN KEY (" + EventsContract.EventsColumns.COLUMN_FAVORITE_RECORD_ID + ") REFERENCES " +
                EventsContract.PATH_FAVORITE + " (" + EventsContract.EventsColumns._ID + ")" +
                */
                + ")" );

        Log.v(TAG, "Events table created");

        /*

        db.execSQL("CREATE TABLE " + EventsContract.PATH_FAVORITE + " ("
                + EventsContract.EventsColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + EventsContract.EventsColumns.COLUMN_ID + " TEXT UNIQUE,"
                + EventsContract.EventsColumns.COLUMN_NAME + " TEXT NOT NULL,"
                + EventsContract.EventsColumns.COLUMN_IMAGE + " TEXT NOT NULL,"
                + EventsContract.EventsColumns.COLUMN_CATEGORY + " TEXT NOT NULL,"
                + EventsContract.EventsColumns.COLUMN_DESCRIPTION + " TEXT NOT NULL,"
                + EventsContract.EventsColumns.COLUMN_EXPERIENCE + " TEXT NOT NULL"
                + ")" );
*/

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + EventsContract.PATH_EVENTS);
        onCreate(db);
    }
}
