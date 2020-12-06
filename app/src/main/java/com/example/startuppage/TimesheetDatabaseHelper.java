package com.example.startuppage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class TimesheetDatabaseHelper extends SQLiteOpenHelper {
    static String DATABASE_NAME = "timesheet.db";
    static int VERSION_NUM = 2;
    final static String TABLE_NAME = "RECORDS";
    final static String KEY_DATE = "KEY_DATE";
    final static String KEY_START = "KEY_START";
    final static String KEY_END = "KEY_END";
    final static String KEY_HOURS = "KEY_HOURS";
    public TimesheetDatabaseHelper(Context ctx) {
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i("TimesheetDatabaseHelper", "Calling onCreate");
        String query = "create table RECORDS (KEY_ID integer primary key autoincrement, KEY_DATE text not null, KEY_START text not null, KEY_END text not null, KEY_HOURS text not null)";
        //String createDB = "create table " + TABLE_NAME + " ( " + KEY_ID + " integer primary key autoincrement, " + KEY_DATE + " text not null);";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i("TimesheetDatabaseHelper", "Calling onUpgrade, oldVersion=" + oldVersion + " newVersion=" + newVersion);
        String dropTable = "DROP TABLE IF EXISTS " + TABLE_NAME;
        db.execSQL(dropTable);
        onCreate(db);
    }
}
