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
import android.widget.Toast;

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
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        final ParseUser user = ParseUser.getCurrentUser();
        TextView usernameView = (TextView) findViewById(R.id.tV_username);
        usernameView.setText(user.getUsername());

        //Display field titltes
        final EditText editName = (EditText) findViewById(R.id.et_name);
        final EditText editEmail = (EditText) findViewById(R.id.et_email);
        final EditText editMajor = (EditText) findViewById(R.id.et_major);
        final EditText editInterests = (EditText) findViewById(R.id.et_interests);
        Button editDoneButton = (Button) findViewById(R.id.btn_editDone);

        //Populate field values
        editName.setText((String) user.get("name"));
        editEmail.setText(user.getEmail());
        editMajor.setText((String) user.get("major"));
        editInterests.setText((String) user.get("interests"));

        editDoneButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                user.put("name", editName.getText().toString());
                user.put("major", editMajor.getText().toString());
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
