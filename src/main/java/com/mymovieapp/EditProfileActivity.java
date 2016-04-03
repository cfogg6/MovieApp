package com.mymovieapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.ParseUser;

/**
 * Edit Profile Activity to edit the fields and update database
 *
 */
public class EditProfileActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        //Initialize Toolbar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        if (myToolbar != null) {
            setSupportActionBar(myToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            myToolbar.setNavigationIcon(R.drawable.ic_dehaze_24dp);
        }

        final ParseUser user = ParseUser.getCurrentUser();
        TextView usernameView = (TextView) findViewById(R.id.tV_username);
        usernameView.setText(user.getUsername());

        //Display field titles
        final EditText editName = (EditText) findViewById(R.id.et_name);
        final EditText editEmail = (EditText) findViewById(R.id.et_email);
        //final EditText editMajor = (EditText) findViewById(R.id.et_major);
        final EditText editInterests = (EditText) findViewById(R.id.et_interests);
        Button editDoneButton = (Button) findViewById(R.id.btn_editDone);

        //Populate field values
        editName.setText((String) user.get("name"));
        editEmail.setText(user.getEmail());
        //editMajor.setText((String) user.get("major"));
        editInterests.setText((String) user.get("interests"));

        editDoneButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                user.put("name", editName.getText().toString());
                //user.put("major", editMajor.getText().toString());
                user.put("interests", editInterests.getText().toString());
                user.setEmail(editEmail.getText().toString());

                user.saveInBackground();
                Intent it = new Intent(EditProfileActivity.this, ShowProfileActivity.class);
                startActivity(it);
            }
        });
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


        return super.onOptionsItemSelected(item);
    }
}
