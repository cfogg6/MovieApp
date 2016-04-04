package com.mymovieapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseUser;

/**
 * Created by Angelo on 3/7/2016.
 */
public class NewMovieActivity extends AppCompatActivity{
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_movie);

        //Initialize Toolbar as ActionBar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
        myToolbar.setNavigationIcon(R.drawable.ic_dehaze_24dp);

        //Initialize DrawerLayout
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        //Initialize Navigation Drawer
        NavigationView navView = (NavigationView) findViewById(R.id.navigation_view);
        View header = navView.getHeaderView(0);
        TextView usernameDraw = (TextView) header.findViewById(R.id.tV_username_header);
        TextView emailDraw = (TextView) header.findViewById(R.id.tV_email_header);
        usernameDraw.setText("John Place");
        emailDraw.setText("Placeholder@gmail.com");

        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {

                    // This method will trigger on item Click of navigation menu
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {

                        Toast.makeText(NewMovieActivity.this, menuItem.getTitle() + " pressed", Toast.LENGTH_SHORT).show();

                        //Checking if the item is in checked state or not, if not make it in checked state
                        if (menuItem.isChecked()) {
                            menuItem.setChecked(false);
                        } else {
                            menuItem.setChecked(true);
                        }

                        //Closing drawer on item click
                        drawerLayout.closeDrawers();

                        //Check to see which item was being clicked and perform appropriate action
                        Intent it;
                        switch (menuItem.getItemId()) {
                            case R.id.profile:
                                it = new Intent(NewMovieActivity.this, EditProfileActivity.class);
                                startActivity(it);
                                return true;

                            case R.id.signout:
                                ParseUser.logOut();
                                it = new Intent(NewMovieActivity.this, LoginActivity.class);
                                startActivity(it);
                                return true;
                        }
                        return true;
                    }
                });

        //Initialize Tab Layout
        TabLayout tabLayout = (TabLayout) findViewById(R.id.movie_tabs);
        tabLayout.addTab(tabLayout.newTab().setText("New DVDs"));
        tabLayout.addTab(tabLayout.newTab().setText("New Releases"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
