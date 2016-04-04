package com.mymovieapp;

import android.os.Bundle;
import android.view.Menu;
import android.widget.ListView;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import java.util.ArrayList;
import java.util.List;

/**
 * Activity in the admin functionality that allows admin to see and delete flagged comments.
 */
public class FlaggedCommentsActivity extends AdminToolbarDrawerActivity {
    /**
     * List of flagged comments
     */
    private ArrayList<FlaggedComment> comments = new ArrayList<>();
    /**
     * ListView to display comments
     */
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flagged_comments);
        listView = (ListView) findViewById(R.id.lv_flagged_comments);
        listView.setAdapter(new FlaggedCommentsAdapter(this, 0));
        final ParseQuery<ParseObject> query = ParseQuery.getQuery("FlaggedComments");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null) {
                    for (ParseObject element : list) {
                        comments.add(new FlaggedComment(element.getString("title"),
                                element.getString("comment"),
                                element.getString("username")));
                    }
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
}
