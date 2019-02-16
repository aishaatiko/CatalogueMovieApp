package com.nct.darkchocolate.cataloguemovieapp;


import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import static com.nct.darkchocolate.cataloguemovieapp.SearchMovieFragment.KEY_MOVIE;

public class MainActivity extends AppCompatActivity {
    public static final String KEY_TITLE = "title";
    public static final String KEY_FRAGMENT = "fragment";
    private Fragment pageContent = new NowPlayingFragment();
    private String title = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        title = getString(R.string.now_playing);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case R.id.menuNow:
                        pageContent = new NowPlayingFragment();
                        title = getString(R.string.now_playing);
                        break;
                    case R.id.menuUpcoming:
                        pageContent = new UpcomingFragment();
                        title = getString(R.string.up_coming);
                        break;
                    case R.id.menuFav:
                        pageContent = new FavoriteFragment();
                        title = getString(R.string.favorite);
                        break;
                }
                setActionBarTitle(title);

                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, pageContent).commit();
                return true;
            }
        });

        if (savedInstanceState == null) {
            setActionBarTitle(title);
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, pageContent).commit();

        } else {
            pageContent = getSupportFragmentManager().getFragment(savedInstanceState, KEY_FRAGMENT);
            title = savedInstanceState.getString(KEY_TITLE);
            setActionBarTitle(title);
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, pageContent).commit();
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(KEY_TITLE, title);
        getSupportFragmentManager().putFragment(outState, KEY_FRAGMENT, pageContent);
        super.onSaveInstanceState(outState);
    }

    private void setActionBarTitle(String title){
        getSupportActionBar().setTitle(title);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        final SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        if (searchManager != null) {

            final SearchView searchView = (SearchView) (menu.findItem(R.id.search)).getActionView();
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            searchView.setQueryHint(getResources().getString(R.string.search_hint));
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    Toast.makeText(MainActivity.this, query, Toast.LENGTH_SHORT).show();
                    searchView.clearFocus();
                    title = query;
                    setActionBarTitle(title);
                    pageContent = new SearchMovieFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString(KEY_MOVIE, query);
                    pageContent.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, pageContent).commit();
                    return true;
                }
                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }
            });
        }
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_change_settings) {
            Intent mIntent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
            startActivity(mIntent);
        }
        return super.onOptionsItemSelected(item);
    }

}
