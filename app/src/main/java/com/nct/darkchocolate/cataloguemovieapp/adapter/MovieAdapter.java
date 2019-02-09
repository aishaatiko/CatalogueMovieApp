package com.nct.darkchocolate.cataloguemovieapp.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nct.darkchocolate.cataloguemovieapp.BuildConfig;
import com.nct.darkchocolate.cataloguemovieapp.MovieItems;
import com.nct.darkchocolate.cataloguemovieapp.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.CategoryViewHolder> {
    private ArrayList<MovieItems> mData;


    public MovieAdapter(Context context, ArrayList<MovieItems> items) {
        this.context = context;
        this.mData = items;

    }

    private Context context;

    public ArrayList<MovieItems> getListMovie() {
        return mData;
    }

    public void setData(ArrayList<MovieItems> listMovie) {
        this.mData = new ArrayList<>();
        this.mData.addAll(listMovie);

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemRow = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.movie_items, viewGroup, false);
        return new CategoryViewHolder(itemRow);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder categoryViewHolder, int position) {
        categoryViewHolder.tvTitle.setText(getListMovie().get(position).getTitle());
        categoryViewHolder.tvDesc.setText(getListMovie().get(position).getDescription());
        Glide.with(context)
                .load(BuildConfig.IMAGE_URL+getListMovie().get(position).getPoster_path())
                //.load("https://image.tmdb.org/t/p/w185" +getListMovie().get(position).getPoster_path())
                .into(categoryViewHolder.ivPoster);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_title_list) TextView tvTitle;
        @BindView(R.id.tv_desc_list) TextView tvDesc;
        @BindView(R.id.iv_preview) ImageView ivPoster;

        CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }


    }
}