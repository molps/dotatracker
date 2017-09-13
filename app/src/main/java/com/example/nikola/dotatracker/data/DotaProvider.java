package com.example.nikola.dotatracker.data;


import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.nikola.dotatracker.data.DotaContract.DotaEntry;
import com.example.nikola.dotatracker.data.DotaContract.DotaFollowing;

public class DotaProvider extends ContentProvider {

    private static final String LOG_TAG = DotaProvider.class.getSimpleName();

    private static final int CODE_SEARCH = 100;
    private static final int CODE_SEARCH_ID = 101;

    private static final int CODE_FOLLOW = 200;
    private static final int CODE_FOLLOW_ID = 201;

    private DotaDbHelper mDbHelper;
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private static UriMatcher buildUriMatcher() {
        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

        matcher.addURI(DotaContract.CONTENT_AUTHORITY, DotaContract.PATH_SEARCH, CODE_SEARCH);
        matcher.addURI(DotaContract.CONTENT_AUTHORITY, DotaContract.PATH_SEARCH + "/*", CODE_SEARCH_ID);

        matcher.addURI(DotaContract.CONTENT_AUTHORITY, DotaContract.PATH_FOLLOWING, CODE_FOLLOW);
        matcher.addURI(DotaContract.CONTENT_AUTHORITY, DotaContract.PATH_FOLLOWING + "/#", CODE_FOLLOW_ID);

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
        Cursor cursor;

        switch (sUriMatcher.match(uri)) {
            case CODE_SEARCH:
                cursor = null;
                break;

            case CODE_SEARCH_ID:
                selection = DotaEntry.COLUMN_ENTRY + " LIKE ?";
                selectionArgs = new String[]{uri.getLastPathSegment() + "%"};
                sortOrder = DotaEntry._ID + " DESC";
                cursor = mDbHelper.getReadableDatabase().query(
                        DotaEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

            case CODE_FOLLOW:
                cursor = mDbHelper.getReadableDatabase().query(
                        DotaFollowing.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Can not perform query on this uri: " + uri);
        }
        if (cursor != null) {
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
        }
        return cursor;
    }


    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        Uri returnUri;
        long rowId;

        switch (sUriMatcher.match(uri)) {
            case CODE_SEARCH:
                rowId = mDbHelper.getWritableDatabase().insert(
                        DotaEntry.TABLE_NAME,
                        null,
                        values);
                if (rowId != -1)
                    returnUri = ContentUris.withAppendedId(uri, rowId);
                else
                    throw new SQLException("Failed to insert row into: " + uri);
                break;

            case CODE_FOLLOW:
                rowId = mDbHelper.getWritableDatabase().insert(
                        DotaFollowing.TABLE_NAME,
                        null,
                        values);
                if (rowId != -1)
                    returnUri = ContentUris.withAppendedId(uri, rowId);
                else
                    throw new SQLException("Failed to insert row into: " + uri);
                break;

            default:
                throw new UnsupportedOperationException("Unkown uri " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);


        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int deletedRows;

        switch (sUriMatcher.match(uri)) {
            case CODE_FOLLOW_ID:
                selection = DotaFollowing.COLUMN_PLAYER_ID + "=?";
                selectionArgs = new String[]{uri.getLastPathSegment()};
                deletedRows = mDbHelper.getWritableDatabase().delete(
                        DotaFollowing.TABLE_NAME,
                        selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unkown uri " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return deletedRows;
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
