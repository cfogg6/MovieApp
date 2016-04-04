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

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.InputStream;
import java.util.List;

/**
 * Activity that shows movie information on selected movie using json query from RottenTomatoes API
 * Also allows users to comment and rate movies themselves to bbe saved internally for
 * recommendations by major.
 */
public class MovieInfoActivity extends BackToolbarActivity {
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
    com.mymovieapp.Movie movieObject;
    private RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_info);
        final TextView movieTitle = (TextView) findViewById(R.id.tv_movie_title);
        starBar = (RatingBar) findViewById(R.id.rb_star_bar);
        commentEditText = (EditText) findViewById(R.id.et_comment);
        movPic = (AppCompatImageView) findViewById(R.id.iV_movPhoto);
        synopsis = (TextView) findViewById(R.id.tV_synopsis);
        rv = (RecyclerView) findViewById(R.id.comments_rv);
        movieObject = (getIntent().getParcelableExtra("SALTY_POPCORN_CURRENT_MOVIE"));
        starBar.setRating(0);
        movieName = movieObject.getName();
        if (movieObject.getSynopsis().equals("")) {
            synopsis.setText("No synopsis.");
        } else {
            synopsis.setText(movieObject.getSynopsis());
        }
        new DownloadImageTask(movPic).execute(movieObject.getPhotoID());
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
        ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");
        RecyclerView rv = (RecyclerView) findViewById(R.id.comments_rv);
        final RVCommentsAdapter adapter = new RVCommentsAdapter(activity, movieObject.getName());
        rv.setAdapter(adapter);
        LinearLayoutManager llm = new LinearLayoutManager(activity);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(llm);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null) {
                    RecyclerView rv = (RecyclerView) findViewById(R.id.comments_rv);
                    final RVCommentsAdapter adapter = new RVCommentsAdapter(activity, movieObject.getName());
                    final LinearLayoutManager llm = new LinearLayoutManager(activity);
                    llm.setOrientation(LinearLayoutManager.VERTICAL);
                    rv.setLayoutManager(llm);
                    rv.setAdapter(adapter);
                    for (ParseObject element : list) {
                        adapter.users.add(new AdminUser(element.getString("username")));
                    }
                    ParseQuery<ParseObject> query = ParseQuery.getQuery("Ratings");
                    query.whereEqualTo("title", movieObject.getName());
                    query.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> list, ParseException e) {
                            if (e == null) {
                                for (ParseObject element : list) {
                                    adapter.addComment(element.getString("comment"), element.getString("username"),
                                            element.getDouble("rating"));
                                }
                            } else {
                                e.printStackTrace();
                            }
                            RecyclerView rv = (RecyclerView) findViewById(R.id.comments_rv);
                            rv.setAdapter(adapter);
                            llm.setOrientation(LinearLayoutManager.VERTICAL);
                            rv.setLayoutManager(llm);
                            (rv.getAdapter()).notifyDataSetChanged();
                        }
                    });
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Ratings");
        query.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());
        try {
            movieObject = (getIntent().getParcelableExtra("SALTY_POPCORN_CURRENT_MOVIE"));
            movieName = movieObject.name;
            query.whereEqualTo("movieId", movieObject.getId());
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
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }

    }
}