package com.nct.darkchocolate.cataloguemovieapp.loader;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.net.Uri;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;
import com.nct.darkchocolate.cataloguemovieapp.BuildConfig;
import com.nct.darkchocolate.cataloguemovieapp.DetailItems;
import com.nct.darkchocolate.cataloguemovieapp.GenresList;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class DetailAsyncTaskLoader extends AsyncTaskLoader<DetailItems> {

    private DetailItems mData;
    private boolean mHasResult = false;

    private int mMovieId;

    public DetailAsyncTaskLoader(Context context, int movieId) {
        super(context);

        onContentChanged();
        this.mMovieId = movieId;
    }

    @Override
    protected void onStartLoading() {
        if (takeContentChanged())
            forceLoad();
        else if (mHasResult)
            deliverResult(mData);
    }

    @Override
    public void deliverResult(final DetailItems data) {
        mData = data;
        mHasResult = true;
        super.deliverResult(data);
    }

    @Override
    protected void onReset() {
        super.onReset();
        onStopLoading();
        if (mHasResult) {

            mData = null;
            mHasResult = false;
        }
    }

    private static final String apiKey = BuildConfig.TMDB_API_KEY;

    @Override
    public DetailItems loadInBackground() {
        SyncHttpClient client = new SyncHttpClient();

        final ArrayList<DetailItems> detailItemss = new ArrayList<>();
        final ArrayList<GenresList> genresLists = new ArrayList<>();

        String url = Uri.parse(BuildConfig.BASE_URL).buildUpon()
                .appendPath("3")
                .appendPath("movie")
                .appendPath(mMovieId+"")
                .appendQueryParameter("api_key", apiKey)
                .appendQueryParameter("language", "en-US")
                .build()
                .toString();
        //String url = "https://api.themoviedb.org/3/movie/"+mMovieId+"?api_key="+apiKey+"&language=en-US";

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
                    JSONArray genres = responseObject.getJSONArray("genres");

                    for (int i = 0; i < genres.length() ; i++){
                        JSONObject genre = genres.getJSONObject(i);
                        GenresList genresList = new GenresList(genre);
                        genresLists.add(genresList);
                    }

                    DetailItems detailItems = new DetailItems(responseObject,genresLists);
                    detailItemss.add(detailItems);

                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });

        DetailItems detailItems  = detailItemss.get(0);
        return detailItems;
    }
}
