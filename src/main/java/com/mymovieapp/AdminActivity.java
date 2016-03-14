package com.mymovieapp;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Corey on 3/13/16.
 */
public class AdminActivity extends Activity {
    ArrayList<AdminUser> users = new ArrayList<>();
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        listView = (ListView) findViewById(R.id.lv_user_list);
        listView.setAdapter(new UserListAdapter(this, 0));
        ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null) {
                    for (ParseObject element : list) {
                        users.add(new AdminUser(element.getString("username")));
                    }
                } else {
                    e.printStackTrace();
                }
                ((UserListAdapter) listView.getAdapter()).updateUsers(users);
                ((UserListAdapter) listView.getAdapter()).notifyDataSetChanged();
            }
        });
        ParseQuery<ParseObject> lockedQuery = ParseQuery.getQuery("Locked");
        lockedQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null) {
                    for (ParseObject element : list) {
                        for (AdminUser user: users) {
                            Log.d("users", user.getName());
                        }
                        int index = users.lastIndexOf(new AdminUser(element.getString("username")));
                        if (element.getInt("strikes") >= 3) {
                            users.get(index).setLocked(true);
                        }
                    }
                } else {
                    e.printStackTrace();
                }
                ((UserListAdapter) listView.getAdapter()).updateUsers(users);
                ((UserListAdapter) listView.getAdapter()).notifyDataSetChanged();
            }
        });
        ParseQuery<ParseObject> bannedQuery = ParseQuery.getQuery("Banned");
        bannedQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null) {
                    for (ParseObject element : list) {
                        int index = users.lastIndexOf(new AdminUser(element.getString("username")));
                        users.get(index).setBanned(true);
                    }
                } else {
                    e.printStackTrace();
                }
                ((UserListAdapter) listView.getAdapter()).updateUsers(users);
                ((UserListAdapter) listView.getAdapter()).notifyDataSetChanged();
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
            Toast.makeText(AdminActivity.this, "No Settings", Toast.LENGTH_SHORT).show();
        }
        if (id == R.id.action_logout) {
            Toast.makeText(AdminActivity.this, "Logout Failed", Toast.LENGTH_SHORT).show();
        }
        if (id == R.id.action_editProfile) {
            Toast.makeText(AdminActivity.this, "In Edit", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }
}
