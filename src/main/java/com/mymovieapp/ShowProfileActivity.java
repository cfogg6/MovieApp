package com.mymovieapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.parse.ParseUser;

public class ShowProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_profile);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        //Display field titles
        ParseUser user = ParseUser.getCurrentUser();
        TextView usernameView = (TextView) findViewById(R.id.tV_username);
        TextView nameView = (TextView) findViewById(R.id.tV_name);
        TextView emailView = (TextView) findViewById(R.id.tV_email);
        TextView majorView = (TextView) findViewById(R.id.tV_major);
        TextView interestsView = (TextView) findViewById(R.id.tV_interests);

        //Populate fields
        usernameView.setText(user.getUsername());
        nameView.setText(nameView.getText() + (String)user.get("name"));
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Toast.makeText(ShowProfileActivity.this, "No Settings", Toast.LENGTH_SHORT).show();
        }
        if (id == R.id.action_logout) {
            ParseUser.logOut();
            Intent it = new Intent(ShowProfileActivity.this, WelcomeActivity.class);
            startActivity(it);
        }
        if (id == R.id.action_editProfile) {
            Intent it = new Intent(ShowProfileActivity.this, EditProfileActivity.class);
            startActivity(it);
        }
        if (id == R.id.action_search) {
            Intent it = new Intent(ShowProfileActivity.this, SearchActivity.class);
            startActivity(it);
        }
        if (id == R.id.action_inTheaters) {
            Intent it = new Intent(ShowProfileActivity.this, InTheatersActivity.class);
            startActivity(it);
        }
        if (id == R.id.action_newDVDs) {
            Intent it = new Intent(ShowProfileActivity.this, NewDvdActivity.class);
            startActivity(it);
        }
        if (id == R.id.action_recommendations) {
            Intent it = new Intent(ShowProfileActivity.this, RecommendationsActivity.class);
            startActivity(it);
        }

        return super.onOptionsItemSelected(item);
    }
}
