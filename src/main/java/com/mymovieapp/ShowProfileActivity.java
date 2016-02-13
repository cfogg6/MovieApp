package com.mymovieapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class ShowProfileActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_profile);

        TextView usernameView = (TextView) findViewById(R.id.tV_username);
        TextView nameView = (TextView) findViewById(R.id.tV_name);
        TextView emailView = (TextView) findViewById(R.id.tV_email);
        TextView majorView = (TextView) findViewById(R.id.tV_major);
        TextView interestsView = (TextView) findViewById(R.id.tV_interests);

        usernameView.setText(User.getUsername());
        nameView.setText(nameView.getText() + User.getName());
        emailView.setText(emailView.getText() + User.getEmail());
        majorView.setText(majorView.getText() + User.getMajor());
        interestsView.setText(interestsView.getText() + User.getInterests());
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
