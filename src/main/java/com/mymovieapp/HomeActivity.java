package com.mymovieapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

    /**
     * Current Activity
     */
    private final Activity activity = this;
    /**
     * List of movies returned by JSON request
     */
    private JSONArray listOfMovies;

    /**
     * List of new releases
     */
    private List<Movie> newMovies = new ArrayList<>();
    /**
     * List of new DVDs
     */
    private List<Movie> newDVDs = new ArrayList<>();

    /**
     * Set all the data
     * @param movies list of movies
     * @throws JSONException in case JSON is bad
     */
    private void initializeData(List<Movie> movies) throws JSONException {
        for (int i = 0; i < listOfMovies.length(); i++) {
            //Assign name, date, details, and photo to the movies array
            final String nameOfMovie = listOfMovies.getJSONObject(i).getString("title");
            final String dateOfMovie = listOfMovies.getJSONObject(i).getString("year");
            final String imageOfMovie = listOfMovies.getJSONObject(i).getJSONObject("posters").getString("detailed");
            final String synopsisOfMovie = listOfMovies.getJSONObject(i).getString("synopsis");
            final String ratingRuntimeOfMovie = listOfMovies.getJSONObject(i).getString("mpaa_rating") +
                    " Rating " +
                    listOfMovies.getJSONObject(i).getString("runtime") + " min";
            double ratingOfMovie = listOfMovies.getJSONObject(i).getJSONObject("ratings").getInt("audience_score");
            ratingOfMovie = ratingOfMovie / 20;

            final Rating ratingToAdd = new Rating(nameOfMovie, ParseUser.getCurrentUser().getUsername());
            ratingToAdd.addRating(ratingOfMovie);
            final String idOfMovie = listOfMovies.getJSONObject(i).getString("id");
            final com.mymovieapp.Movie toAdd = new com.mymovieapp.Movie(nameOfMovie, dateOfMovie, imageOfMovie, synopsisOfMovie, ratingRuntimeOfMovie, idOfMovie, ratingToAdd);
            movies.add(i, toAdd);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        showNewDVDs();
        showNewReleases();

        final Button moreButton1 = (Button) findViewById(R.id.more_btn);
        moreButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent it = new Intent(HomeActivity.this, NewMovieDrawerActivity.class);
                startActivity(it);
            }
        });
        final Button moreButton2 = (Button) findViewById(R.id.more_btn2);
        moreButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent it = new Intent(HomeActivity.this, NewMovieDrawerActivity.class);
                startActivity(it);
            }
        });
    }

    /**
     * Update list to show new DVDs
     */
    private void showNewDVDs() {
        final String url = "http://api.rottentomatoes.com/api/public/v1.0/lists/dvds/new_releases.json?apikey=yedukp76ffytfuy24zsqk7f5";
        final RequestQueue queue = Volley.newRequestQueue(this);
        final StringRequest stringRequest = new StringRequest(Request.Method.GET, url + "&page_limit=20",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        try {
                            listOfMovies = new JSONObject(response).getJSONArray("movies");
                            try {
                                initializeData(newDVDs);
                            } catch (JSONException e) {
                                Log.d("e", String.valueOf(e));
                            }
                            //Initialize RecycleView, Layout Manager, and Adapter
                            final RecyclerView rv = (RecyclerView) findViewById(R.id.home_rv1);
                            rv.setHasFixedSize(true);
                            final LinearLayoutManager llm = new LinearLayoutManager(activity);
                            llm.setOrientation(LinearLayoutManager.HORIZONTAL);
                            rv.setLayoutManager(llm);
                            final RVSearchAdapter adapter = new RVSearchAdapter(newDVDs);
                            rv.setAdapter(adapter);
                        } catch (JSONException e) {
                            Log.d("e", String.valueOf(e));
                        }
                    }
                }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {}
                    });
        queue.add(stringRequest);
    }

    /**
     * Update list to show new releases
     */
    private void showNewReleases() {
        final String url = "http://api.rottentomatoes.com/api/public/v1.0/lists/movies/in_theaters.json?apikey=yedukp76ffytfuy24zsqk7f5";
        final RequestQueue queue = Volley.newRequestQueue(this);
        final StringRequest stringRequest = new StringRequest(Request.Method.GET, url + "&page_limit=20",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        try {
                            listOfMovies = new JSONObject(response).getJSONArray("movies");
                            try {
                                initializeData(newMovies);
                            } catch (JSONException e) {
                                Log.d("e", String.valueOf(e));
                            }
                            //Initialize RecycleView, Layout Manager, and Adapter
                            final RecyclerView rv = (RecyclerView) findViewById(R.id.home_rv2);
                            rv.setHasFixedSize(true);
                            final LinearLayoutManager llm = new LinearLayoutManager(activity);
                            llm.setOrientation(LinearLayoutManager.HORIZONTAL);
                            rv.setLayoutManager(llm);
                            final RVSearchAdapter adapter = new RVSearchAdapter(newMovies);
                            rv.setAdapter(adapter);
                        } catch (JSONException e) {
                            Log.d("e", String.valueOf(e));
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

