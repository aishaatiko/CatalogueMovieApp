package com.nct.darkchocolate.cataloguemovieapp;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.nct.darkchocolate.cataloguemovieapp.db.DatabaseContract;
import com.nct.darkchocolate.cataloguemovieapp.db.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

class StackRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private ArrayList<FavItems> favorites = new ArrayList<>();
    private Context context;
    int appWidgetId;

    public StackRemoteViewsFactory(Context context, Intent intent) {
        this.context = context;
        appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    @Override
    public void onCreate() {
        loadWidgetData();
    }

    @Override
    public void onDataSetChanged() {
        loadWidgetData();
    }

    private void loadWidgetData() {
        favorites.clear();

        DatabaseHelper helper = new DatabaseHelper(context);
        SQLiteDatabase database = helper.getWritableDatabase();

        Cursor cursors = database.query(DatabaseContract.TABLE_NAME, null, null, null, null, null, null);
        if (cursors != null && cursors.moveToFirst()) {
            do {
                FavItems fav = new FavItems(cursors);
                favorites.add(fav);

                Log.i("WIDGET_DATA", "loadWidgetData: " + fav.getTitle());
            } while (cursors.moveToNext());
            cursors.close();
        }
    }

    @Override
    public void onDestroy() {
    }

    @Override
    public int getCount() {
        return favorites.size();
    }

    @Override
    public RemoteViews getViewAt(final int position) {
        final RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_item);

        try {
            FavItems favoriteMovie = favorites.get(position);

            Bundle extras = new Bundle();
            extras.putInt(FavoriteMoviesWidget.EXTRA_ITEM, position);
            Intent fillInIntent = new Intent();
            fillInIntent.putExtras(extras);

            remoteViews.setImageViewResource(R.id.imageView, R.drawable.background_fill_genres);
            remoteViews.setTextViewText(R.id.banner_text, favoriteMovie.getTitle());
            remoteViews.setOnClickFillInIntent(R.id.imageView, fillInIntent);

            String posterUrl = BuildConfig.IMAGE_URL+favoriteMovie.getPoster_path();
            try {
                Bitmap preview = Glide.with(context)
                        .asBitmap()
                        .load(posterUrl)
                        .apply(new RequestOptions().fitCenter())
                        .submit()
                        .get();
                remoteViews.setImageViewBitmap(R.id.imageView , preview);

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }



        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}