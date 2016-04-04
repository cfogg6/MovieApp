package com.mymovieapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
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

import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;

/**
 * Activity that shows movie information on selected movie using json query from RottenTomatoes API
 * Also allows users to comment and rate movies themselves to bbe saved internally for
 * recommendations by major.
 */
public class MovieInfoActivity extends BackToolbarActivity {
    /**
     * Movie Information Parse
     */
    private ParseObject movieInfo;
    /**
     * rating
     */
    private float rating;
    /**
     * The star Bar
     */
    private RatingBar starBar;
    /**
     * Movie Name
     */
    private String movieName;
    /**
     * Comment Edit Text View
     */
    private EditText commentEditText;
    /**
     * Comment String
     */
    private String comment;
    /**
     * this Activity
     */
    private MovieInfoActivity thisActivity = this;
    /**
     * movie object
     */
    private com.mymovieapp.Movie movieObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_info);
        final TextView movieTitle = (TextView) findViewById(R.id.tv_movie_title);
        starBar = (RatingBar) findViewById(R.id.rb_star_bar);
        commentEditText = (EditText) findViewById(R.id.et_comment);
        final AppCompatImageView movPic = (AppCompatImageView) findViewById(R.id.iV_movPhoto);
        final TextView synopsis = (TextView) findViewById(R.id.tV_synopsis);

        movieObject = (getIntent().getParcelableExtra("SALTY_POPCORN_CURRENT_MOVIE"));

        starBar.setRating(0);
        movieName = movieObject.getName();

        if (("").equals(movieObject.getSynopsis())) {
            synopsis.setText("No synopsis.");
        } else {
            synopsis.setText(movieObject.getSynopsis());
        }
        new DownloadImageTask(movPic).execute(movieObject.getPhotoID());
        movieTitle.setText(movieName);
        final Button commentButton = (Button) findViewById(R.id.btn_comment);
        commentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rating == 0) {
                    Toast.makeText(MovieInfoActivity.this, "Please provide a rating", Toast.LENGTH_SHORT).show();
                } else {
                    if (movieInfo == null) {
                        movieInfo = new ParseObject("Ratings");
                        movieInfo.put("username", ParseUser.getCurrentUser().getUsername());
                        movieInfo.put("major", ParseUser.getCurrentUser().get("major"));
                        movieInfo.put("title", movieTitle.getText());
                        movieInfo.saveInBackground();
                    }
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
                }
            }
        });
        starBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rate, boolean fromUser) {

                if (rate < .5f) {
                    ratingBar.setRating(.5f);
                    rate = .5f;
                }
                thisActivity.rating = rate;
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
    }

    @Override
    public void onResume() {
        super.onResume();
        final ParseQuery<ParseObject> query = ParseQuery.getQuery("Ratings");
        query.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());
        try {
            movieName = RVMovAdapter.movieToPass.name;
            query.whereEqualTo("movieId", movieObject.getId());
            movieInfo = query.getFirst();
        } catch (com.parse.ParseException e) {
            Log.d("e", String.valueOf(e));
        }
        if (movieInfo != null) {
            rating = (float) movieInfo.getDouble("rating");
            comment = movieInfo.getString("comment");
            commentEditText.setText(comment);
            starBar.setRating(rating);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        /**
         * bitmap Image for the download
         */
        private ImageView bmImage;

        /**
         * Constructor for download image
         * @param image imageView to place bitmap in
         */
        public DownloadImageTask(ImageView image) {
            this.bmImage = image;
        }

        /**
         * Make bitmap in background
         * @param urls url of the bitmap
         * @return Bitmap object
         */
        protected Bitmap doInBackground(String... urls) {
            final String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                final InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (MalformedURLException  e) {
                Log.d("m", String.valueOf(e));
            } catch (IOException e) {
                Log.d("i", String.valueOf(e));
            }
            return mIcon11;
        }

        /**
         * Set Imageview to bitmap
         *
         * @param result bitmap to set the imageview to
         */
        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }

    }
}