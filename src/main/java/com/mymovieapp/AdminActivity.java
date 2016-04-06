package com.mymovieapp;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;

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
    /**
     * Recycler View for Admin
     */
    private RecyclerView rv;

    /**
     * Username String literal
     */
    private final String username = "username";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("User Management");
        }

        final Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
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
                        ((RVUserAdapter) rv.getAdapter()).setMode("ALL");
                        ((RVUserAdapter) rv.getAdapter()).updateLists();
                        rv.getAdapter().notifyDataSetChanged();
                        break;
                    //Active Users
                    case 1:
                        ((RVUserAdapter) rv.getAdapter()).setMode("UNLOCKED");
                        ((RVUserAdapter) rv.getAdapter()).updateLists();
                        rv.getAdapter().notifyDataSetChanged();
                        break;
                    //All Locked Users
                    case 2:
                        ((RVUserAdapter) rv.getAdapter()).setMode("LOCKED");
                        ((RVUserAdapter) rv.getAdapter()).updateLists();
                        rv.getAdapter().notifyDataSetChanged();
                        break;
                    //All Banned Users
                    case 3:
                        ((RVUserAdapter) rv.getAdapter()).setMode("BANNED");
                        ((RVUserAdapter) rv.getAdapter()).updateLists();
                        rv.getAdapter().notifyDataSetChanged();
                        break;
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
        final LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        final RVUserAdapter adapter = new RVUserAdapter(this, new ArrayList<AdminUser>());
        rv.setAdapter(adapter);
        final ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                RVUserAdapter adapter = (RVUserAdapter)rv.getAdapter();
                if (e == null) {
                    for (ParseObject element : list) {
                        if (!adapter.getUsers().contains(new AdminUser(element.getString(username)))) {
                            adapter.getUsers().add(new AdminUser(element.getString(username)));
                        }
                    }
                }
                adapter.updateLists();
                adapter.notifyDataSetChanged();
            }
        });
        final ParseQuery<ParseObject> lockedQuery = ParseQuery.getQuery("Locked");
        lockedQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null) {
                    for (ParseObject element : list) {
                        final int index = adapter.getUsers().lastIndexOf(new AdminUser(element.getString(username)));
                        if (element.getInt("strikes") >= 3 && index >= 0) {
                            adapter.getUsers().get(index).setUserIsLocked(true);
                        }
                        adapter.updateLists();
                        adapter.notifyDataSetChanged();
                    }
                    final ParseQuery<ParseObject> bannedQuery = ParseQuery.getQuery("Banned");
                    bannedQuery.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> list, ParseException e) {
                            RVUserAdapter adapter = (RVUserAdapter) rv.getAdapter();
                            if (e == null) {
                                for (ParseObject element : list) {
                                    final int index = adapter.getUsers().lastIndexOf(new AdminUser(element.getString(username)));
                                    if (index >= 0) {
                                        adapter.getUsers().get(index).setBanned(true);
                                    }
                                }
                            }
                            adapter.updateLists();
                            adapter.notifyDataSetChanged();
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        final RVUserAdapter adapter = new RVUserAdapter(this, new ArrayList<AdminUser>());
        rv.setAdapter(adapter);
        final ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                RVUserAdapter adapter = (RVUserAdapter)rv.getAdapter();
                if (e == null) {
                    for (ParseObject element : list) {
                        if (!adapter.getUsers().contains(new AdminUser(element.getString(username)))) {
                            adapter.getUsers().add(new AdminUser(element.getString(username)));
                        }
                    }
                }
                final ParseQuery<ParseObject> lockedQuery = ParseQuery.getQuery("Locked");
                lockedQuery.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> list, ParseException e) {
                        RVUserAdapter adapter = (RVUserAdapter)rv.getAdapter();
                        if (e == null) {
                            for (ParseObject element : list) {
                                final int index = adapter.getUsers().lastIndexOf(new AdminUser(element.getString(username)));
                                Log.d("user", adapter.getUsers().get(index).getName());
                                if (element.getInt("strikes") >= 3 && index >= 0) {
                                    adapter.getUsers().get(index).setUserIsLocked(true);
                                }
                            }
                        }
                        adapter.updateLists();
                        adapter.notifyDataSetChanged();
                    }
                });
                adapter.updateLists();
                adapter.notifyDataSetChanged();
            }
        });
        final ParseQuery<ParseObject> bannedQuery = ParseQuery.getQuery("Banned");
        bannedQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null) {
                    for (ParseObject element : list) {
                        final int index = adapter.getUsers().lastIndexOf(new AdminUser(element.getString(username)));
                        if (index >= 0) {
                            adapter.getUsers().get(index).setBanned(true);
                        }
                        adapter.updateLists();
                        adapter.notifyDataSetChanged();
                    }
                }
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
        if (!(getIntent().hasExtra("Login"))) {
            super.onBackPressed();
        }
    }
}
