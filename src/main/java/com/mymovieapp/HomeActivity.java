package com.mymovieapp;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Angelo on 3/28/2016.
 */
public class HomeActivity extends ToolbarDrawerActivity {

    class SearchMovie {
        String name;
        String picture;

        SearchMovie(String name, String picture) {
            this.name = name;
            this.picture = picture;
        }
    }

    private List<SearchMovie> searchMovies;

    private void initializeData() {
        searchMovies = new ArrayList<>();
        searchMovies.add(new SearchMovie("Something", "http://content6.flixster.com/movie/11/13/43/11134356_tmb.jpg"));
        searchMovies.add(new SearchMovie("Other Movie", "http://content6.flixster.com/movie/11/13/43/11134356_tmb.jpg"));
        searchMovies.add(new SearchMovie("Place Movie", "http://content6.flixster.com/movie/11/13/43/11134356_tmb.jpg"));
        searchMovies.add(new SearchMovie("Holder Movie", "http://content6.flixster.com/movie/11/13/43/11134356_tmb.jpg"));
        searchMovies.add(new SearchMovie("Moving Movies", "http://content6.flixster.com/movie/11/13/43/11134356_tmb.jpg"));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        /*RecyclerView rv = (RecyclerView) findViewById(R.id.search_rv);
        rv.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this)
        rv.setLayoutManager(llm);
        initializeData();
        RVSearchAdapter adapter = new RVSearchAdapter(searchMovies);
        rv.setAdapter(adapter);*/
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

