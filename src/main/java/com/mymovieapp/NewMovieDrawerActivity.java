package com.mymovieapp;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * New DVDs and New Releases screen with a Tab selector for the RecycleView Card Lists.
 */
public class NewMovieDrawerActivity extends ToolbarDrawerActivity {
    final Activity activity = this;
    JSONArray listOfMovies;

    private List<com.mymovieapp.Movie> movies;

    private void initializeData() throws JSONException {
        movies = new ArrayList<>();
        //iterate through the JSON array
        for (int i = 0; i < listOfMovies.length(); i++) {
            //Assign name, date, details, and photo to the movies array
            String nameOfMovie = listOfMovies.getJSONObject(i).getString("title");
            String dateOfMovie = listOfMovies.getJSONObject(i).getString("year");
            String imageOfMovie = listOfMovies.getJSONObject(i).getJSONObject("posters").getString("detailed");
            String synopsisOfMovie = listOfMovies.getJSONObject(i).getString("synopsis");
            String ratingRuntimeOfMovie = listOfMovies.getJSONObject(i).getString("mpaa_rating") +
                                          " Rating " +
                                          listOfMovies.getJSONObject(i).getString("runtime") + " min";
            double ratingOfMovie = listOfMovies.getJSONObject(i).getJSONObject("ratings").getInt("audience_score");
            ratingOfMovie = ratingOfMovie / 20;

            Rating ratingToAdd = new Rating(nameOfMovie, ParseUser.getCurrentUser().getUsername());
            ratingToAdd.addRating(ratingOfMovie);
            com.mymovieapp.Movie toAdd = new com.mymovieapp.Movie(nameOfMovie, dateOfMovie, imageOfMovie, synopsisOfMovie, ratingRuntimeOfMovie, ratingToAdd);
            movies.add(i, toAdd);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_movie);

        showNewDVDs();

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setElevation(0);

        //Initialize Tab Layout
        TabLayout tabLayout = (TabLayout) findViewById(R.id.movie_tabs);
        TabLayout.Tab dvdTab = tabLayout.newTab().setText("New DVDs");
        dvdTab.setTag("DVD Tab");
        TabLayout.Tab releasesTab = tabLayout.newTab().setText("New Releases");
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getTag() != null && tab.getTag().equals("DVD Tab")) {
                    showNewDVDs();
                } else {
                    showNewReleases();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        tabLayout.addTab(dvdTab);
        tabLayout.addTab(releasesTab);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
    }

    private void showNewDVDs() {
        String url = "http://api.rottentomatoes.com/api/public/v1.0/lists/dvds/new_releases.json?apikey=yedukp76ffytfuy24zsqk7f5";
        final RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url + "&page_limit=20",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        try {
                            listOfMovies = new JSONObject(response).getJSONArray("movies");
                            try {
                                initializeData();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            //Initialize RecycleView, Layout Manager, and Adapter
                            RecyclerView rv = (RecyclerView) findViewById(R.id.mov_rv);
                            rv.setHasFixedSize(true);
                            LinearLayoutManager llm = new LinearLayoutManager(activity);
                            llm.setOrientation(LinearLayoutManager.VERTICAL);
                            rv.setLayoutManager(llm);
                            RVMovAdapter adapter = new RVMovAdapter(movies);
                            rv.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        queue.add(stringRequest);
    }

    private void showNewReleases() {
        String url = "http://api.rottentomatoes.com/api/public/v1.0/lists/movies/in_theaters.json?apikey=yedukp76ffytfuy24zsqk7f5";
        final RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url + "&page_limit=20",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        try {
                            listOfMovies = new JSONObject(response).getJSONArray("movies");
                            try {
                                initializeData();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            //Initialize RecycleView, Layout Manager, and Adapter
                            RecyclerView rv = (RecyclerView) findViewById(R.id.mov_rv);
                            rv.setHasFixedSize(true);
                            LinearLayoutManager llm = new LinearLayoutManager(activity);
                            llm.setOrientation(LinearLayoutManager.VERTICAL);
                            rv.setLayoutManager(llm);
                            RVMovAdapter adapter = new RVMovAdapter(movies);
                            rv.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        queue.add(stringRequest);
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
