package com.mymovieapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.ParseUser;

/**
 * Edit profile activity that takes input and saves to database. Uses the back toolbar.
 */
public class EditProfileToolbarActivity extends BackToolbarActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        final ParseUser user = ParseUser.getCurrentUser();
        TextView usernameView = (TextView) findViewById(R.id.tV_username);
        usernameView.setText(user.getUsername());

        //Display field titles
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
                Intent it = new Intent(EditProfileToolbarActivity.this,
                        ShowProfileDrawerActivity.class);
                startActivity(it);
            }
        });
    }
}
