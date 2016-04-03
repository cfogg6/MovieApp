package com.mymovieapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.InputStream;
import java.util.List;

/**
 * Adapter for the RecyclerView regarding Square Cards or Search Cards
 */
public class RVSearchAdapter extends RecyclerView.Adapter<RVSearchAdapter.SearchViewHolder> {

    /**
     * ViewHolder Class following the ViewHolder Android Pattern. Establishes views held inside
     * the movie cards that this adapter sets.
     */
    public static class SearchViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        RelativeLayout cvLayout;
        TextView movName;
        ImageView movPhoto;
        RatingBar starBar;

        SearchViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.searchCardView);
            cvLayout = (RelativeLayout)itemView.findViewById(R.id.cv_layout);
            movName = (TextView)itemView.findViewById(R.id.movie_name);
            movPhoto = (ImageView) itemView.findViewById(R.id.movie_photo);
            starBar = (RatingBar) itemView.findViewById(R.id.mov_rating);
        }
    }

    List<com.mymovieapp.Movie> movies;
    static com.mymovieapp.Movie movieToPass = new com.mymovieapp.Movie("", "", "", "", "", "", null);

    /**
     * Constructor for the adapter that sets the movies list to argument.
     * @param movies List of movies to set the cards to
     */
    RVSearchAdapter(List<com.mymovieapp.Movie> movies) {
        this.movies = movies;
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    @Override
    public SearchViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
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
                Intent it = new Intent(v.getContext(), MovieInfoActivity.class);
                it.putExtra("SALTY_POPCORN_CURRENT_MOVIE", mov);
                v.getContext().startActivity(it);

            }
        });
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        /**
         * Create image downloader
         * @param bmImage Image to download
         */
        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        @Override
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

        @Override
        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }

    }
}
