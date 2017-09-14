package com.example.nikola.dotatracker.data;


import android.net.Uri;
import android.provider.BaseColumns;

public class DotaContract {

    public static final String CONTENT_AUTHORITY = "com.example.nikola.dotatracker";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_SEARCH = "search";
    public static final String PATH_FOLLOWING = "following";

    public static class DotaEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_SEARCH).build();

        public static final String TABLE_NAME = "recententry";
        public static final String COLUMN_ENTRY = "entry";
    }

    public static class DotaFollowing implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_FOLLOWING).build();

        public static final String TABLE_NAME = "followingplayers";
        public static final String COLUMN_PLAYER_ID = "steamid";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_TAG = "tag";
        public static final String COLUMN_IMAGE_URL = "imageurl";
    }
}
