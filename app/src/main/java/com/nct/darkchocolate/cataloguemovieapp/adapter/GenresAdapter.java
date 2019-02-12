package com.nct.darkchocolate.cataloguemovieapp.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nct.darkchocolate.cataloguemovieapp.GenresList;
import com.nct.darkchocolate.cataloguemovieapp.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GenresAdapter extends RecyclerView.Adapter<GenresAdapter.CategoryViewHolder> {
    private ArrayList<GenresList> mData = new ArrayList<>();

    public GenresAdapter() {

    }

    public ArrayList<GenresList> getListGenre() {
        return mData;
    }

    public void setData(ArrayList<GenresList> listGenre) {
        mData = listGenre;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemRow = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.genres_list, viewGroup, false);
        return new CategoryViewHolder(itemRow);
    }

    @Override
    public void onBindViewHolder(@NonNull GenresAdapter.CategoryViewHolder categoryViewHolder, int position) {
        categoryViewHolder.tvGenre.setText(getListGenre().get(position).getGenreName());
    }

    @Override
    public int getItemCount() {
        return getListGenre().size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_genres)
        TextView tvGenre;

        CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
