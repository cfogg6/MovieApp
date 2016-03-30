package com.mymovieapp;

import android.graphics.Movie;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by Corey on 3/6/16.
 */
public class RecommendationsActivity extends ToolbarDrawerActivity {
    Spinner spinner;
    ArrayList<Rating> ratings = new ArrayList<>();
    ListView listView;

    JSONArray listOfMovies;

    private List<com.mymovieapp.Movie> movies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommendations);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setElevation(0);

        Toolbar spinBar = (Toolbar) findViewById(R.id.spinner_toolbar);

        listView = (ListView) findViewById(R.id.lv_ratings);
        listView.setAdapter(new RatingsListAdapter(this, 0));

        View spinnerContainer = LayoutInflater.from(this)
                .inflate(R.layout.toolbar_spinner,spinBar, false);
        Toolbar.LayoutParams lp = new Toolbar.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        spinBar.addView(spinnerContainer, lp);


        /*// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.toolbar_spinner_item,
                getResources().getStringArray(R.array.majors_array));
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.toolbar_spinner_item_dropdown);*/


        MajorSpinnerAdapter spinnerAdapter = new MajorSpinnerAdapter();
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
        RecyclerView rv = (RecyclerView) findViewById(R.id.mov_rv);
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

    private class MajorSpinnerAdapter extends BaseAdapter {
        private List<String> majors = new ArrayList<>();

        public void clear() {
            majors.clear();
        }

        public void addItem(String yourObject) {
            majors.add(yourObject);
        }

        public void addItems(List<String> yourObjectList) {
            majors.addAll(yourObjectList);
        }

        @Override
        public int getCount() {
            return majors.size();
        }

        @Override
        public Object getItem(int position) {
            return majors.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getDropDownView(int position, View view, ViewGroup parent) {
            if (view == null || !view.getTag().toString().equals("DROPDOWN")) {
                view = getLayoutInflater().inflate(R.layout.toolbar_spinner_item_dropdown, parent, false);
                view.setTag("DROPDOWN");
            }

            TextView textView = (TextView) view.findViewById(android.R.id.text1);
            textView.setText(getTitle(position));

            return view;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            if (view == null || !view.getTag().toString().equals("NON_DROPDOWN")) {
                view = getLayoutInflater().inflate(R.layout.
                        toolbar_spinner_item, parent, false);
                view.setTag("NON_DROPDOWN");
            }
            TextView textView = (TextView) view.findViewById(android.R.id.text1);
            textView.setText(getTitle(position));
            return view;
        }

        private String getTitle(int position) {
            return position >= 0 && position < majors.size() ? majors.get(position) : "";
        }
    }
}