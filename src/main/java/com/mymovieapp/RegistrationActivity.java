package com.mymovieapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

/**
 * Registration activity for new users without an entry in the application's database.
 *
 * @version 1.0
 */
public class RegistrationActivity extends BackToolbarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Sign Up");
        }
        final Button registerButton = (Button) findViewById(R.id.btn_register);
        final Button cancelButton = (Button) findViewById(R.id.btn_cancel);
        final RelativeLayout regRelativeLayout = (RelativeLayout) findViewById(R.id.rl_register);

        regRelativeLayout.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        //TextView title = (TextView) findViewById(R.id.tv_register_title);
        //title.setText("Welcome to Registration");
        final EditText username = (EditText) findViewById(R.id.et_username);
        final EditText password = (EditText) findViewById(R.id.et_password);
        final EditText confirm_pass = (EditText) findViewById(R.id.et_confirm_pass);
        final EditText name = (EditText) findViewById(R.id.et_name);
        final EditText email = (EditText) findViewById(R.id.et_email);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String usernametxt = username.getText().toString();
                final String passwordtxt = password.getText().toString();
                final String confirm_passtxt = confirm_pass.getText().toString();
                final String nametxt = name.getText().toString();
                final String emailtxt = email.getText().toString();

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
                    final ParseUser user = new ParseUser();
                    user.setUsername(usernametxt);
                    user.setPassword(passwordtxt);
                    user.setEmail(emailtxt);
                    user.put("name", nametxt);

                    final ParseQuery<ParseUser> userquery = ParseUser.getQuery();
                    userquery.whereEqualTo("username", usernametxt);
                    final ParseQuery<ParseUser> mailquery = ParseUser.getQuery();
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
                                        final Intent it = new Intent(RegistrationActivity.this, ShowProfileDrawerActivity.class);
                                        it.putExtra("title", "Set up Profile");
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
                        Log.d("e", String.valueOf(e));
                    }
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent it = new Intent(RegistrationActivity.this, LoginActivity.class);
                startActivity(it);
            }
        });
    }

    /**
     * View
     * @param view view
     */
    private void hideKeyboard(View view) {
        final InputMethodManager inputMethodManager =
                (InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
