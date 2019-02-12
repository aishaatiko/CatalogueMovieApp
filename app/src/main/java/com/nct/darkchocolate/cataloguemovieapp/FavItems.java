package com.nct.darkchocolate.cataloguemovieapp;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

public class FavItems implements Parcelable {

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

    public FavItems(JSONObject object){

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
