package com.mymovieapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.parse.ParseUser;

/**
 * Profile Activity for user using Toolbar and Navigation Drawer from Parent activity.
 */
public class ShowProfileDrawerActivity extends ToolbarDrawerActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_profile);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Profile");
        }

        final ParseUser user = ParseUser.getCurrentUser();

        //Display field titles
        final TextView usernameView = (TextView) findViewById(R.id.tV_username);
        final TextView nameView = (TextView) findViewById(R.id.tV_name);
        final TextView emailView = (TextView) findViewById(R.id.tV_email);
        final TextView majorView = (TextView) findViewById(R.id.tV_major);
        final TextView interestsView = (TextView) findViewById(R.id.tV_interests);

        //Populate fields
        usernameView.setText(user.getUsername());
        nameView.setText(nameView.getText() + (String) user.get("name"));
        emailView.setText(emailView.getText() + user.getEmail());
        if (user.get("major") != null) {
            majorView.setText(majorView.getText() + (String)user.get("major"));
        } else {
            majorView.setText(majorView.getText() + "No Major");
        }
        if (user.get("interests") != null) {
            interestsView.setText(interestsView.getText() + (String)user.get("interests"));
        } else {
            interestsView.setText(interestsView.getText() + "No Interests");
        }

        final Button editProfButton = (Button) findViewById(R.id.btn_editprofile);

        editProfButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent it = new Intent(ShowProfileDrawerActivity.this,
                        EditProfileToolbarActivity.class);
                it.putExtra("title", "Edit Profile");
                startActivity(it);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_login, menu);
        menu.findItem(R.id.menu_search).setVisible(false);
        return true;
    }
}
