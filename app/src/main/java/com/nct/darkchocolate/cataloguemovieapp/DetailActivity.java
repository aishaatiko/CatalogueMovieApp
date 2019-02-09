package com.nct.darkchocolate.cataloguemovieapp;

import android.app.LoaderManager;
import android.content.Loader;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nct.darkchocolate.cataloguemovieapp.adapter.GenresAdapter;
import com.nct.darkchocolate.cataloguemovieapp.loader.DetailAsyncTaskLoader;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class DetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<DetailItems> {

    @BindView(R.id.tv_title) TextView tvTitle;
    @BindView(R.id.tv_tagline) TextView tvTagline;
    @BindView(R.id.tv_rating) TextView tvRating;
    @BindView(R.id.tv_duration) TextView tvDuration;
    @BindView(R.id.tv_language) TextView tvLanguage;
    @BindView(R.id.tv_release) TextView tvRelease;
    @BindView(R.id.tv_overview) TextView tvOverview;
    @BindView(R.id.iv_poster) ImageView ivPoster;
    @BindView(R.id.rv_genre) RecyclerView rvGenre;
    @BindView(R.id.pb_detail) ProgressBar progressBar;

    static final String EXTRA_ID = "extra_id";
    public DetailItems mData;
    public ArrayList<GenresList> genres = new ArrayList<>();
    GenresAdapter adapter;

    public static final String STATE_DATA = "state_data";
    public static final String STATE_LIST = "genres";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ButterKnife.bind(this);

        rvGenre.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        adapter = new GenresAdapter(this);
        rvGenre.setAdapter(adapter);

        int movieId = getIntent().getIntExtra(EXTRA_ID, 0);
        Bundle bundle = new Bundle();
        bundle.putInt(EXTRA_ID, movieId);
        if (savedInstanceState == null){
            getLoaderManager().initLoader(0, bundle, this).forceLoad();
        } else {
            progressBar.setVisibility(View.GONE);

            genres = savedInstanceState.getParcelableArrayList(STATE_LIST);
            mData = savedInstanceState.getParcelable(STATE_DATA);
            adapter.setData(genres);
            setTitle(mData.getOriginal_title());
            Glide.with(this)
                    .load(BuildConfig.IMAGE_URL+mData.getPoster_path())
                    .into(ivPoster);
            tvTitle.setText(mData.getOriginal_title());
            tvTagline.setText(mData.getTagline());
            tvRating.setText(mData.getVote_avarage());
            tvDuration.setText(mData.getRuntime());
            tvLanguage.setText(mData.getOriginal_language());
            tvRelease.setText(mData.getRelease_date());
            tvOverview.setText(mData.getOverview());
        }


    }

    @Override
    public Loader<DetailItems> onCreateLoader(int id, Bundle args) {
        progressBar.setVisibility(View.VISIBLE);

        int movieId = 0;
        if (args != null){
            movieId = args.getInt(EXTRA_ID);
        }

        return new DetailAsyncTaskLoader(this, movieId);
    }

    @Override
    public void onLoadFinished(Loader<DetailItems> loader, DetailItems data) {
        mData = data;
        setTitle(mData.getOriginal_title());
        Glide.with(this)
                .load(BuildConfig.IMAGE_URL+mData.getPoster_path())
                .into(ivPoster);
        tvTitle.setText(mData.getOriginal_title());
        tvTagline.setText(mData.getTagline());
        tvRating.setText(mData.getVote_avarage());
        tvDuration.setText(mData.getRuntime());
        tvLanguage.setText(mData.getOriginal_language());
        tvRelease.setText(mData.getRelease_date());
        tvOverview.setText(mData.getOverview());
        adapter.setData(mData.getGenres());
        genres = mData.getGenres();
        progressBar.setVisibility(View.GONE);

    }

    @Override
    public void onLoaderReset(Loader<DetailItems> loader) {
        mData = null;

    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId() == android.R.id.home) {
//            finish();
//            return true;
//        }
//
//        if (item.getItemId() == R.id.add_to_favorite) {
//            item.setIcon(R.drawable.ic_favorite_white_24dp);
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.detail_menu, menu);
//        return super.onCreateOptionsMenu(menu);
//    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable(STATE_DATA, mData);
        outState.putParcelableArrayList(STATE_LIST, genres);
        super.onSaveInstanceState(outState);
    }
}
