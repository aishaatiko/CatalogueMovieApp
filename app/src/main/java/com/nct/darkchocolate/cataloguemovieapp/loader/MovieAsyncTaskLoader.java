package com.nct.darkchocolate.cataloguemovieapp.loader;

import android.net.Uri;
import android.support.v4.content.AsyncTaskLoader;
import android.content.Context;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;
import com.nct.darkchocolate.cataloguemovieapp.BuildConfig;
import com.nct.darkchocolate.cataloguemovieapp.MovieItems;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MovieAsyncTaskLoader extends AsyncTaskLoader<ArrayList<MovieItems>> {

    private ArrayList<MovieItems> mData;
    private boolean mHasResult = false;

    private String mSearchMovie;
    private String params;
    private String url;

    public MovieAsyncTaskLoader(final Context context, String searchMovie, String params) {
        super(context);

        onContentChanged();
        this.mSearchMovie = searchMovie;
        this.params = params;
    }

    @Override
    protected void onStartLoading() {
        if (takeContentChanged())
            forceLoad();
        else if (mHasResult)
            deliverResult(mData);
    }

    @Override
    public void deliverResult(final ArrayList<MovieItems> data) {
        mData = data;
        mHasResult = true;
        super.deliverResult(data);
    }

    @Override
    protected void onReset() {
        super.onReset();
        onStopLoading();
        if (mHasResult) {
            onReleaseResources(mData);
            mData = null;
            mHasResult = false;
        }
    }

    private static final String apiKey = BuildConfig.TMDB_API_KEY;

    @Override
    public ArrayList<MovieItems> loadInBackground() {
        SyncHttpClient client = new SyncHttpClient();

        final ArrayList<MovieItems> movieItemses = new ArrayList<>();

        if (params.equals("search")){
            url = Uri.parse(BuildConfig.BASE_URL).buildUpon()
                    .appendPath("3")
                    .appendPath("search")
                    .appendPath("movie")
                    .appendQueryParameter("api_key", apiKey)
                    .appendQueryParameter("language", "en-US")
                    .appendQueryParameter("query",mSearchMovie)
                    .build()
                    .toString();

        } else {
            url = Uri.parse(BuildConfig.BASE_URL).buildUpon()
                    .appendPath("3")
                    .appendPath("movie")
                    .appendPath(params)
                    .appendQueryParameter("api_key", apiKey)
                    .appendQueryParameter("language", "en-US")
                    .build()
                    .toString();
        }

        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                setUseSynchronousMode(true);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                try {
                    String result = new String(responseBody);
                    JSONObject responseObject = new JSONObject(result);
                    JSONArray list = responseObject.getJSONArray("results");

                    for (int i = 0; i < list.length() ; i++){
                        JSONObject movie = list.getJSONObject(i);
                        MovieItems movieItems = new MovieItems(movie);
                        movieItemses.add(movieItems);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });


        return movieItemses;
    }

    protected void onReleaseResources(ArrayList<MovieItems> data){

    }
}
