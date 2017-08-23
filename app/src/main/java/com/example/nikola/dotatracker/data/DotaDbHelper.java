package com.example.nikola.dotatracker.data;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.nikola.dotatracker.data.DotaContract.DotaEntry;

public class DotaDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "tracker.db";
    private static final int DATABASE_VERSION = 1;

    public DotaDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + DotaEntry.TABLE_NAME + " ("
                + DotaEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DotaEntry.COLUMN_ENTRY + " TEXT NOT NULL COLLATE NOCASE UNIQUE ON CONFLICT REPLACE);";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DotaEntry.TABLE_NAME);
        onCreate(db);
    }
}
