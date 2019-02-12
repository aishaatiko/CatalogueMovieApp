package com.nct.darkchocolate.cataloguemovieapp.db;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.nct.darkchocolate.cataloguemovieapp.FavItems;

import java.util.ArrayList;

import static android.provider.BaseColumns._ID;
import static com.nct.darkchocolate.cataloguemovieapp.db.DatabaseContract.NoteColumns.DESCRIPTION;
import static com.nct.darkchocolate.cataloguemovieapp.db.DatabaseContract.NoteColumns.MOVIE_ID;
import static com.nct.darkchocolate.cataloguemovieapp.db.DatabaseContract.NoteColumns.POSTER;
import static com.nct.darkchocolate.cataloguemovieapp.db.DatabaseContract.NoteColumns.TITLE;
import static com.nct.darkchocolate.cataloguemovieapp.db.DatabaseContract.TABLE_NAME;

public class MovieHelper {
    private static final String DATABASE_TABLE = TABLE_NAME;
    private static DatabaseHelper dataBaseHelper;
    private static MovieHelper INSTANCE;

    private static SQLiteDatabase database;

    private MovieHelper(Context context) {
        dataBaseHelper = new DatabaseHelper(context);
    }

    public static MovieHelper getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (SQLiteOpenHelper.class) {
                if (INSTANCE == null) {
                    INSTANCE = new MovieHelper(context);
                }
            }
        }
        return INSTANCE;
    }

    public void open() throws SQLException {
        database = dataBaseHelper.getWritableDatabase();
    }

    public void close() {
        dataBaseHelper.close();

        if (database.isOpen())
            database.close();
    }

    public ArrayList<FavItems> getAllMovies() {
        ArrayList<FavItems> arrayList = new ArrayList<>();
        Cursor cursor = database.query(DATABASE_TABLE, null,
                null,
                null,
                null,
                null,
                _ID + " ASC",
                null);
        cursor.moveToFirst();
        FavItems favItems;
        if (cursor.getCount() > 0) {
            do {
                favItems = new FavItems();
                favItems.setId(cursor.getInt(cursor.getColumnIndexOrThrow(_ID)));
                favItems.setMovie_id(cursor.getInt(cursor.getColumnIndexOrThrow(MOVIE_ID)));
                favItems.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(TITLE)));
                favItems.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(DESCRIPTION)));
                favItems.setPoster_path(cursor.getString(cursor.getColumnIndexOrThrow(POSTER)));

                arrayList.add(favItems);
                cursor.moveToNext();

            } while (!cursor.isAfterLast());
        }
        cursor.close();
        return arrayList;
    }

    public boolean getState(int movie_id){
        Cursor cursor = database.query(DATABASE_TABLE,
                null,
                MOVIE_ID + "=?",
                new String[] { ""+movie_id+"" },
                null,
                null,
                null,
                null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            cursor.close();
            return true;
        } else {
            cursor.close();
            return false;
        }
    }

    public long insertMovie(FavItems favItems) {
        ContentValues args = new ContentValues();
        args.put(MOVIE_ID, favItems.getMovie_id());
        args.put(TITLE, favItems.getTitle());
        args.put(DESCRIPTION, favItems.getDescription());
        args.put(POSTER, favItems.getPoster_path());
        return database.insert(DATABASE_TABLE, null, args);
    }

    public int deleteMovie(int id) {
        return database.delete(DATABASE_TABLE, MOVIE_ID + " = '" + id + "'", null);
    }
}