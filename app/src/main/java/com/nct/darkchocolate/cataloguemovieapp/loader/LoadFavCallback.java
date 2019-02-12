package com.nct.darkchocolate.cataloguemovieapp.loader;

import com.nct.darkchocolate.cataloguemovieapp.FavItems;

import java.util.ArrayList;

public interface LoadFavCallback {
    void preExecute();
    void postExecute(ArrayList<FavItems> favItems);
}
