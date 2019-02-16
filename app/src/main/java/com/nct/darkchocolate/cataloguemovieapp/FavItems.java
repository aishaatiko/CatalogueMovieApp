package com.nct.darkchocolate.cataloguemovieapp;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.nct.darkchocolate.cataloguemovieapp.db.DatabaseContract;

import org.json.JSONObject;

import static android.provider.BaseColumns._ID;
import static com.nct.darkchocolate.cataloguemovieapp.db.DatabaseContract.getColumnInt;
import static com.nct.darkchocolate.cataloguemovieapp.db.DatabaseContract.getColumnString;

public class FavItems implements Parcelable {

    public FavItems(int id, int movieId, String title, String description, String poster) {
        this.id = id;
        this.movie_id = movieId;
        this.title = title;
        this.description = description;
        this.poster_path = poster;
    }

    public FavItems(Cursor cursor) {
        this.id = getColumnInt(cursor, _ID);
        this.movie_id = getColumnInt(cursor, DatabaseContract.MovieColumns.MOVIE_ID);
        this.title = getColumnString(cursor, DatabaseContract.MovieColumns.TITLE);
        this.description = getColumnString(cursor, DatabaseContract.MovieColumns.DESCRIPTION);
        this.poster_path = getColumnString(cursor, DatabaseContract.MovieColumns.POSTER);
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    private int id;

    public int getMovie_id() {
        return movie_id;
    }

    public void setMovie_id(int movie_id) {
        this.movie_id = movie_id;
    }

    private int movie_id;

    public FavItems() {
    }

    protected FavItems(Parcel in) {
        id = in.readInt();
        title = in.readString();
        description = in.readString();
        poster_path = in.readString();
    }

    public static final Creator<FavItems> CREATOR = new Creator<FavItems>() {
        @Override
        public FavItems createFromParcel(Parcel in) {
            return new FavItems(in);
        }

        @Override
        public FavItems[] newArray(int size) {
            return new FavItems[size];
        }
    };

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getPoster_path() {
        return poster_path;
    }

    private String title;
    private String description;
    private String poster_path;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(poster_path);
    }

}
