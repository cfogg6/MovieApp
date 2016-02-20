package com.mymovieapp;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Corey on 2/20/16.
 */
public class SearchListAdapter extends ArrayAdapter {
    int count = 0;
    LayoutInflater inflater;
    Context context;
    JSONArray movies;

    public SearchListAdapter(Activity parentActivity, int textViewResourceId) {
        super(parentActivity, textViewResourceId);
        context = parentActivity;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void updateJSON(JSONObject json) {
        try {
            movies = json.getJSONArray("movies");
            count = Math.min(movies.length(), 20);
            Log.d("count", String.valueOf(count));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int getCount() {
        return count;
    }

    public View getView(final int position,View convertView,ViewGroup parent) {
        final ViewHolder viewHolder;
        if(convertView == null) {
            convertView = inflater.inflate(R.layout.listrow_search, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.movieTextView = (TextView) convertView.findViewById(R.id.tv_movie_title);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)convertView.getTag();
        }
        try {
            viewHolder.movieTextView.setText(movies.getJSONObject(position).getString("title"));
        } catch (JSONException e) {
            viewHolder.movieTextView.setText("");
        }
        return convertView;
    }

    private static class ViewHolder {
        TextView movieTextView;
    }
}
