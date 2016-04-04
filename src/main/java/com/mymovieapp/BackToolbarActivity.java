package com.mymovieapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;

/**
 * Parent class for activities on user side. Sets a toolbar with standard back button.
 */
public class BackToolbarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_login, menu);
        menu.findItem(R.id.menu_search).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == android.R.id.home) {
            this.onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setContentView(final int layoutResID) {
        RelativeLayout fullLayout = (RelativeLayout) getLayoutInflater()
                .inflate(R.layout.activity_backtoolbar, null);
        RelativeLayout actContent = (RelativeLayout) fullLayout.findViewById(R.id.content);
        getLayoutInflater().inflate(layoutResID, actContent, true);

        super.setContentView(fullLayout);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        //Initialize Toolbar as ActionBar
        if (myToolbar != null) {
            setSupportActionBar(myToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
    }
}
