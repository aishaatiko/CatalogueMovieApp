package com.nct.darkchocolate.cataloguemovieapp.helper;

import android.database.Cursor;

import com.nct.darkchocolate.cataloguemovieapp.FavItems;

import java.util.ArrayList;

import static android.provider.BaseColumns._ID;
import static com.nct.darkchocolate.cataloguemovieapp.db.DatabaseContract.MovieColumns.DESCRIPTION;
import static com.nct.darkchocolate.cataloguemovieapp.db.DatabaseContract.MovieColumns.MOVIE_ID;
import static com.nct.darkchocolate.cataloguemovieapp.db.DatabaseContract.MovieColumns.POSTER;
import static com.nct.darkchocolate.cataloguemovieapp.db.DatabaseContract.MovieColumns.TITLE;

public class MappingHelper {

    public static ArrayList<FavItems> mapCursorToArrayList(Cursor movieCursor) {
        ArrayList<FavItems> movieList = new ArrayList<>();
        while (movieCursor.moveToNext()) {
            int id = movieCursor.getInt(movieCursor.getColumnIndexOrThrow(_ID));
            int movieId = movieCursor.getInt(movieCursor.getColumnIndexOrThrow(MOVIE_ID));
            String title = movieCursor.getString(movieCursor.getColumnIndexOrThrow(TITLE));
            String description = movieCursor.getString(movieCursor.getColumnIndexOrThrow(DESCRIPTION));
            String poster = movieCursor.getString(movieCursor.getColumnIndexOrThrow(POSTER));
            movieList.add(new FavItems(id, movieId, title, description, poster));
        }
        return movieList;
    }
}
