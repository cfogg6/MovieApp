package com.mymovieapp;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
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
import com.parse.RequestPasswordResetCallback;

/**
 * Login screen that authenticates with Parse database. Determines user or admin.
 */
public class LoginActivity extends Activity {

    private Context context = this;

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
                final String password = ((EditText) findViewById(R.id.et_password)).getText().toString();
                if (username.equals("") && !password.equals("")) {
                    Toast.makeText(LoginActivity.this, "Please enter username", Toast.LENGTH_SHORT).show();
                } else if (!username.equals("") && password.equals("")) {
                    Toast.makeText(LoginActivity.this, "Please enter password", Toast.LENGTH_SHORT).show();
                } else if (username.equals("") && password.equals("")) {
                    Toast.makeText(LoginActivity.this, "Please enter credentials", Toast.LENGTH_SHORT).show();
                } else {
                ParseUser.logInInBackground(username,
                        ((EditText) findViewById(R.id.et_password)).getText().toString(),
                        new LogInCallback() {
                            @Override
                            public void done(ParseUser parseUser, ParseException e) {
                                if (parseUser != null) {
                                    final ParseQuery query = ParseQuery.getQuery("Locked");
                                    query.whereEqualTo("username", username);
                                    final ParseObject user;
                                    ParseObject user1;
                                    try {
                                        user1 = query.getFirst();
                                    } catch (ParseException e1) {
                                        user1 = null;
                                    }
                                    user = user1;
                                    if (user == null || user.getDouble("strikes") < 3) {
                                        if (user != null) {
                                            ParseObject.createWithoutData("Locked", user.getObjectId()).deleteInBackground();
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
                                        Toast.makeText(LoginActivity.this,
                                                "No attempts remaining. Contact an Admin " +
                                                        "to unlock your account",
                                                Toast.LENGTH_SHORT).show();
                                        ParseUser.logOut();
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

        final Button forgotButton = (Button) findViewById(R.id.btn_forgotPassword);
        forgotButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Use the AlertDialog.Builder to configure the AlertDialog.
                AlertDialog.Builder alertDialogBuilder =
                        new AlertDialog.Builder(context)
                                .setTitle("Reset Password")
                                .setMessage("Enter your account's email")
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });

                // Set up the input
                final EditText input = new EditText(context);
                input.setHint("Email Address");
                input.setHeight(25);
                input.setInputType(InputType.TYPE_TEXT_VARIATION_WEB_EMAIL_ADDRESS);
                alertDialogBuilder.setView(input,27,0,27,0);

                alertDialogBuilder.setPositiveButton("Reset", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, int which) {
                        ParseUser.requestPasswordResetInBackground(input.getText().toString(),
                                new RequestPasswordResetCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e == null) {
                                    //Email sent
                                    dialog.dismiss();
                                } else {
                                    Toast.makeText(LoginActivity.this,
                                            "Email Invalid", Toast.LENGTH_SHORT).show();
                                    Log.d("e", String.valueOf(e));
                                }
                            }
                        });

                    }
                });

                alertDialogBuilder.show();
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
