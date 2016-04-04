package com.mymovieapp;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MajorSpinnerAdapter extends BaseAdapter{

    private List<String> majors = new ArrayList<>();
    private boolean isDark = false;
    Activity activity;

    public MajorSpinnerAdapter(Activity act) {
        this.activity = act;
    }
    public MajorSpinnerAdapter(Activity act, Boolean isDk) {
        this.activity = act;
        this.isDark = isDk;
    }

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
        if (view == null || !view.getTag().toString().equals("DROPDOWN")) {
            view = activity.getLayoutInflater().inflate(R.layout.toolbar_spinner_item_dropdown, parent, false);
            view.setTag("DROPDOWN");
        }

        TextView textView = (TextView) view.findViewById(android.R.id.text1);
        textView.setText(getTitle(position));

        return view;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (isDark) {
            if (view == null || !view.getTag().toString().equals("NON_DROPDOWN")) {
                view = activity.getLayoutInflater().inflate(R.layout.
                        toolbar_spinner_item_dark, parent, false);
                view.setTag("NON_DROPDOWN");
            }
        } else {
            if (view == null || !view.getTag().toString().equals("NON_DROPDOWN")) {
                view = activity.getLayoutInflater().inflate(R.layout.
                        toolbar_spinner_item, parent, false);
                view.setTag("NON_DROPDOWN");
            }
        }

        TextView textView = (TextView) view.findViewById(android.R.id.text1);
        textView.setText(getTitle(position));
        return view;
    }

    private String getTitle(int position) {
        return position >= 0 && position < majors.size() ? majors.get(position) : "";
    }
}
