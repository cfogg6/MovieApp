package com.mymovieapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

/**
 * Created by Corey on 2/2/16. Modified by Angelo.
 *
 * @version 1.0
 */
public class RegistrationActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        Button registerButton = (Button) findViewById(R.id.btn_register);
        Button cancelButton = (Button) findViewById(R.id.btn_cancel);

        TextView title = (TextView) findViewById(R.id.tv_register_title);
        title.setText("Welcome to Registration");
        final EditText username = (EditText) findViewById(R.id.et_username);
        final EditText password = (EditText) findViewById(R.id.et_password);
        final EditText confirm_pass = (EditText) findViewById(R.id.et_confirm_pass);
        final EditText name = (EditText) findViewById(R.id.et_name);
        final EditText email = (EditText) findViewById(R.id.et_email);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usernametxt = username.getText().toString();
                String passwordtxt = password.getText().toString();
                String confirm_passtxt = confirm_pass.getText().toString();
                String nametxt = name.getText().toString();
                String emailtxt = email.getText().toString();

                if (usernametxt.equals("")||passwordtxt.equals("")||emailtxt.equals("")||
                        confirm_passtxt.equals("")||nametxt.equals("")) {
                    Toast.makeText(RegistrationActivity.this,
                            "Please Complete the Registration Form",
                            Toast.LENGTH_LONG).show();
                } else if (!passwordtxt.equals(confirm_passtxt)){
                    Toast.makeText(RegistrationActivity.this,
                            "Passwords do not Match",
                            Toast.LENGTH_LONG).show();
                } else {
                    ParseUser user = new ParseUser();
                    user.setUsername(usernametxt);
                    user.setPassword(passwordtxt);
                    user.setEmail(emailtxt);
                    user.put("name", nametxt);

                    ParseQuery<ParseUser> userquery = ParseUser.getQuery();
                    userquery.whereEqualTo("username", usernametxt);
                    ParseQuery<ParseUser> mailquery = ParseUser.getQuery();
                    mailquery.whereEqualTo("email", emailtxt);
                    try {
                        if (userquery.count() > 0) {
                            Toast.makeText(RegistrationActivity.this,
                                    "Username is Taken",
                                    Toast.LENGTH_LONG).show();
                        } else if (mailquery.count() > 0){
                            Toast.makeText(RegistrationActivity.this,
                                    "Email is already in use",
                                    Toast.LENGTH_LONG).show();
                        } else {
                            user.signUpInBackground(new SignUpCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (e == null) {
                                        Toast.makeText(getApplicationContext(),
                                                "Successfully Registered",
                                                Toast.LENGTH_SHORT).show();
                                        Intent it = new Intent(RegistrationActivity.this, ShowProfileActivity.class);
                                        startActivity(it);
                                    } else {
                                        Toast.makeText(getApplicationContext(),
                                                "Registration Error", Toast.LENGTH_SHORT)
                                                .show();
                                    }
                                }
                            });
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(RegistrationActivity.this, WelcomeActivity.class);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
