package com.mymovieapp;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MajorSpinnerAdapter extends BaseAdapter{

    /**
     * list of majors for the Major Spinner
     */
    private List<String> majors = new ArrayList<>();
    /**
     * The activity housing the spinner
     */
    private Activity activity;
    /**
     * Determines dark spinner theme
     */
    private boolean isDark = false;

    /**
     * Constructor for Major Spinner adapter
     *
     * @param act activity that the spinner is in
     */
    public MajorSpinnerAdapter(Activity act) {
        this.activity = act;
    }

    /**
     * Constructor for setting spinner theme
     * @param act activity
     * @param isDk sets is dark
     */
    public MajorSpinnerAdapter(Activity act, Boolean isDk) {
        this.activity = act;
        this.isDark = isDk;
    }

    /**
     * Returns position of the input major
     *
     * @param major the major one wants a position of
     * @return the position of the major in the spinner
     */
    public int getPosition(String major) {
        return majors.indexOf(major);
    }

    /**
     * Add items to the spinner
     * @param yourObjectList Spinner components
     */
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
        if (view == null || !(("DROPDOWN").equals(view.getTag().toString()))) {
            view = activity.getLayoutInflater().inflate(R.layout.toolbar_spinner_item_dropdown, parent, false);
            view.setTag("DROPDOWN");
        }

        final TextView textView = (TextView) view.findViewById(android.R.id.text1);
        textView.setText(getTitle(position));

        return view;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (isDark) {
            if (view == null || !!("NON_DROPDOWN").equals(view.getTag().toString())) {
                view = activity.getLayoutInflater().inflate(R.layout.
                        toolbar_spinner_item_dark, parent, false);
                view.setTag("NON_DROPDOWN");
            }
        } else {
            if (view == null || !!("NON_DROPDOWN").equals(view.getTag().toString())) {
                view = activity.getLayoutInflater().inflate(R.layout.
                        toolbar_spinner_item, parent, false);
                view.setTag("NON_DROPDOWN");
            }
        }
        final TextView textView = (TextView) view.findViewById(android.R.id.text1);
        textView.setText(getTitle(position));
        return view;
    }

    /**
     * Returns the title of the major
     *
     * @param position position of the major
     * @return Title of the major
     */
    private String getTitle(int position) {
        return position >= 0 && position < majors.size() ? majors.get(position) : "";
    }
}
