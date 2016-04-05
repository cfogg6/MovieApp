package com.mymovieapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.Arrays;
import java.util.List;

/**
 * Edit profile activity that takes input and saves to database. Uses the back toolbar.
 */
public class EditProfileToolbarActivity extends BackToolbarActivity{

    /**
     * Spinner with major choices
     */
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        String title = getIntent().getStringExtra("title");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }


        final ParseUser user = ParseUser.getCurrentUser();
        final TextView usernameView = (TextView) findViewById(R.id.tV_username);
        usernameView.setText(user.getUsername());

        final LinearLayout spinView = (LinearLayout) findViewById(R.id.spinner_container);
        final View spinnerContainer = LayoutInflater.from(this)
                .inflate(R.layout.toolbar_spinner, spinView, false);
        final LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        spinView.addView(spinnerContainer, lp);
        final MajorSpinnerAdapter spinnerAdapter = new MajorSpinnerAdapter(this, true);
        spinnerAdapter.addItems(
                Arrays.asList(getResources().getStringArray(R.array.majors_array)));
        spinner = (Spinner) spinnerContainer.findViewById(R.id.toolbar_spinner);
        spinner.setAdapter(spinnerAdapter);

        //Display field titles
        final EditText editName = (EditText) findViewById(R.id.et_name);
        final EditText editEmail = (EditText) findViewById(R.id.et_email);
        final EditText editInterests = (EditText) findViewById(R.id.et_interests);
        final Button editDoneButton = (Button) findViewById(R.id.btn_editDone);

        //Populate field values
        editName.setText((String) user.get("name"));
        editEmail.setText(user.getEmail());
        spinner.setSelection(spinnerAdapter.getPosition((String) user.get("major")));
        editInterests.setText((String) user.get("interests"));

        editDoneButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                user.put("name", editName.getText().toString());
                user.put("major", spinner.getSelectedItem().toString());
                user.put("interests", editInterests.getText().toString());
                user.setEmail(editEmail.getText().toString());

                user.saveInBackground();

                //We also have to update the Ratings table
                final ParseQuery<ParseObject> query = ParseQuery.getQuery("Ratings");
                query.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());
                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> list, ParseException e) {
                        if (e == null) {
                            for (ParseObject o: list) {
                                o.put("major", spinner.getSelectedItem().toString());
                                o.saveInBackground();
                            }
                        }
                    }
                });
                final Intent it = new Intent(EditProfileToolbarActivity.this,
                        ShowProfileDrawerActivity.class);
                startActivity(it);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (getIntent().hasExtra("Register")) {
            ParseUser.getCurrentUser()
                    .put("major", spinner.getSelectedItem().toString());
        }
        final Intent it = new Intent(EditProfileToolbarActivity.this,
                ShowProfileDrawerActivity.class);
        startActivity(it);
    }
}
