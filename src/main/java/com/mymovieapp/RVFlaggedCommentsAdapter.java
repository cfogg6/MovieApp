package com.mymovieapp;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Recycler View Adapter for Flagged Comments
 */
public class RVFlaggedCommentsAdapter extends RecyclerView.Adapter<RVFlaggedCommentsAdapter.UserViewHolder> {
    /**
     * List of comments
     */
    private List<FlaggedComment> comments = new ArrayList<>();
    /**
     * current context
     */
    private Context context;
    /**
     * Constructor that sets the context of the adapter and the list of users to the argument list.
     * @param c Comments list
     */
    public RVFlaggedCommentsAdapter(Activity parentActivity, List<FlaggedComment> c)  {
        context = parentActivity;
        this.comments = c;
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        final View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.comment_row, viewGroup, false);
        String title = comments.get(i).getTitle();
        String username = comments.get(i).getUsername();
        String comment = comments.get(i).getComment();
        double rating = comments.get(i).getRating();
        final UserViewHolder uvh = new UserViewHolder(v, title, comment, username);
        uvh.username.setText(username);
//        uvh.profPhoto.setImageDrawable(users.get(i).profilePic);
        uvh.profPhoto.setImageResource(R.mipmap.bucket);
        uvh.comment.setText(comment);
        uvh.starBar.setRating((float) rating);
        return uvh;
    }

    @Override
    public void onBindViewHolder(final UserViewHolder userViewHolder, final int i) {
        userViewHolder.username.setText(comments.get(i).getUsername());
        userViewHolder.profPhoto.setImageResource(R.mipmap.bucket);
        userViewHolder.comment.setText(comments.get(i).getComment());
        userViewHolder.starBar.setRating((float) comments.get(i).getRating());
        userViewHolder.flaggedImage.setImageResource(R.drawable.ic_flag_24dp);

        userViewHolder.cvLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int pos = userViewHolder.getAdapterPosition();
                final FlaggedComment comment = comments.get(pos);
                // Use the AlertDialog.Builder to configure the AlertDialog.
                AlertDialog.Builder alertDialogBuilder =
                        new AlertDialog.Builder(context)
                                .setTitle("Flagged Comment Options")
                                .setMessage("Do you want to Delete or Unflag the Comment?")
                                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        comment.setDeleted(true);
                                        final ParseQuery<ParseObject> deletedQuery = ParseQuery.getQuery("FlaggedComments");
                                        deletedQuery.whereEqualTo("username", comment.getUsername());
                                        deletedQuery.whereEqualTo("title", comment.getTitle());
                                        deletedQuery.whereEqualTo("comment", comment.getComment());

                                        try {
                                            final ParseObject lockedObj = deletedQuery.getFirst();
                                            ParseObject.createWithoutData("FlaggedComments", lockedObj.getObjectId()).deleteInBackground();
                                            lockedObj.saveInBackground();
                                        } catch (ParseException e) {
                                            Log.d("e", String.valueOf(e));
                                        }

                                        comments.remove(comments.get(pos));

                                        dialog.dismiss();
                                        notifyItemRemoved(pos);
                                        notifyItemRangeChanged(pos, getItemCount());
                                    }
                                })
                                .setNegativeButton("Unflag", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        comment.setDeleted(true);
                                        final ParseQuery<ParseObject> deletedQuery = ParseQuery.getQuery("FlaggedComments");
                                        deletedQuery.whereEqualTo("username", comment.getUsername());
                                        deletedQuery.whereEqualTo("title", comment.getTitle());
                                        deletedQuery.whereEqualTo("comment", comment.getComment());

                                        try {
                                            final ParseObject lockedObj = deletedQuery.getFirst();
                                            ParseObject.createWithoutData("FlaggedComments", lockedObj.getObjectId()).deleteInBackground();
                                            lockedObj.saveInBackground();
                                        } catch (ParseException e) {
                                            Log.d("e", String.valueOf(e));
                                        }

                                        comments.remove(comments.get(pos));

                                        dialog.dismiss();
                                        notifyItemRemoved(pos);
                                        notifyItemRangeChanged(pos, getItemCount());
                                    }
                                })
                                .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });

                // Show the AlertDialog.
                AlertDialog alertDialog = alertDialogBuilder.show();
            }
        });
    }

    /**
     * Update the list of comments
     * @param c new list of comments
     */
    public void updateComments(List<FlaggedComment> c) {
        this.comments = c;
    }

    /**
     * ViewHolder Class following the ViewHolder Android Pattern. Establishes views held inside
     * the movie cards that this adapter sets.
     */
    public static class UserViewHolder extends RecyclerView.ViewHolder {
        /**
         * Rel Layout
         */
        private RelativeLayout cvLayout;
        /**
         * Username
         */
        private final TextView username;
        /**
         * Comment
         */
        private final TextView comment;
        /**
         * Movie picture
         */
        private final CircleImageView profPhoto;
        /**
         * Starbar to show rating value
         */
        private final RatingBar starBar;
        /**
         * Image
         */
        private final ImageView flaggedImage;
        /**
         * title of movie
         */
        private final String title;
        /**
         * comment
         */
        private final String commentString;
        /**
         * username
         */
        private final String usernameString;

        /**
         * Userview Holder
         * @param itemView itemview
         * @param t string
         * @param c comment
         * @param u user
         */
        UserViewHolder(View itemView, String t, String c, String u) {
            super(itemView);
            username = (TextView) itemView.findViewById(R.id.user_name);
            profPhoto = (CircleImageView) itemView.findViewById(R.id.profile_image);
            comment = (TextView) itemView.findViewById(R.id.tv_comment);
            starBar = (RatingBar) itemView.findViewById(R.id.rb_star_bar);
            flaggedImage = (ImageView) itemView.findViewById(R.id.iv_flagged);
            cvLayout = (RelativeLayout) itemView.findViewById(R.id.cv_layout);
            this.title = t;
            this.commentString = c;
            this.usernameString = u;
            flaggedImage.setImageResource(R.drawable.ic_flag_24dp);
        }
    }
}
