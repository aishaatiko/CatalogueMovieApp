package com.nct.darkchocolate.cataloguemovieapp;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

public class MovieItems implements Parcelable {

    private int id;

    protected MovieItems(Parcel in) {
        id = in.readInt();
        title = in.readString();
        description = in.readString();
        poster_path = in.readString();
    }

    public static final Creator<MovieItems> CREATOR = new Creator<MovieItems>() {
        @Override
        public MovieItems createFromParcel(Parcel in) {
            return new MovieItems(in);
        }

        @Override
        public MovieItems[] newArray(int size) {
            return new MovieItems[size];
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

    public MovieItems(JSONObject object){

        try {
            int id = object.getInt("id");
            String title = object.getString("original_title");
            String description = object.getString("overview");
            String poster_path = object.getString("poster_path");

            this.id = id;
            this.title = title;
            this.description = description;
            this.poster_path = poster_path;

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
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(poster_path);
    }

}
