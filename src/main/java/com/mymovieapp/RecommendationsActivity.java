package com.mymovieapp;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Corey on 3/6/16.
 */
public class RecommendationsActivity extends ToolbarDrawerActivity {
    Spinner spinner;
    ArrayList<Rating> ratings = new ArrayList<>();
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommendations);
        spinner = (Spinner) findViewById(R.id.majors_spinner);
        listView = (ListView) findViewById(R.id.lv_ratings);
        listView.setAdapter(new RatingsListAdapter(this, 0));
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.majors_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener((new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        }));
    }

    @Override
    public void onResume() {
        super.onResume();
        updateList();
    }

    private void updateList() {
        ratings = new ArrayList<>();
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
                    }
                    Collections.sort(ratings);
                } else {
                    e.printStackTrace();
                }
                ((RatingsListAdapter) listView.getAdapter()).updateRatings(ratings);
                ((RatingsListAdapter) listView.getAdapter()).notifyDataSetChanged();
            }
        });
    }
}