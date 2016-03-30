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
 * Created by Angelo on 3/28/2016.
 */
public class RVSearchAdapter extends RecyclerView.Adapter<RVSearchAdapter.SearchViewHolder> {
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
    static com.mymovieapp.Movie movieToPass = new com.mymovieapp.Movie("", "", "", "", "", null);

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
        SearchViewHolder svh = new SearchViewHolder(v);
        return svh;
    }

    @Override
    public void onBindViewHolder(SearchViewHolder searchViewHolder, int i) {
        final Movie mov = movies.get(i);
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
