package com.mymovieapp;

import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by Angelo on 3/19/2016.
 */
public class RVMovAdapter extends RecyclerView.Adapter<RVMovAdapter.MovieViewHolder> {

    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView movieName;
        TextView releaseDate;
        TextView details;
        ImageView movPhoto;

        MovieViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.movCardView);
            movieName = (TextView) itemView.findViewById(R.id.movie_name);
            releaseDate = (TextView) itemView.findViewById(R.id.release_date);
            details = (TextView) itemView.findViewById(R.id.movie_details);
            movPhoto = (ImageView) itemView.findViewById(R.id.movie_photo);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent it = new Intent(v.getContext(), LoginActivity.class);
                    v.getContext().startActivity(it);
                    Toast.makeText(v.getContext(), "Card Clicked.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    List<NewMovieDrawerActivity.Movie> movies;

    RVMovAdapter(List<NewMovieDrawerActivity.Movie> movies) {
        this.movies = movies;
    }

    @Override
    public int getItemCount() {
        return  movies.size();
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.movie_card, viewGroup, false);
        return new MovieViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder movieViewHolder, int i) {
        movieViewHolder.movieName.setText("PlaceHolder name");
        movieViewHolder.releaseDate.setText("1990");
        movieViewHolder.details.setText("all details");
        movieViewHolder.movPhoto.setImageResource(R.drawable.profile_person);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
