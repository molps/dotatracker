package com.example.nikola.dotatracker.data;


import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class DotaProvider extends ContentProvider {

    private static final int CODE_SEARCH = 100;
    private static final int CODE_SEARCH_ID = 101;

    private DotaDbHelper mDbHelper;
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private static UriMatcher buildUriMatcher() {
        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

        matcher.addURI(DotaContract.CONTENT_AUTHORITY, DotaContract.PATH_SEARCH, CODE_SEARCH);
        matcher.addURI(DotaContract.CONTENT_AUTHORITY, DotaContract.PATH_SEARCH + "/#", CODE_SEARCH_ID);

        return matcher;
    }

    @Override
    public boolean onCreate() {

        mDbHelper = new DotaDbHelper(getContext());

        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        return null;
    }



    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }
}
