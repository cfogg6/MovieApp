package com.mymovieapp;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * Admin User screen where users can be sorted by All, Active, Locked, or Banned.
 */
public class AdminActivity extends AdminToolbarDrawerActivity {
    private RecyclerView rv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            myToolbar.setElevation(0);
        }
        //Initialize Tab Layout

        final TabLayout tabLayout = (TabLayout) findViewById(R.id.admin_tabs);
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_person_white_24dp));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_check_white_24dp));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_lock_white_24dp));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_not_interested_white_24dp));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tabLayout.getSelectedTabPosition()) {
                    //All Users
                    case 0:
                        ((RVUserAdapter)rv.getAdapter()).mode = "ALL";
                        ((RVUserAdapter)rv.getAdapter()).updateLists();
                        rv.getAdapter().notifyDataSetChanged();
                        return;
                    //Active Users
                    case 1:
                        ((RVUserAdapter)rv.getAdapter()).mode = "UNLOCKED";
                        ((RVUserAdapter)rv.getAdapter()).updateLists();
                        rv.getAdapter().notifyDataSetChanged();
                        return;
                    //All Locked Users
                    case 2:
                        ((RVUserAdapter)rv.getAdapter()).mode = "LOCKED";
                        ((RVUserAdapter)rv.getAdapter()).updateLists();
                        rv.getAdapter().notifyDataSetChanged();
                        return;
                    //All Banned Users
                    case 3:
                        ((RVUserAdapter)rv.getAdapter()).mode = "BANNED";
                        ((RVUserAdapter)rv.getAdapter()).updateLists();
                        rv.getAdapter().notifyDataSetChanged();
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        rv = (RecyclerView) findViewById(R.id.users_rv);
        rv.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        final RVUserAdapter adapter = new RVUserAdapter(this, new ArrayList<AdminUser>());
        rv.setAdapter(adapter);
        ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null) {
                    for (ParseObject element : list) {
                        adapter.users.add(new AdminUser(element.getString("username")));
                    }
                } else {
                    e.printStackTrace();
                }
                adapter.updateLists();
                adapter.notifyDataSetChanged();
            }
        });
        ParseQuery<ParseObject> lockedQuery = ParseQuery.getQuery("Locked");
        lockedQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null) {
                    for (ParseObject element : list) {
                        int index = adapter.users.lastIndexOf(new AdminUser(element.getString("username")));
                        if (element.getInt("strikes") >= 3 && index >= 0) {
                            adapter.users.get(index).setLocked(true);
                        }
                    }
                } else {
                    e.printStackTrace();
                }
                adapter.updateLists();
                adapter.notifyDataSetChanged();
            }
        });
        ParseQuery<ParseObject> bannedQuery = ParseQuery.getQuery("Banned");
        bannedQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null) {
                    for (ParseObject element : list) {
                        int index = adapter.users.lastIndexOf(new AdminUser(element.getString("username")));
                        if (index >= 0) {
                            adapter.users.get(index).setBanned(true);
                        }
                    }
                } else {
                    e.printStackTrace();
                }
                adapter.updateLists();
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        final RVUserAdapter adapter = new RVUserAdapter(this, new ArrayList<AdminUser>());
        rv.setAdapter(adapter);
        ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null) {
                    for (ParseObject element : list) {
                        adapter.users.add(new AdminUser(element.getString("username")));
                    }
                } else {
                    e.printStackTrace();
                }
                adapter.updateLists();
                adapter.notifyDataSetChanged();
            }
        });
        ParseQuery<ParseObject> lockedQuery = ParseQuery.getQuery("Locked");
        lockedQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null) {
                    for (ParseObject element : list) {
                        int index = adapter.users.lastIndexOf(new AdminUser(element.getString("username")));
                        if (element.getInt("strikes") >= 3 && index >= 0) {
                            adapter.users.get(index).setLocked(true);
                        }
                    }
                } else {
                    e.printStackTrace();
                }
                adapter.updateLists();
                adapter.notifyDataSetChanged();
            }
        });
        ParseQuery<ParseObject> bannedQuery = ParseQuery.getQuery("Banned");
        bannedQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null) {
                    for (ParseObject element : list) {
                        int index = adapter.users.lastIndexOf(new AdminUser(element.getString("username")));
                        if (index >= 0) {
                            adapter.users.get(index).setBanned(true);
                        }
                    }
                } else {
                    e.printStackTrace();
                }
                adapter.updateLists();
                adapter.notifyDataSetChanged();
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
