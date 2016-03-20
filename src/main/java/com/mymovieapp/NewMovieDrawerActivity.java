package com.mymovieapp;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Angelo on 3/13/2016.
 */
public class NewMovieDrawerActivity extends ToolbarDrawerActivity {
    class Movie {
        String name;
        String date;
        int photoId;

        Movie(String name, String date, int photoId) {
            this.name = name;
            this.date = date;
            this.photoId = photoId;
        }
    }

    private List<Movie> movies;

    private void initializeData() {
        movies = new ArrayList<>();
        movies.add(new Movie("Deadpool", "2016", R.drawable.ic_dehaze_24dp));
        movies.add(new Movie("Deadpool", "2016", R.drawable.ic_dehaze_24dp));
        movies.add(new Movie("Deadpool", "2016", R.drawable.ic_dehaze_24dp));
        movies.add(new Movie("Deadpool", "2016", R.drawable.ic_dehaze_24dp));
        movies.add(new Movie("Deadpool", "2016", R.drawable.ic_dehaze_24dp));
        movies.add(new Movie("Deadpool", "2016", R.drawable.ic_dehaze_24dp));
        movies.add(new Movie("Deadpool", "2016", R.drawable.ic_dehaze_24dp));
        movies.add(new Movie("Deadpool", "2016", R.drawable.ic_dehaze_24dp));
        movies.add(new Movie("Deadpool", "2016", R.drawable.ic_dehaze_24dp));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_movie);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setElevation(0);

        //Initialize Tab Layout
        TabLayout tabLayout = (TabLayout) findViewById(R.id.movie_tabs);
        tabLayout.addTab(tabLayout.newTab().setText("New DVDs"));
        tabLayout.addTab(tabLayout.newTab().setText("New Releases"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        //Initialize RecycleView, Layout Manager, and Adapter
        RecyclerView rv = (RecyclerView) findViewById(R.id.mov_rv);
        rv.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(llm);
        initializeData();
        RVMovAdapter adapter = new RVMovAdapter(movies);
        rv.setAdapter(adapter);


    }



    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
