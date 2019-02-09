package com.nct.darkchocolate.cataloguemovieapp;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

public class GenresList implements Parcelable {

    protected GenresList(Parcel in) {
        genreId = in.readInt();
        genreName = in.readString();
    }

    public static final Creator<GenresList> CREATOR = new Creator<GenresList>() {
        @Override
        public GenresList createFromParcel(Parcel in) {
            return new GenresList(in);
        }

        @Override
        public GenresList[] newArray(int size) {
            return new GenresList[size];
        }
    };

    public int getGenreId() {
        return genreId;
    }

    public String getGenreName() {
        return genreName;
    }

    private int genreId;
    private String genreName;

    public GenresList(JSONObject object) {
        try {
            int genreId = object.getInt("id");
            String genreName = object.getString("name");

            this.genreId = genreId;
            this.genreName = genreName;

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(genreId);
        dest.writeString(genreName);
    }
}
