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
import android.view.MenuItem;

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
 * Created by Angelo on 3/30/2016.
 */
public class SearchManagerActivity extends BackToolbarActivity {
    String url ="http://api.rottentomatoes.com/api/public/v1.0/movies.json?apikey=yedukp76ffytfuy24zsqk7f5";
    final Activity activity = this;
    JSONArray listOfMovies;

    private List<Movie> searchMovies;

    private void initializeData() throws JSONException {
        searchMovies = new ArrayList<>();
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
            searchMovies.add(i, toAdd);
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        handleIntent(getIntent());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);

        // Associate Searchable with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Log.e("SEARCH MANAGER ACTIVITY", "Searching for " + query);
            // Instantiate the RequestQueue.
            final RequestQueue queue = Volley.newRequestQueue(this);
            final MovieListAdapter searchListAdapter = new MovieListAdapter(this, 0);
            // Add the request to the RequestQueue.
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url + "&q="
                    + query.replace(" ", "+") + "&page_limit=20",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Display the first 500 characters of the response string.
                            try {
                                searchListAdapter.updateJSON(new JSONObject(response));
                                searchListAdapter.notifyDataSetChanged();
                                listOfMovies = new JSONObject(response).getJSONArray("movies");
                                Log.d("response", String.valueOf(response));
                                Log.d("list of movies", String.valueOf(listOfMovies.length()));
                                RecyclerView rv = (RecyclerView) findViewById(R.id.search_rv);
                                rv.setHasFixedSize(true);
                                GridLayoutManager glm = new GridLayoutManager(activity, 2);
                                rv.setLayoutManager(glm);
                                try {
                                    initializeData();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                RVSearchAdapter adapter = new RVSearchAdapter(searchMovies);
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
}
