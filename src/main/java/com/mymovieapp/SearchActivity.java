package com.mymovieapp;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Corey on 2/20/16.
 */
public class SearchActivity extends ToolbarDrawerActivity {
    String url ="http://api.rottentomatoes.com/api/public/v1.0/movies.json?apikey=yedukp76ffytfuy24zsqk7f5";
    final Activity activity = this;

    private List<com.mymovieapp.Movie> searchMovies;

    private void initializeData() {
        searchMovies = new ArrayList<>();
//        searchMovies.add(new com.mymovieapp.Movie("Something", "http://content6.flixster.com/movie/11/13/43/11134356_tmb.jpg"));
//        searchMovies.add(new com.mymovieapp.Movie("Other Movie", "http://content6.flixster.com/movie/11/13/43/11134356_tmb.jpg"));
//        searchMovies.add(new com.mymovieapp.Movie("Place Movie", "http://content6.flixster.com/movie/11/13/43/11134356_tmb.jpg"));
//        searchMovies.add(new com.mymovieapp.Movie("Holder Movie", "http://content6.flixster.com/movie/11/13/43/11134356_tmb.jpg"));
//        searchMovies.add(new com.mymovieapp.Movie("Moving Movies", "http://content6.flixster.com/movie/11/13/43/11134356_tmb.jpg"));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

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

        RecyclerView rv = (RecyclerView) findViewById(R.id.search_rv);
        rv.setHasFixedSize(true);
        GridLayoutManager glm = new GridLayoutManager(this, 2);
        rv.setLayoutManager(glm);
        initializeData();
        RVSearchAdapter adapter = new RVSearchAdapter(searchMovies);
        rv.setAdapter(adapter);




        /**
         * Makes keyboard disappear when you click away from an EditText field
         */
        searchLinearLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!(v instanceof EditText)) {
                    InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
                return true;
            }
        });
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