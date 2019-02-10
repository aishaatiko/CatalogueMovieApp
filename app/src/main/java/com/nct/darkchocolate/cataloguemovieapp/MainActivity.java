package com.nct.darkchocolate.cataloguemovieapp;


import android.content.Intent;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

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
                    case R.id.menuSearch:
                        pageContent = new SearchMovieFragment();
                        title = getString(R.string.search);
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
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_change_settings) {
            Intent mIntent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
            startActivity(mIntent);
        }

        title = getSupportActionBar().getTitle().toString();
        return super.onOptionsItemSelected(item);
    }

}
