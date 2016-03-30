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
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Angelo on 3/19/2016.
 */
public class RVMovAdapter extends RecyclerView.Adapter<RVMovAdapter.MovieViewHolder> {

    List<com.mymovieapp.Movie> movies;
    static com.mymovieapp.Movie movieToPass = new com.mymovieapp.Movie("", "", "", "", "", null);

    public RVMovAdapter(List<com.mymovieapp.Movie> movies) {
        this.movies = movies;
    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView movieName;
        TextView releaseDate;
        TextView details;
        ImageView movPhoto;
        RelativeLayout cvLayout;

        MovieViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.movCardView);
            movieName = (TextView) itemView.findViewById(R.id.movie_name);
            releaseDate = (TextView) itemView.findViewById(R.id.release_date);
            details = (TextView) itemView.findViewById(R.id.movie_details);
            movPhoto = (ImageView) itemView.findViewById(R.id.movie_photo);
            cvLayout = (RelativeLayout) itemView.findViewById(R.id.cv_layout);
        }
    }

    @Override
    public int getItemCount() {
        return  movies.size();
    }

    public void updateMovies(ArrayList<com.mymovieapp.Movie> list) {
        movies = list;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.movie_card, viewGroup, false);
        return new MovieViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final MovieViewHolder movieViewHolder, int i) {
        final com.mymovieapp.Movie mov = movies.get(i);

        movieViewHolder.movieName.setText(mov.getName());
        movieViewHolder.releaseDate.setText(mov.getDate());
        movieViewHolder.details.setText(mov.getRatingRuntime());

        new DownloadImageTask(movieViewHolder.movPhoto).execute(mov.photoId);
        
        movieViewHolder.cvLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(v.getContext(), MovieInfoActivity.class);
                it.putExtra("SALTY_POPCORN_CURRENT_MOVIE", mov);
                v.getContext().startActivity(it);
                movieToPass.name = mov.getName();
                movieToPass.date = mov.getDate();
                movieToPass.photoId = mov.getPhotoID();
                movieToPass.synopsis = mov.getSynopsis();
                movieToPass.ratingRuntime = mov.getRatingRuntime();
                movieToPass.rating = mov.getRating();
            }
        });
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
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
                Log.d("url", urldisplay);
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                //Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }

    }
}
