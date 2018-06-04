package com.example.l.gamedb.data;


import android.net.Uri;

public class GameContract {
    public static final String AUTHORITY = "com.example.l.gamedb";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_TASKS = "games";

    public static final class GameEntry{

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_TASKS).build();

        public static final String TABLE_NAME = "games";

        public static final String COLUMN_ID = "id";
    }
}
