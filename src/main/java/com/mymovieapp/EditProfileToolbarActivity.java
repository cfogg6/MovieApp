package com.mymovieapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Edit profile activity that takes input and saves to database. Uses the back toolbar.
 */
public class EditProfileToolbarActivity extends BackToolbarActivity{

    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        final ParseUser user = ParseUser.getCurrentUser();
        TextView usernameView = (TextView) findViewById(R.id.tV_username);
        usernameView.setText(user.getUsername());

        LinearLayout spinView = (LinearLayout) findViewById(R.id.spinner_container);
        View spinnerContainer = LayoutInflater.from(this)
                .inflate(R.layout.toolbar_spinner, spinView, false);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        spinView.addView(spinnerContainer, lp);
        MajorSpinnerAdapter spinnerAdapter = new MajorSpinnerAdapter();
        spinnerAdapter.addItems(
                Arrays.asList(getResources().getStringArray(R.array.majors_array)));
        spinner = (Spinner) spinnerContainer.findViewById(R.id.toolbar_spinner);
        spinner.setAdapter(spinnerAdapter);

        //Display field titles
        final EditText editName = (EditText) findViewById(R.id.et_name);
        final EditText editEmail = (EditText) findViewById(R.id.et_email);
        //final EditText editMajor = (EditText) findViewById(R.id.et_major);
        final EditText editInterests = (EditText) findViewById(R.id.et_interests);
        Button editDoneButton = (Button) findViewById(R.id.btn_editDone);

        //Populate field values
        editName.setText((String) user.get("name"));
        editEmail.setText(user.getEmail());
        spinner.setSelection(spinnerAdapter.getPosition((String) user.get("major")));
        //editMajor.setText((String) user.get("major"));
        editInterests.setText((String) user.get("interests"));

        editDoneButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                user.put("name", editName.getText().toString());
                user.put("major", spinner.getSelectedItem().toString());
                user.put("interests", editInterests.getText().toString());
                user.setEmail(editEmail.getText().toString());

                user.saveInBackground();
                Intent it = new Intent(EditProfileToolbarActivity.this,
                        ShowProfileDrawerActivity.class);
                startActivity(it);
            }
        });
    }

    private class MajorSpinnerAdapter extends BaseAdapter {
        private List<String> majors = new ArrayList<>();

       /* public void clear() {
            majors.clear();
        }*/

        /*public void addItem(String yourObject) {
            majors.add(yourObject);
        }*/

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
                        toolbar_spinner_item_dark, parent, false);
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
