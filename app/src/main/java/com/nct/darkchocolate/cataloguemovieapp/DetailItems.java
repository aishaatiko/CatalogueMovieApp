package com.nct.darkchocolate.cataloguemovieapp;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

import java.util.ArrayList;

public class DetailItems implements Parcelable {

    protected DetailItems(Parcel in) {
        id = in.readInt();
        poster_path = in.readString();
        original_title = in.readString();
        tagline = in.readString();
        vote_avarage = in.readString();
        runtime = in.readString();
        original_language = in.readString();
        release_date = in.readString();
        overview = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(poster_path);
        dest.writeString(original_title);
        dest.writeString(tagline);
        dest.writeString(vote_avarage);
        dest.writeString(runtime);
        dest.writeString(original_language);
        dest.writeString(release_date);
        dest.writeString(overview);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<DetailItems> CREATOR = new Creator<DetailItems>() {
        @Override
        public DetailItems createFromParcel(Parcel in) {
            return new DetailItems(in);
        }

        @Override
        public DetailItems[] newArray(int size) {
            return new DetailItems[size];
        }
    };

    public String getPoster_path() {
        return poster_path;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public String getTagline() {
        return tagline;
    }

    public String getVote_avarage() {
        return vote_avarage;
    }

    public String getRuntime() {
        return runtime;
    }

    public String getOriginal_language() {
        return original_language;
    }

    public ArrayList<GenresList> getGenres() {
        return genres;
    }

    public String getRelease_date() {
        return release_date;
    }

    public String getOverview() {
        return overview;
    }

    public int getId() {
        return id;
    }

    private int id;
    private String poster_path;
    private String original_title;
    private String tagline;
    private String vote_avarage;
    private String runtime;
    private String original_language;
    private ArrayList<GenresList> genres = new ArrayList<>();
    private String release_date;
    private String overview;

    public DetailItems(JSONObject object, ArrayList<GenresList> list) {

        try {
            int id = object.getInt("id");
            String poster_path = object.getString("poster_path");
            String original_title = object.getString("original_title");
            String tagline = object.getString("tagline");
            String vote_avarage = object.getString("vote_average");
            String runtime = object.getString("runtime");
            String original_language = object.getString("original_language");
            ArrayList<GenresList> genres = list;
            String release_date = object.getString("release_date");
            String overview = object.getString("overview");

            this.id = id;
            this.poster_path = poster_path;
            this.original_title = original_title;
            this.tagline = tagline;
            this.vote_avarage = vote_avarage;
            this.runtime = runtime;
            this.original_language = original_language;
            this.genres = genres;
            this.release_date = release_date;
            this.overview = overview;

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
