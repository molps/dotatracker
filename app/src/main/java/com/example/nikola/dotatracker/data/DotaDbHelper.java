package com.example.nikola.dotatracker.data;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.nikola.dotatracker.data.DotaContract.DotaEntry;
import com.example.nikola.dotatracker.data.DotaContract.DotaFollowing;

public class DotaDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "tracker.db";
    private static final int DATABASE_VERSION = 1;

    public DotaDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlSuggestion = "CREATE TABLE " + DotaEntry.TABLE_NAME + " ("
                + DotaEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DotaEntry.COLUMN_ENTRY + " TEXT NOT NULL COLLATE NOCASE UNIQUE ON CONFLICT REPLACE);";

        String sqlFollowing = "CREATE TABLE " + DotaFollowing.TABLE_NAME + " ("
                + DotaFollowing._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DotaFollowing.COLUMN_PLAYER_ID + " INTEGER NOT NULL, "
                + DotaFollowing.COLUMN_TAG + " TEXT);";

        db.execSQL(sqlSuggestion);
        db.execSQL(sqlFollowing);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DotaEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DotaFollowing.TABLE_NAME);
        onCreate(db);
    }
}
