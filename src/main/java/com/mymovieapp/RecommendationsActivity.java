package com.mymovieapp;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

//import android.widget.ListView;
//import org.json.JSONArray;

/**
 * Recommendations screen that recommends movies by the ratings of other users with the same major.
 * Includes a spinner to select the major filter and cards of the respective movies.
 */
public class RecommendationsActivity extends ToolbarDrawerActivity {
    Spinner spinner;
    ArrayList<Rating> ratings = new ArrayList<>();
    RecyclerView rv;

    private ArrayList<com.mymovieapp.Movie> movies = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommendations);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Recommendations");
        }

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            myToolbar.setElevation(0);
        }

        Toolbar spinBar = (Toolbar) findViewById(R.id.spinner_toolbar);

        View spinnerContainer = LayoutInflater.from(this)
                .inflate(R.layout.toolbar_spinner,spinBar, false);
        Toolbar.LayoutParams lp = new Toolbar.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        spinBar.addView(spinnerContainer, lp);

        MajorSpinnerAdapter spinnerAdapter = new MajorSpinnerAdapter(this, false);
        spinnerAdapter.addItems(
                Arrays.asList(getResources().getStringArray(R.array.majors_array)));
        spinner = (Spinner) spinnerContainer.findViewById(R.id.toolbar_spinner);
        // Apply the adapter to the spinner
        spinner.setAdapter(spinnerAdapter);

        spinner.setOnItemSelectedListener((new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        }));

        //Initialize RecycleView, Layout Manager, and Adapter
        rv = (RecyclerView) findViewById(R.id.mov_rv);
        rv.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(llm);
        RVMovAdapter adapter = new RVMovAdapter(movies);
        rv.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateList();
    }

    /**
     * Update list of movies showing
     */
    private void updateList() {
        movies = new ArrayList<>();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Ratings");
        query.whereEqualTo("major", spinner.getSelectedItem().toString());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null) {
                    for (ParseObject element : list) {
                        Rating newRating = new Rating(element.getString("title"),
                                element.getString("username"));
                        newRating.addRating(element.getDouble("rating"));
                        int index = ratings.lastIndexOf(newRating);
                        if (index > -1) {
                            ratings.get(index).addRating(element.getDouble("rating"));
                        } else {
                            ratings.add(newRating);
                        }

                        com.mymovieapp.Movie tempMovie = (new com.mymovieapp.Movie(element.getString("title"),
                                element.getString("date"),
                                element.getString("photoId"),
                                element.getString("synopsis"),
                                element.getString("ratingRuntime"),
                                element.getString("movieId"),
                                newRating));

                        if (!(movies.contains(tempMovie))) {
                            movies.add(tempMovie);
                        } else {
                            movies.get(movies.lastIndexOf(tempMovie)).getRating().addRating(element.getDouble("rating"));
                        }
                    }
                    Collections.sort(ratings);
                    Collections.sort(movies);

                } else {
                    e.printStackTrace();
                }
                ((RVMovAdapter) rv.getAdapter()).updateMovies(movies);
                (rv.getAdapter()).notifyDataSetChanged();
            }
        });
    }
}