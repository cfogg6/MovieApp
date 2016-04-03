package com.mymovieapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity that searches the movie database for a matching movie from the query.
 * Currently extends the Toolbar and Navigation Drawer from the parent for accessibility.
 */
public class SearchActivity extends ToolbarDrawerActivity {
    String url ="http://api.rottentomatoes.com/api/public/v1.0/movies.json?apikey=yedukp76ffytfuy24zsqk7f5";
    final Activity activity = this;
    JSONArray listOfMovies;

    private List<com.mymovieapp.Movie> searchMovies;

    /**
     * Initializes the data shown in the search
     */
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
            String idOfMovie = listOfMovies.getJSONObject(i).getString("id");
            com.mymovieapp.Movie toAdd = new com.mymovieapp.Movie(nameOfMovie, dateOfMovie, imageOfMovie, synopsisOfMovie, ratingRuntimeOfMovie, ratingToAdd, idOfMovie);
            searchMovies.add(i, toAdd);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        /*
        // Instantiate the RequestQueue.
        final RequestQueue queue = Volley.newRequestQueue(this);
        Button searchButton = (Button) findViewById(R.id.btn_search);
        ListView searchResultListView = (ListView) findViewById(R.id.lv_search_results);
        final MovieListAdapter searchListAdapter = new MovieListAdapter(activity, 0);
        searchResultListView.setAdapter(searchListAdapter);
        final EditText searchEditText = (EditText) findViewById(R.id.et_search);
        LinearLayout searchLinearLayout = (LinearLayout) findViewById(R.id.rl_search);
        // Add the request to the RequestQueue.
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url + "&q="
                        + searchEditText.getText().toString().replace(" ", "+") + "&page_limit=20",
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
        });

        /**
         * Makes keyboard disappear when you click away from an EditText field
         *//*
        searchLinearLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!(v instanceof EditText)) {
                    InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
                return true;
            }
        });*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        return super.onOptionsItemSelected(item);
    }
}