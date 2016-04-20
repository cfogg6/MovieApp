package com.mymovieapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

/**
 * Login screen that authenticates with Parse database. Determines user or admin.
 */
public class LoginActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        final Button loginButton = (Button) findViewById(R.id.btn_login);
        final RelativeLayout loginRelativeLayout = (RelativeLayout) findViewById(R.id.rl_login);

        loginRelativeLayout.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        final TextView title = (TextView) findViewById(R.id.tv_login_title);
        title.setText(R.string.welcome);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = ((EditText) findViewById(R.id.et_username)).getText().toString();
                ParseUser.logInInBackground(username,
                        ((EditText) findViewById(R.id.et_password)).getText().toString(),
                        new LogInCallback() {
                            @Override
                            public void done(ParseUser parseUser, ParseException e) {
                                if (parseUser != null) {
                                    final ParseQuery query = ParseQuery.getQuery("Locked");
                                    query.whereEqualTo("username", username);
                                    try {
                                        final ParseObject user = query.getFirst();
                                        ParseObject.createWithoutData("Locked", user.getObjectId()).deleteInBackground();
                                    } catch (ParseException e1) {
                                        Log.d("e1", String.valueOf(e1));
                                    }
                                    final ParseQuery<ParseObject> bannedQuery = ParseQuery.getQuery("Banned");
                                    bannedQuery.whereEqualTo("username", username);
                                    try {
                                        bannedQuery.getFirst();
                                        Toast.makeText(LoginActivity.this, "This user is banned.", Toast.LENGTH_SHORT).show();
                                    } catch (ParseException e1) {
                                        final Intent it = new Intent(LoginActivity.this, HomeActivity.class);
                                        it.putExtra("Login", true);
                                        startActivity(it);
                                    }
                                } else {
                                    ParseQuery query = ParseQuery.getQuery("_User");
                                    query.whereEqualTo("username", username);
                                    try {
                                        query.getFirst();
                                        final ParseQuery lockedQuery = ParseQuery.getQuery("Locked");
                                        lockedQuery.whereEqualTo("username", username);
                                        try {
                                            final ParseObject strikeObject = lockedQuery.getFirst();
                                            final int strikes = strikeObject.getInt("strikes");
                                            if (strikes < 2) {
                                                Log.d("strikes", String.valueOf(strikes + 1));
                                                strikeObject.put("strikes", strikes + 1);
                                                strikeObject.saveInBackground();
                                                Toast.makeText(LoginActivity.this,
                                                        "Incorrect Password. You have "
                                                                + (3 - (strikes + 1)) + " attempts remaining.",
                                                        Toast.LENGTH_SHORT).show();
                                            } else if (strikes == 2) {
                                                Log.d("strikes", String.valueOf(strikes + 1));
                                                strikeObject.put("strikes", strikes + 1);
                                                strikeObject.saveInBackground();
                                                Toast.makeText(LoginActivity.this,
                                                        "Too many incorrect attempts. Account has been locked.",
                                                        Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(LoginActivity.this,
                                                        "No attempts remaining. Contact an Admin " +
                                                                "to unlock your account",
                                                        Toast.LENGTH_SHORT).show();
                                            }
                                        } catch (ParseException e1) {
                                            final ParseObject userStrikeRow = new ParseObject("Locked");
                                            userStrikeRow.put("username", username);
                                            userStrikeRow.put("strikes", 1);
                                            userStrikeRow.saveInBackground();
                                            Toast.makeText(LoginActivity.this,
                                                    "Incorrect Password. You have 2" +
                                                            " attempts remaining.",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (ParseException e1) {
                                        query = ParseQuery.getQuery("Admin");
                                        query.whereEqualTo("username", username);
                                        query.whereEqualTo("password", ((EditText) findViewById(R.id.et_password)).getText().toString());
                                        try {
                                            query.getFirst();
                                            final Intent it = new Intent(LoginActivity.this, AdminActivity.class);
                                            it.putExtra("Login", true);
                                            startActivity(it);
                                        } catch (ParseException e2) {
                                            Toast.makeText(LoginActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            }
                        });
            }
        });

        final Button registerButton = (Button) findViewById(R.id.btn_registration);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent it = new Intent(LoginActivity.this, RegistrationActivity.class);
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
    public void onBackPressed() {
        final Intent it = new Intent(Intent.ACTION_MAIN);
        it.addCategory(Intent.CATEGORY_HOME);
        it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(it);
    }

    /**
     * Hide the keyboard from view
     * @param view view
     */
    private void hideKeyboard(View view) {
        final InputMethodManager inputMethodManager =
                (InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


}
