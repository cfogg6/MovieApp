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
 * Adapter for the RecyclerView regarding Square Cards or Search Cards
 */
public class RVSearchAdapter extends RecyclerView.Adapter<RVSearchAdapter.SearchViewHolder> {

    /**
     * List of movies returned by the search call
     */
    public List<com.mymovieapp.Movie> movies;

    /**
     * Constructor for the adapter that sets the movies list to argument.
     * @param m List of movies to set the cards to
     */
    RVSearchAdapter(List<com.mymovieapp.Movie> m) {
        this.movies = m;
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    @Override
    public SearchViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        final View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.search_card, viewGroup, false);
        return new SearchViewHolder(v);
    }

    @Override
    public void onBindViewHolder(SearchViewHolder searchViewHolder, int i) {
        final com.mymovieapp.Movie mov = movies.get(i);
        searchViewHolder.movName.setText(movies.get(i).getName());
        searchViewHolder.starBar.setRating((float) movies.get(i).getRating().getAverageRating());
        new DownloadImageTask(searchViewHolder.movPhoto).execute(movies.get(i).getPhotoID());

        searchViewHolder.cvLayout.setOnClickListener(new View.OnClickListener() {
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
    public static class SearchViewHolder extends RecyclerView.ViewHolder {
        //private CardView cv;
        /**
         * Layout
         */
        private RelativeLayout cvLayout;
        /**
         * title of movie
         */
        private TextView movName;
        /**
         * picture of movie
         */
        private ImageView movPhoto;
        /**
         * rating for movie
         */
        private RatingBar starBar;

        /**
         * View
         * @param itemView item view
         */
        SearchViewHolder(View itemView) {
            super(itemView);
            //cv = (CardView)itemView.findViewById(R.id.searchCardView);
            cvLayout = (RelativeLayout)itemView.findViewById(R.id.cv_layout);
            movName = (TextView)itemView.findViewById(R.id.movie_name);
            movPhoto = (ImageView) itemView.findViewById(R.id.movie_photo);
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
            if (urldisplay != null) {
                Bitmap mIcon11 = null;
                try {
                    final InputStream in = new java.net.URL(urldisplay).openStream();
                    mIcon11 = BitmapFactory.decodeStream(in);
                } catch (IOException e) {
                    Log.d("e", String.valueOf(e));
                }
                return mIcon11;
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }

    }
}
