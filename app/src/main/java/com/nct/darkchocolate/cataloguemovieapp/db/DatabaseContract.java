package com.nct.darkchocolate.cataloguemovieapp.db;

import android.provider.BaseColumns;

public class DatabaseContract {

    static String TABLE_NAME = "movie";
    static final class NoteColumns implements BaseColumns {
        static String MOVIE_ID = "movie_id";
        static String TITLE = "title";
        static String DESCRIPTION = "description";
        static String POSTER = "poster_path";
    }
}