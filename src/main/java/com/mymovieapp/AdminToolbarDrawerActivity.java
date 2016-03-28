package com.mymovieapp;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseUser;

public class AdminToolbarDrawerActivity extends AppCompatActivity{
    private DrawerLayout mDrawerLayout;
    protected ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected  void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return mDrawerToggle.onOptionsItemSelected(item)||
                super.onOptionsItemSelected(item);
    }

    @Override
    public void setContentView(final int layoutResID) {
        DrawerLayout fullLayout = (DrawerLayout) getLayoutInflater()
                .inflate(R.layout.activity_admin_drawer, null);
        RelativeLayout actContent = (RelativeLayout) fullLayout.findViewById(R.id.content);

        getLayoutInflater().inflate(layoutResID, actContent, true);
        super.setContentView(fullLayout);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        mDrawerLayout = (DrawerLayout) fullLayout.findViewById(R.id.admin_drawer_layout);
        NavigationView navView = (NavigationView) fullLayout.findViewById(R.id.navigation_view);

        View header = navView.getHeaderView(0);
        TextView usernameDraw = (TextView) header.findViewById(R.id.tV_username_header);
        TextView emailDraw = (TextView) header.findViewById(R.id.tV_email_header);
        usernameDraw.setText(ParseUser.getCurrentUser().getUsername());
        emailDraw.setText(ParseUser.getCurrentUser().getEmail());

        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {

                    // This method will trigger on item Click of navigation menu
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {

                        Toast.makeText(AdminToolbarDrawerActivity.this, menuItem.getTitle() + " pressed", Toast.LENGTH_SHORT).show();

                        //Checking if the item is in checked state or not, if not make it in checked state
                        if (menuItem.isChecked()) menuItem.setChecked(false);
                        else menuItem.setChecked(true);

                        //Closing drawer on item click
                        mDrawerLayout.closeDrawers();

                        //Check to see which item was being clicked and perform appropriate action
                        Intent it;
                        switch (menuItem.getItemId()) {
                            case R.id.users:
                                it = new Intent(AdminToolbarDrawerActivity.this, AdminActivity.class);
                                startActivity(it);
                                return true;

                            case R.id.flag_com:
                                it = new Intent(AdminToolbarDrawerActivity.this, FlaggedCommentsActivity.class);
                                startActivity(it);
                                return true;

                            case R.id.signout:
                                ParseUser.logOut();
                                it = new Intent(AdminToolbarDrawerActivity.this, LoginActivity.class);
                                startActivity(it);
                                return true;
                        }
                        return true;
                    }
                });

        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                myToolbar,
                R.string.drawer_open,
                R.string.drawer_close
        ) {

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };

        mDrawerLayout.addDrawerListener(mDrawerToggle);

        //Initialize Toolbar as ActionBar
        if (myToolbar != null) {
            setSupportActionBar(myToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
    }
}
