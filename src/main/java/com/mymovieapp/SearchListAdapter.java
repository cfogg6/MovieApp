package com.mymovieapp;

import android.app.Activity;
import android.util.Log;
import android.widget.ArrayAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Corey on 2/20/16.
 */
public class SearchListAdapter extends ArrayAdapter {
    int count = 0;
    public SearchListAdapter(JSONObject json, Activity parentActivity, int textViewResourceId) {
        super(parentActivity, textViewResourceId);
        try {
            JSONArray movies = json.getJSONArray("movies");
            count = json.getInt("total");
            Log.d("count", String.valueOf(count));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int getCount() {
        return count;
    }
}
