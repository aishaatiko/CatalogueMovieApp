package com.nct.darkchocolate.cataloguemovieapp.db;

import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

public class DatabaseContract {

    public static final String AUTHORITY = "com.nct.darkchocolate.cataloguemovieapp";
    private static final String SCHEME = "content";

    private DatabaseContract(){}

    public static String TABLE_NAME = "movie";
    public static final class MovieColumns implements BaseColumns {
        public static String MOVIE_ID = "movie_id";
        public static String TITLE = "title";
        public static String DESCRIPTION = "description";
        public static String POSTER = "poster_path";

        public static final Uri CONTENT_URI = new Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build();
    }

    public static String getColumnString(Cursor cursor, String columnName) {
        return cursor.getString(cursor.getColumnIndex(columnName));
    }
    public static int getColumnInt(Cursor cursor, String columnName) {
        return cursor.getInt(cursor.getColumnIndex(columnName));
    }
    public static long getColumnLong(Cursor cursor, String columnName) {
        return cursor.getLong(cursor.getColumnIndex(columnName));
    }
}