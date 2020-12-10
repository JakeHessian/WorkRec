package com.example.startuppage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class TimesheetDatabaseHelper extends SQLiteOpenHelper {
    static String DATABASE_NAME = "timesheet.db";
    static int VERSION_NUM = 7;
    final static String TABLE_NAME = "RECORDS";
    final static String KEY_DATE = "KEY_DATE";
    final static String KEY_START = "KEY_START";
    final static String KEY_END = "KEY_END";
    final static String KEY_HOURS = "KEY_HOURS";
    final static String KEY_SICK = "KEY_SICK";
    public TimesheetDatabaseHelper(Context ctx) {
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i("TimesheetDatabaseHelper", "Calling onCreate");
        String query = "create table RECORDS (KEY_ID integer primary key autoincrement, KEY_DATE text, KEY_START text, KEY_END text, KEY_HOURS text, KEY_SICK text)";
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
