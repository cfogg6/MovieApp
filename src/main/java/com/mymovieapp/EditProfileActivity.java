package com.mymovieapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Edit Profile Activity to edit the fields and update database
 *
 */
public class EditProfileActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        TextView usernameView = (TextView) findViewById(R.id.tV_username);
        usernameView.setText(User.getUsername());

        EditText editName = (EditText) findViewById(R.id.et_name);
        EditText editEmail = (EditText) findViewById(R.id.et_email);
        EditText editMajor = (EditText) findViewById(R.id.et_major);
        EditText editInterests = (EditText) findViewById(R.id.et_interests);

        editName.setText(User.getName());
        editEmail.setText(User.getEmail());
        editMajor.setText(User.getMajor());
        editInterests.setText(User.getInterests());

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
            Toast.makeText(EditProfileActivity.this, "No Settings", Toast.LENGTH_SHORT).show();
        }
        if (id == R.id.action_logout) {
            Toast.makeText(EditProfileActivity.this, "Logout Failed", Toast.LENGTH_SHORT).show();
        }
        if (id == R.id.action_editProfile) {
            Toast.makeText(EditProfileActivity.this, "In Edit", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }
}
