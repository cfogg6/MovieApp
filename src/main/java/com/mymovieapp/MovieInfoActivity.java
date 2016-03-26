package com.mymovieapp;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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

import java.io.InputStream;

/**
 * Created by Corey on 2/26/16.
 */
public class MovieInfoActivity extends Activity {
    final Activity activity = this;
    ParseObject movieInfo;
    float rating;
    RatingBar starBar;
    String movieName;
    EditText commentEditText;
    AppCompatImageView movPic;
    TextView synopsis;
    String comment;
    Button commentButton;
    MovieInfoActivity thisActivity = this;
    NewMovieDrawerActivity.Movie movieObject = RVMovAdapter.movieToPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_info);
        final TextView movieTitle = (TextView) findViewById(R.id.tv_movie_title);
        starBar = (RatingBar) findViewById(R.id.rb_star_bar);
        commentEditText = (EditText) findViewById(R.id.et_comment);
        movPic = (AppCompatImageView) findViewById(R.id.iV_movPhoto);
        synopsis = (TextView) findViewById(R.id.tV_synopsis);

        starBar.setRating(0);
        movieName = movieObject.name;
        synopsis.setText(movieObject.synopsis);
        new DownloadImageTask(movPic).execute(movieObject.photoId);
        movieTitle.setText(movieName);
        commentButton = (Button) findViewById(R.id.btn_comment);
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
                        movieInfo.put("title", movieName);
                        movieInfo.saveInBackground();
                    }
                    movieInfo.put("rating", rating);
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
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

                if (rating < .5f) {
                    ratingBar.setRating(.5f);
                    rating = .5f;
                }
                thisActivity.rating = rating;
            }
        });
        RelativeLayout newDVDLinearLayout = (RelativeLayout) findViewById(R.id.rl_movie_info);
        /**
         * Makes keyboard disappear when you click away from an EditText field
         */
        newDVDLinearLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!(v instanceof EditText)) {
                    InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
                return true;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Ratings");
        query.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());
        try {
            movieName = RVMovAdapter.movieToPass.name.toString();
            query.whereEqualTo("title", movieName);
            movieInfo = query.getFirst();
        } catch (com.parse.ParseException e) {
            e.printStackTrace();
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

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }

    }
}