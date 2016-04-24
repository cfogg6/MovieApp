package com.mymovieapp;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

/**
 * Activity that shows movie information on selected movie using json query from RottenTomatoes API
 * Also allows users to comment and rate movies themselves to bbe saved internally for
 * recommendations by major.
 */
public class MovieInfoActivity extends BackToolbarActivity {
    /**
     * Current activity
     */
    private final Activity activity = this;
    /**
     * Current movie to display
     */
    private ParseObject movieInfo;
    /**
     * Rating of movie
     */
    private float rating;
    /**
     * Star bar to display rating of movie
     */
    private RatingBar starBar;
    /**
     * Title of movie
     */
    private String movieName;
    /**
     * Edit text field for user's comments
     */
    private EditText commentEditText;
    /**
     * Comment on the movie
     */
    private String comment;
    /**
     * MovieInfo Activity
     */
    private final MovieInfoActivity thisActivity = this;
    /**
     * Current movie object
     */
    private com.mymovieapp.Movie movieObject;

    /**
     * RecyclerVire to hold comments
     */
    private RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_info);
        final TextView movieTitle = (TextView) findViewById(R.id.tv_movie_title);
        starBar = (RatingBar) findViewById(R.id.rb_star_bar);
        commentEditText = (EditText) findViewById(R.id.et_comment);

        AppCompatImageView movPic = (AppCompatImageView) findViewById(R.id.iV_movPhoto);
        ExpandableTextView synopsis = (ExpandableTextView) findViewById(R.id.tV_synopsis);
        movieObject = (getIntent().getParcelableExtra("SALTY_POPCORN_CURRENT_MOVIE"));
        starBar.setRating(0);
        movieName = movieObject.getName();
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(movieName);
        }
        if ("".equals(movieObject.getSynopsis())) {
            synopsis.setText(R.string.NoSynopsis);
        } else {
            synopsis.setText(movieObject.getSynopsis());
        }
        new DownloadImageTask(movPic).execute(movieObject.getPhotoID());
        movieTitle.setText(movieName);
        Button commentButton = (Button) findViewById(R.id.btn_comment);
        commentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rating == 0) {
                    Toast.makeText(MovieInfoActivity.this, "Please provide a rating", Toast.LENGTH_SHORT).show();
                } else {
                    movieInfo = new ParseObject("Ratings");
                    movieInfo.put("username", ParseUser.getCurrentUser().getUsername());
                    movieInfo.put("major", ParseUser.getCurrentUser().get("major"));
                    movieInfo.put("title", movieTitle.getText());
                    movieInfo.put("rating", rating);
                    movieInfo.put("photoId", movieObject.getPhotoID());
                    movieInfo.put("synopsis", movieObject.getSynopsis());
                    movieInfo.put("ratingRuntime", movieObject.getRatingRuntime());
                    movieInfo.put("date", movieObject.getDate());
                    movieInfo.put("movieId", movieObject.getId());
                    movieInfo.saveInBackground();
                    comment = commentEditText.getText().toString();
                    movieInfo.put("comment", comment);
                    Toast.makeText(v.getContext(), "Rating Submitted!", Toast.LENGTH_SHORT).show();
                    movieInfo.saveInBackground();
                    commentEditText.setText("");
                    starBar.setRating(0);
                    ((RVCommentsAdapter)rv.getAdapter()).addComment(comment,
                            ParseUser.getCurrentUser().getUsername(), rating, new Date());
                }
            }
        });
        starBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float r, boolean fromUser) {
                thisActivity.rating = r;
            }
        });
        final RelativeLayout newDVDLinearLayout = (RelativeLayout) findViewById(R.id.rl_movie_info);
        /**
         * Makes keyboard disappear when you click away from an EditText field
         */
        newDVDLinearLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!(v instanceof EditText)) {
                    final InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
                return true;
            }
        });
        final ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");
        rv = (RecyclerView) findViewById(R.id.comments_rv);
        final RVCommentsAdapter adapter = new RVCommentsAdapter(movieObject.getName());
        rv.setAdapter(adapter);
        final LinearLayoutManager llm = new LinearLayoutManager(activity);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(llm);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null) {
                    final RecyclerView rv = (RecyclerView) findViewById(R.id.comments_rv);
                    final RVCommentsAdapter adapter = new RVCommentsAdapter(movieObject.getName());
                    final LinearLayoutManager llm = new LinearLayoutManager(activity);
                    llm.setOrientation(LinearLayoutManager.VERTICAL);
                    rv.setLayoutManager(llm);
                    rv.setAdapter(adapter);
                    for (ParseObject element : list) {
                        adapter.getUsers().add(new AdminUser(element.getString("username")));
                    }
                    final ParseQuery<ParseObject> query = ParseQuery.getQuery("Ratings");
                    query.whereEqualTo("title", movieObject.getName());
                    query.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> list, ParseException e) {
                            if (e == null) {
                                for (ParseObject element : list) {
                                    adapter.addComment(element.getString("comment"), element.getString("username"),
                                            element.getDouble("rating"), element.getCreatedAt());
                                }
                            }
                            final RecyclerView rv = (RecyclerView) findViewById(R.id.comments_rv);
                            rv.setAdapter(adapter);
                            llm.setOrientation(LinearLayoutManager.VERTICAL);
                            rv.setLayoutManager(llm);
                            (rv.getAdapter()).notifyDataSetChanged();
                        }
                    });
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        /**
         * Image view
         */
        private final ImageView bmImage;

        /**
         * To display image
         * @param i image to display
         */
        public DownloadImageTask(ImageView i) {
            this.bmImage = i;
        }

        /**
         * Display image
         * @param urls url of picture
         * @return image as a bitmap
         */
        protected Bitmap doInBackground(String... urls) {
            final String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                final InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (IOException e) {
                Log.d("e", String.valueOf(e));
            }
            return mIcon11;
        }

        /**
         * Display the image
         * @param result Bitmap input
         */
        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }

    }

}