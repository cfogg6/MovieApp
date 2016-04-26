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
    private String imageOfMovie;

    private RecyclerView rv1;
    private RecyclerView rv2;
    private com.mymovieapp.Movie toAdd;

    /**
     * List of new releases
     */
    private final List<Movie> newMovies = new ArrayList<>();
    /**
     * List of new DVDs
     */
    private final List<Movie> newDVDs = new ArrayList<>();

    /**
     * Set all the data
     * @param movies list of movies
     * @throws JSONException in case JSON is bad
     */
    private void initializeData(final List<Movie> movies) throws JSONException {
        for (int i = 0; i < listOfMovies.length(); i++) {
            final int index = i;
            //Assign name, date, details, and photo to the movies array
            final String nameOfMovie = listOfMovies.getJSONObject(i).getString("title");
            final String dateOfMovie = listOfMovies.getJSONObject(i).getString("year");
            final String synopsisOfMovie = listOfMovies.getJSONObject(i).getString("synopsis");
            final String ratingRuntimeOfMovie = listOfMovies.getJSONObject(i).getString("mpaa_rating") +
                    " Rating " +
                    listOfMovies.getJSONObject(i).getString("runtime") + " min";
            double ratingOfMovie = listOfMovies.getJSONObject(i).getJSONObject("ratings").getInt("audience_score");
            ratingOfMovie = ratingOfMovie / 20;
            final Rating ratingToAdd = new Rating(nameOfMovie, ParseUser.getCurrentUser().getUsername());
            ratingToAdd.addRating(ratingOfMovie);

            toAdd = new com.mymovieapp.Movie(nameOfMovie, dateOfMovie, null, synopsisOfMovie, ratingRuntimeOfMovie, null, ratingToAdd);

            try {
                final String imdbOfMovie = listOfMovies.getJSONObject(i).getJSONObject("alternate_ids").getString("imdb");
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
                                    //imageOfMovie = "http://ia.media-imdb.com/images/M/MV5BOTMyMjEyNzIzMV5BMl5BanBnXkFtZTgwNzIyNjU0NzE@._V1_SX300.jpg";
                                    ((RVSearchAdapter) rv2.getAdapter()).movies.get(index).setImage(imageOfMovie);
                                } catch (JSONException e) {
                                }
                                rv2.getAdapter().notifyDataSetChanged();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {}
                        });
                queue.add(request);

                toAdd = new com.mymovieapp.Movie(nameOfMovie, dateOfMovie, imageOfMovie, synopsisOfMovie, ratingRuntimeOfMovie, imdbOfMovie, ratingToAdd);
            } catch (JSONException e) {
                imageOfMovie = listOfMovies.getJSONObject(i).getJSONObject("posters").getString("detailed");
                toAdd.setImage(imageOfMovie);
            }
            movies.add(toAdd);
                    }
    }

    private void initializeDataDVDS(final List<Movie> movies) throws JSONException {
        for (int i = 0; i < listOfMovies.length(); i++) {
            final int index = i;
            //Assign name, date, details, and photo to the movies array
            final String nameOfMovie = listOfMovies.getJSONObject(i).getString("title");
            final String dateOfMovie = listOfMovies.getJSONObject(i).getString("year");
            final String synopsisOfMovie = listOfMovies.getJSONObject(i).getString("synopsis");
            final String ratingRuntimeOfMovie = listOfMovies.getJSONObject(i).getString("mpaa_rating") +
                    " Rating " +
                    listOfMovies.getJSONObject(i).getString("runtime") + " min";
            double ratingOfMovie = listOfMovies.getJSONObject(i).getJSONObject("ratings").getInt("audience_score");
            ratingOfMovie = ratingOfMovie / 20;
            final Rating ratingToAdd = new Rating(nameOfMovie, ParseUser.getCurrentUser().getUsername());
            ratingToAdd.addRating(ratingOfMovie);

            toAdd = new com.mymovieapp.Movie(nameOfMovie, dateOfMovie, null, synopsisOfMovie, ratingRuntimeOfMovie, null, ratingToAdd);

            try {
                final String imdbOfMovie = listOfMovies.getJSONObject(i).getJSONObject("alternate_ids").getString("imdb");
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
                                    //imageOfMovie = "http://ia.media-imdb.com/images/M/MV5BOTMyMjEyNzIzMV5BMl5BanBnXkFtZTgwNzIyNjU0NzE@._V1_SX300.jpg";
                                    ((RVSearchAdapter) rv1.getAdapter()).movies.get(index).setImage(imageOfMovie);
                                } catch (JSONException e) {
                                }
                                rv1.getAdapter().notifyDataSetChanged();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {}
                        });
                queue.add(request);

                toAdd = new com.mymovieapp.Movie(nameOfMovie, dateOfMovie, imageOfMovie, synopsisOfMovie, ratingRuntimeOfMovie, imdbOfMovie, ratingToAdd);
            } catch (JSONException e) {
                imageOfMovie = listOfMovies.getJSONObject(i).getJSONObject("posters").getString("detailed");
                toAdd.setImage(imageOfMovie);
            }
            //toAdd = new com.mymovieapp.Movie(nameOfMovie, dateOfMovie, imageOfMovie, synopsisOfMovie, ratingRuntimeOfMovie, imdbOfMovie, ratingToAdd);
            movies.add(toAdd);
            Log.d("image of movie", String.valueOf(imageOfMovie));
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

        final Button moreButton1 = (Button) findViewById(R.id.more_btn);
        if (moreButton1 != null) {
            moreButton1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Intent it = new Intent(HomeActivity.this, NewMovieDrawerActivity.class);
                    it.putExtra("tab", 0);
                    startActivity(it);
                }
            });
        }
        final Button moreButton2 = (Button) findViewById(R.id.more_btn2);
        if (moreButton2 != null) {
            moreButton2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Intent it = new Intent(HomeActivity.this, NewMovieDrawerActivity.class);
                    it.putExtra("tab", 1);
                    startActivity(it);
                }
            });
        }
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
                        //Initialize RecycleView, Layout Manager, and Adapter
                        rv1 = (RecyclerView) findViewById(R.id.home_rv1);
                        rv1.setHasFixedSize(true);
                        final LinearLayoutManager llm = new LinearLayoutManager(activity);
                        llm.setOrientation(LinearLayoutManager.HORIZONTAL);
                        rv1.setLayoutManager(llm);
                        final RVSearchAdapter adapter = new RVSearchAdapter(newDVDs, 1);
                        rv1.setAdapter(adapter);
                        // Display the first 500 characters of the response string.
                        try {
                            listOfMovies = new JSONObject(response).getJSONArray("movies");
                            try {
                                initializeDataDVDS(newDVDs);
                            } catch (JSONException e) {
                                Log.d("e", String.valueOf(e));
                            }
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
                        //Initialize RecycleView, Layout Manager, and Adapter
                        rv2 = (RecyclerView) findViewById(R.id.home_rv2);
                        rv2.setHasFixedSize(true);
                        final LinearLayoutManager llm = new LinearLayoutManager(activity);
                        llm.setOrientation(LinearLayoutManager.HORIZONTAL);
                        rv2.setLayoutManager(llm);
                        final RVSearchAdapter adapter = new RVSearchAdapter(newMovies, 1);
                        rv2.setAdapter(adapter);
                        // Display the first 500 characters of the response string.
                        try {
                            listOfMovies = new JSONObject(response).getJSONArray("movies");
                            try {
                                initializeData(newMovies);
                            } catch (JSONException e) {
                                Log.d("e", String.valueOf(e));
                            }
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

    @Override
    public void onBackPressed() {
        if (!(getIntent().hasExtra("Login"))) {
            super.onBackPressed();
        }
    }
}

