package com.mymovieapp;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;

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
 * Default Searchable Screen
 * Includes Toolbar and Navigation drawer to the rest of the user interface
 */
public class SearchManagerActivity extends BackToolbarActivity {
    /**
     * Current activity
     */
    private final Activity activity = this;
    /**
     * List of movies returned by the API
     */
    private JSONArray listOfMovies;
    /**
     * List of movies we will display
     */
    private List<Movie> searchMovies = new ArrayList<>();
    private RecyclerView rv;
    private com.mymovieapp.Movie toAdd;
    private String imageOfMovie;
    private String imdbOfMovie;

    /**
     * Initializes the list of movie based on reult of search query
     * @throws JSONException if a JSON field is missing
     */
    private void initializeData() throws JSONException {
        searchMovies = new ArrayList<>();
        for (int i = 0; i < listOfMovies.length(); i++) {
            final int index = i;
            //Assign name, date, details, and photo to the movies array
            String nameOfMovie = listOfMovies.getJSONObject(i).getString("title");
            String dateOfMovie = listOfMovies.getJSONObject(i).getString("year");
            String synopsisOfMovie = listOfMovies.getJSONObject(i).getString("synopsis");
            String ratingRuntimeOfMovie = listOfMovies.getJSONObject(i).getString("mpaa_rating") +
                    " Rating " +
                    listOfMovies.getJSONObject(i).getString("runtime") + " min";
            double ratingOfMovie = listOfMovies.getJSONObject(i).getJSONObject("ratings").getInt("audience_score");
            ratingOfMovie = ratingOfMovie / 20;

            Rating ratingToAdd = new Rating(nameOfMovie, ParseUser.getCurrentUser().getUsername());
            ratingToAdd.addRating(ratingOfMovie);

            toAdd = new com.mymovieapp.Movie(nameOfMovie, dateOfMovie, null, synopsisOfMovie, ratingRuntimeOfMovie, null, ratingToAdd);

            try {
                imdbOfMovie = listOfMovies.getJSONObject(i).getJSONObject("alternate_ids").getString("imdb");
                toAdd.setId(imdbOfMovie);
                String urlOMDB = "http://www.omdbapi.com/?i=tt" + toAdd.getId() + "&plot=short&r=json";
                RequestQueue queue = Volley.newRequestQueue(this);
                final StringRequest request = new StringRequest(Request.Method.GET, urlOMDB,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject result = new JSONObject(response);
                                    imageOfMovie = result.getString("Poster");
                                    ((RVSearchAdapter) rv.getAdapter()).movies.get(index).setImage(imageOfMovie);
                                } catch (JSONException e) {
                                }
                                rv.getAdapter().notifyDataSetChanged();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {}
                        });
                queue.add(request);

                toAdd = new com.mymovieapp.Movie(nameOfMovie, dateOfMovie, imageOfMovie, synopsisOfMovie, ratingRuntimeOfMovie, imdbOfMovie, ratingToAdd);
            } catch (JSONException e) {
                imdbOfMovie = listOfMovies.getJSONObject(i).getString("id");
                imageOfMovie = listOfMovies.getJSONObject(i).getJSONObject("posters").getString("detailed");
                toAdd.setImage(imageOfMovie);
                toAdd.setId(imdbOfMovie);
            }
            searchMovies.add(toAdd);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Search Movies");
        }
        handleIntent(getIntent());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        // Associate Searchable with the SearchView
        final SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView =
                (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        if (!Intent.ACTION_SEARCH.equals(getIntent().getAction())) {
            searchView.setIconified(false);
        }
        return true;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    /**
     * Load the Activity based on incoming intent from another activity
     * @param intent passed from another activity
     */
    private void handleIntent(Intent intent) {
        String url ="http://api.rottentomatoes.com/api/public/v1.0/movies.json?apikey=yedukp76ffytfuy24zsqk7f5";
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            final String query = intent.getStringExtra(SearchManager.QUERY);
            Log.e("SEARCH MANAGER ACTIVITY", "Searching for " + query);
            // Instantiate the RequestQueue.
            final RequestQueue queue = Volley.newRequestQueue(this);
            // Add the request to the RequestQueue.
            final StringRequest stringRequest = new StringRequest(Request.Method.GET, url + "&q="
                    + query.replace(" ", "+") + "&page_limit=20",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                listOfMovies = new JSONObject(response).getJSONArray("movies");
                                rv = (RecyclerView) findViewById(R.id.search_rv);
                                rv.setHasFixedSize(true);
                                final GridLayoutManager glm = new GridLayoutManager(activity, 2);
                                rv.setLayoutManager(glm);
                                try {
                                    initializeData();
                                } catch (JSONException e) {
                                    Log.d("e", String.valueOf(e));
                                }
                                final RVSearchAdapter adapter = new RVSearchAdapter(searchMovies);
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
}
