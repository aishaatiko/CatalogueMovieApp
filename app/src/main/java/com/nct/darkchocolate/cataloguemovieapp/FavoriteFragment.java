package com.nct.darkchocolate.cataloguemovieapp;

import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.nct.darkchocolate.cataloguemovieapp.adapter.FavAdapter;
import com.nct.darkchocolate.cataloguemovieapp.db.MovieHelper;
import com.nct.darkchocolate.cataloguemovieapp.loader.LoadFavCallback;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.nct.darkchocolate.cataloguemovieapp.DetailActivity.EXTRA_POSITION;
import static com.nct.darkchocolate.cataloguemovieapp.db.DatabaseContract.MovieColumns.CONTENT_URI;
import static com.nct.darkchocolate.cataloguemovieapp.helper.MappingHelper.mapCursorToArrayList;


public class FavoriteFragment extends Fragment implements LoadFavCallback{

    @BindView(R.id.rv_category)
    RecyclerView recyclerView;
    ArrayList<FavItems> movies = new ArrayList<>();

    FavAdapter adapter;
    @BindView(R.id.pb_movie)
    ProgressBar progressBar;
    @BindView(R.id.srl_movie)
    SwipeRefreshLayout swipeRefreshLayout;

    public static final String STATE_LIST = "movies";

    private MovieHelper movieHelper;

    public FavoriteFragment() {
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

        adapter = new FavAdapter(getContext(),movies);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        movieHelper = MovieHelper.getInstance(getActivity());
        movieHelper.open();

        recyclerView.setAdapter(adapter);
        progressBar.setVisibility(View.GONE);


        if (savedInstanceState == null) {
            new LoadFavAsync(getContext(), this).execute();
        } else {
            movies = savedInstanceState.getParcelableArrayList(STATE_LIST);
            if (movies != null) {
                adapter.setData(movies);
            }
        }
        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                showSelectedMovie(movies.get(position),position);
            }
        });

        swipeRefreshLayout.setRefreshing(false);
        swipeRefreshLayout.setEnabled(false);
    }

    private void showSelectedMovie(FavItems items, int position){

        Intent detailIntent = new Intent(getActivity(), DetailActivity.class);
        int id = items.getMovie_id();
        detailIntent.putExtra(DetailActivity.EXTRA_ID, id);
        detailIntent.putExtra(EXTRA_POSITION, position);

        startActivityForResult(detailIntent, DetailActivity.RESULT_DELETE);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelableArrayList(STATE_LIST, adapter.getListMovie());
        super.onSaveInstanceState(outState);
    }


    @Override
    public void preExecute() {
        progressBar.setVisibility(View.VISIBLE);

    }

    @Override
    public void postExecute(Cursor cursor) {
        progressBar.setVisibility(View.INVISIBLE);
        if (cursor != null){
            movies = mapCursorToArrayList(cursor);
        }
        if (movies.size() > 0) {
            adapter.setData(movies);
        } else {
            adapter.setData(new ArrayList<FavItems>());
            Toast.makeText(getContext(), R.string.nothing,Toast.LENGTH_SHORT).show();
        }
    }


    private static class LoadFavAsync extends AsyncTask<Void, Void, Cursor> {

        private final WeakReference<Context> weakContext;
        private final WeakReference<LoadFavCallback> weakCallback;

        private LoadFavAsync(Context context, LoadFavCallback callback) {
            weakContext = new WeakReference<>(context);
            weakCallback = new WeakReference<>(callback);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            weakCallback.get().preExecute();
        }

        @Override
        protected Cursor doInBackground(Void... voids) {
            Context context = weakContext.get();
            return context.getContentResolver().query(CONTENT_URI, null, null, null, null);
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            super.onPostExecute(cursor);
            if (cursor != null)
                weakCallback.get().postExecute(cursor);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null) {
            if (requestCode == DetailActivity.REQUEST_ADD) {
                if (resultCode == DetailActivity.RESULT_ADD) {
                    FavItems favItems = data.getParcelableExtra(DetailActivity.EXTRA_MOVIE);
                    adapter.addItem(favItems);
                    recyclerView.smoothScrollToPosition(adapter.getItemCount() - 1);
                }
            }
            else  {
                if (resultCode == DetailActivity.RESULT_DELETE) {
                    int position = data.getIntExtra(EXTRA_POSITION, 0);
                    adapter.removeItem(position);

                }
            }
        }
    }

    public static class DataObserver extends ContentObserver {
        final Context context;
        public DataObserver(Handler handler, Context context) {
            super(handler);
            this.context = context;
        }
        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            new LoadFavAsync(context, (LoadFavCallback) context).execute();
        }
    }

}
