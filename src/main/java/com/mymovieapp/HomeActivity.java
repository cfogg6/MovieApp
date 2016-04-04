package com.mymovieapp;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

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

import java.util.ArrayList;
import java.util.List;

/**
 * Home Screen activity with two RecycleViews giving user glimpses at movie lists that may interest
 * them. Includes Toolbar and Navigation drawer to the rest of the user functions.
 */
public class HomeActivity extends ToolbarDrawerActivity {

    final Activity activity = this;
    JSONArray listOfMovies;

    private List<Movie> newMovies = new ArrayList<>();
    private List<Movie> newDVDs = new ArrayList<>();

    private void initializeData(List<Movie> movies) throws JSONException {
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
            String idOfMovie = listOfMovies.getJSONObject(i).getString("id");
            com.mymovieapp.Movie toAdd = new com.mymovieapp.Movie(nameOfMovie, dateOfMovie, imageOfMovie, synopsisOfMovie, ratingRuntimeOfMovie, idOfMovie, ratingToAdd);
            movies.add(i, toAdd);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Home");
        }
        showNewDVDs();
        showNewReleases();

        Button moreButton1 = (Button) findViewById(R.id.more_btn);
        moreButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(HomeActivity.this, NewMovieDrawerActivity.class);
                it.putExtra("tab", 0);
                startActivity(it);
            }
        });
        Button moreButton2 = (Button) findViewById(R.id.more_btn2);
        moreButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(HomeActivity.this, NewMovieDrawerActivity.class);
                it.putExtra("tab", 1);
                startActivity(it);
            }
        });
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

    /**
     * Update list to show new DVDs
     */
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
                                initializeData(newDVDs);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            //Initialize RecycleView, Layout Manager, and Adapter
                            RecyclerView rv = (RecyclerView) findViewById(R.id.home_rv1);
                            rv.setHasFixedSize(true);
                            LinearLayoutManager llm = new LinearLayoutManager(activity);
                            llm.setOrientation(LinearLayoutManager.HORIZONTAL);
                            rv.setLayoutManager(llm);
                            RVSearchAdapter adapter = new RVSearchAdapter(newDVDs);
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

    /**
     * Update list to show new releases
     */
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
                                initializeData(newMovies);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            //Initialize RecycleView, Layout Manager, and Adapter
                            RecyclerView rv = (RecyclerView) findViewById(R.id.home_rv2);
                            rv.setHasFixedSize(true);
                            LinearLayoutManager llm = new LinearLayoutManager(activity);
                            llm.setOrientation(LinearLayoutManager.HORIZONTAL);
                            rv.setLayoutManager(llm);
                            RVSearchAdapter adapter = new RVSearchAdapter(newMovies);
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
}

