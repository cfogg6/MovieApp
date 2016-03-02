package com.mymovieapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseUser;

public class ShowProfileActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_profile_drawer);

        //Initialize Toolbar as ActionBat
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        if (myToolbar != null) {
            setSupportActionBar(myToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            myToolbar.setNavigationIcon(R.drawable.ic_dehaze_24dp);
        }

        //Initialize DrawerLayout
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        //Initialize Navigation Drawer
        NavigationView navView = (NavigationView) findViewById(R.id.navigation_view);

        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {

                    // This method will trigger on item Click of navigation menu
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {

                        Toast.makeText(ShowProfileActivity.this, menuItem.getTitle() + " pressed", Toast.LENGTH_SHORT).show();

                        //Checking if the item is in checked state or not, if not make it in checked state
                        if (menuItem.isChecked()) menuItem.setChecked(false);
                        else menuItem.setChecked(true);

                        //Closing drawer on item click
                        drawerLayout.closeDrawers();

                        //Check to see which item was being clicked and perform appropriate action
                        Intent it;
                        switch (menuItem.getItemId()) {
                            case R.id.profile:
                                return true;

                            case R.id.search:
                                it = new Intent(ShowProfileActivity.this, SearchActivity.class);
                                startActivity(it);
                                return true;

                            case R.id.new_movies:
                                it = new Intent(ShowProfileActivity.this, InTheatersActivity.class);
                                startActivity(it);
                                return true;

                            case R.id.new_dvds:
                                it = new Intent(ShowProfileActivity.this, NewDvdActivity.class);
                                startActivity(it);
                                return true;
                        }
                        return true;
                    }
                });


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
        if (id == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
