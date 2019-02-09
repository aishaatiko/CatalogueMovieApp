package com.nct.darkchocolate.cataloguemovieapp;

import android.content.Context;
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
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.nct.darkchocolate.cataloguemovieapp.adapter.MovieAdapter;
import com.nct.darkchocolate.cataloguemovieapp.loader.MovieAsyncTaskLoader;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchMovieFragment extends Fragment implements LoaderManager.LoaderCallbacks<ArrayList<MovieItems>>, SwipeRefreshLayout.OnRefreshListener {


    @BindView(R.id.rv_category) RecyclerView recyclerView;
    @BindView(R.id.et_search_movie) EditText edtSearch;
    @BindView(R.id.btn_search_movie)Button btnSearch;
    private ArrayList<MovieItems> movies = new ArrayList<>();
    MovieAdapter adapter;
    @BindView(R.id.pb_search) ProgressBar progressBar;
    @BindView(R.id.srl_search) SwipeRefreshLayout swipeRefreshLayout;

    public static final String STATE_LIST = "state_list";

    static final String EXTRAS_MOVIE = "extras_movie";
    Bundle bundle;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search_movie, container, false);

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

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(edtSearch.getWindowToken(), 0);

                String movie = edtSearch.getText().toString();

                if (TextUtils.isEmpty(movie)) return;
                Bundle bundle = new Bundle();
                bundle.putString(EXTRAS_MOVIE, movie);

                getLoaderManager().restartLoader(0, bundle, SearchMovieFragment.this);
            }
        });
        String movie = "exo";
        bundle = new Bundle();
        bundle.putString(EXTRAS_MOVIE, movie);


        swipeRefreshLayout.setOnRefreshListener(this);
        if (savedInstanceState == null) {

            getLoaderManager().initLoader(0, bundle, this);
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

    @Override
    public Loader<ArrayList<MovieItems>> onCreateLoader(int id, Bundle args) {
        progressBar.setVisibility(View.VISIBLE);
        String searchMovie = "";
        if (args != null){
            searchMovie = args.getString(EXTRAS_MOVIE);
        }
        return new MovieAsyncTaskLoader(getContext(), searchMovie, "search");
    }

    @Override
    public void onLoadFinished(@NonNull android.support.v4.content.Loader<ArrayList<MovieItems>> loader, final ArrayList<MovieItems> data) {
        adapter.setData(data);
        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                showSelectedMovie(data.get(position));
            }
        });
        progressBar.setVisibility(View.GONE);
        movies = data;
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
        getLoaderManager().restartLoader(0, bundle, SearchMovieFragment.this);
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelableArrayList(STATE_LIST, movies);
        super.onSaveInstanceState(outState);
    }


}
