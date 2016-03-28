package com.mymovieapp;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class FlaggedCommentsActivity extends AdminToolbarDrawerActivity {
    ArrayList<FlaggedComment> comments = new ArrayList<>();
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flagged_comments);
        listView = (ListView) findViewById(R.id.lv_flagged_comments);
        listView.setAdapter(new FlaggedCommentsAdapter(this, 0));
        ParseQuery<ParseObject> query = ParseQuery.getQuery("FlaggedComments");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null) {
                    for (ParseObject element : list) {
                        comments.add(new FlaggedComment(element.getString("title"),
                                element.getString("comment"),
                                element.getString("username")));
                    }
                } else {
                    e.printStackTrace();
                }
                ((FlaggedCommentsAdapter) listView.getAdapter()).updateComments(comments);
                ((FlaggedCommentsAdapter) listView.getAdapter()).notifyDataSetChanged();
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
