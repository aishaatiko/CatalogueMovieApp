package com.nct.darkchocolate.cataloguemovieapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.nct.darkchocolate.cataloguemovieapp.adapter.MovieAdapter;
import com.nct.darkchocolate.cataloguemovieapp.loader.MovieAsyncTaskLoader;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NowPlayingFragment extends Fragment implements LoaderManager.LoaderCallbacks<ArrayList<MovieItems>>, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.rv_category) RecyclerView recyclerView;
    ArrayList<MovieItems> movies = new ArrayList<>();

    MovieAdapter adapter;
    @BindView(R.id.pb_movie)
    ProgressBar progressBar;
    @BindView(R.id.srl_movie)
    SwipeRefreshLayout swipeRefreshLayout;

    public static final String STATE_LIST = "movies";

    public NowPlayingFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.rv_layout, container, false);
        ButterKnife.bind(this, rootView);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter = new MovieAdapter(getContext(),movies);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        if (savedInstanceState == null) {
            getLoaderManager().initLoader(0, null, this);
        } else {
            progressBar.setVisibility(View.GONE);

            movies = savedInstanceState.getParcelableArrayList(STATE_LIST);
            adapter.setData(movies);
            ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                @Override
                public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                    showSelectedMovie(movies.get(position));
                }
            });
        }

        swipeRefreshLayout.setOnRefreshListener(this);

    }


    @NonNull
    @Override
    public Loader<ArrayList<MovieItems>> onCreateLoader(int id, @Nullable Bundle args) {
        progressBar.setVisibility(View.VISIBLE);

        return new MovieAsyncTaskLoader(getContext(),"","now_playing");
    }

    @Override
    public void onLoadFinished(@NonNull android.support.v4.content.Loader<ArrayList<MovieItems>> loader, final ArrayList<MovieItems> data) {
        progressBar.setVisibility(View.GONE);

        movies = data;
        adapter.setData(movies);
        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                showSelectedMovie(data.get(position));
            }
        });

    }

    private void showSelectedMovie(MovieItems items){

        Intent detailIntent = new Intent(getActivity(), DetailActivity.class);
        int id = items.getId();
        detailIntent.putExtra(DetailActivity.EXTRA_ID, id);

        startActivity(detailIntent);
    }

    @Override
    public void onLoaderReset(@NonNull android.support.v4.content.Loader<ArrayList<MovieItems>> loader) {
        adapter.setData(null);
    }

    @Override
    public void onRefresh() {

        getLoaderManager().restartLoader(0, null, NowPlayingFragment.this);
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelableArrayList(STATE_LIST, movies);
        super.onSaveInstanceState(outState);
    }


}
