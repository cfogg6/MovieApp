package com.mymovieapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Adapter for the RecyclerView regarding horizontal cards or movie cards.
 */
public class RVMovAdapter extends RecyclerView.Adapter<RVMovAdapter.MovieViewHolder> {

    /**
     * List of movies
     */
    private List<com.mymovieapp.Movie> movies;

    /**
     * Constructor for the adapter that sets the movies list to argument.
     * @param m List of movies to set the cards to
     */
    public RVMovAdapter(List<com.mymovieapp.Movie> m) {
        this.movies = m;
    }

    @Override
    public int getItemCount() {
        return  movies.size();
    }

    /**
     * Method to update the movie list for the adapter with a new set of movies.
     *
     * @param list New list to update the movie list to
     */
    public void updateMovies(List<com.mymovieapp.Movie> list) {
        movies = list;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        final View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.movie_card, viewGroup, false);
        return new MovieViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final MovieViewHolder movieViewHolder, int i) {
        final com.mymovieapp.Movie mov = movies.get(i);

        movieViewHolder.movieName.setText(mov.getName());
        movieViewHolder.releaseDate.setText(mov.getDate());
        movieViewHolder.details.setText(mov.getRatingRuntime());
        movieViewHolder.starBar.setRating((float) mov.getRating().getAverageRating());

        new DownloadImageTask(movieViewHolder.movPhoto).execute(mov.getPhotoID());
        
        movieViewHolder.cvLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent it = new Intent(v.getContext(), MovieInfoActivity.class);
                it.putExtra("SALTY_POPCORN_CURRENT_MOVIE", mov);
                v.getContext().startActivity(it);
            }
        });
    }

    /**
     * ViewHolder Class following the ViewHolder Android Pattern. Establishes views held inside
     * the movie cards that this adapter sets.
     */
    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        //        private CardView cv;
        /**
         * Movie title
         */
        private TextView movieName;
        /**
         * Movie release date
         */
        private TextView releaseDate;
        /**
         * Movie details
         */
        private TextView details;
        /**
         * Movie rating
         */
        private RatingBar starBar;
        /**
         * Movie photo
         */
        private ImageView movPhoto;
        /**
         * relative layout
         */
        private RelativeLayout cvLayout;

        /**
         * View
         * @param itemView view
         */
        MovieViewHolder(View itemView) {
            super(itemView);
//            cv = (CardView) itemView.findViewById(R.id.movCardView);
            movieName = (TextView) itemView.findViewById(R.id.movie_name);
            releaseDate = (TextView) itemView.findViewById(R.id.release_date);
            details = (TextView) itemView.findViewById(R.id.movie_details);
            movPhoto = (ImageView) itemView.findViewById(R.id.movie_photo);
            cvLayout = (RelativeLayout) itemView.findViewById(R.id.cv_layout);
            starBar = (RatingBar) itemView.findViewById(R.id.mov_rating);
        }
    }

    /**
     * Class to download images
     */
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        /**
         * image
         */
        private ImageView bmImage;

        /**
         * Create image downloader
         * @param i Image to download
         */
        public DownloadImageTask(ImageView i) {
            this.bmImage = i;
        }

        @Override
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

        @Override
        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }

    }
}
