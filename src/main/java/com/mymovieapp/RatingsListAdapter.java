package com.mymovieapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Honey on 3/8/2016.
 */
public class RatingsListAdapter extends ArrayAdapter {
    int count = 0;
    LayoutInflater inflater;
    Context context;
    ArrayList<Rating> ratings = new ArrayList<>();

    public RatingsListAdapter(Activity parentActivity, int textViewResourceId) {
        super(parentActivity, textViewResourceId);
        context = parentActivity;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void updateRatings(ArrayList<Rating> list) {
        ratings = list;

    }

    public int getCount() {
        return ratings.size();
    }

    public View getView(final int position,View convertView,ViewGroup parent) {
        final ViewHolder viewHolder;
        if(convertView == null) {
            convertView = inflater.inflate(R.layout.listrow_ratings, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.ratingsTextView = (TextView) convertView.findViewById(R.id.tv_ratings);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)convertView.getTag();
        }
        viewHolder.ratingsTextView.setText(ratings.get(position).name + " " + ratings.get(position).getAverageRating());
        return convertView;
    }

    private static class ViewHolder {
        TextView ratingsTextView;
    }
}
