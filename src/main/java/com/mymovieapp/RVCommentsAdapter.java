package com.mymovieapp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Adapter for the RecyclerView regarding horizontal user cards for admin purposes.
 */
public class RVCommentsAdapter extends RecyclerView.Adapter<RVCommentsAdapter.UserViewHolder> {
    /**
     * List of comments
     */
    private final List<Comment> comments = new ArrayList<>();
    /**
     * List of users
     */
    private final List<AdminUser> users  = new ArrayList<>();
    /**
     * Title
     */
    private final String title;

    /**
     * Constructor that sets the context of the adapter and the list of users to the argument list.
     * @param t String t
     */
    public RVCommentsAdapter(String t)  {
        this.title = t;
    }

       /**
        * get the list of users
        * @return list of users
        */
    public List<AdminUser> getUsers() {
        return users;
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        final View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.comment_row, viewGroup, false);
        final UserViewHolder uvh = new UserViewHolder(v, title, comments.get(i).comment, users.get(i).getName());
        uvh.username.setText(users.get(i).getName());
//        uvh.profPhoto.setImageDrawable(users.get(i).profilePic);
        uvh.profPhoto.setImageResource(R.mipmap.bucket);
        uvh.comment.setText(comments.get(i).comment);
        uvh.starBar.setRating((float) comments.get(i).rating);
        return uvh;
    }

    @Override
    public void onBindViewHolder(final UserViewHolder userViewHolder, final int i) {
        userViewHolder.username.setText(comments.get(i).user.getName());
        userViewHolder.profPhoto.setImageResource(R.mipmap.bucket);
        userViewHolder.comment.setText(comments.get(i).comment);
        userViewHolder.starBar.setRating((float) comments.get(i).rating);
        userViewHolder.updateCommentFlag();
    }

       /**
        * Add a comment
        * @param comment the string comment
        * @param username user who is giving the comment
        * @param rating the rating the user is giving
        */
    public void addComment(String comment, String username, double rating, Date date) {
        comments.add(new Comment(comment, rating, users.get(users.lastIndexOf(new AdminUser(username))), date));
        Collections.sort(comments);
    }

    private class Comment implements Comparable<Comment> {
        /**
         * Comment string
         */
        private final String comment;
        /**
         * Rating associated with comment
         */
        private final double rating;
        /**
         * Date submitted
         */
        Date date;
        /**
         * User who provided comment
         */
        private final AdminUser user;

        /**
         * Constructor for a new comment
         * @param c comment
         * @param r rating
         * @param u user
         */
        public Comment(String c, double r, AdminUser u, Date date) {
            this.comment = c;
            this.rating = r;
            this.user = u;
            this.date = date;
        }

        /**
         * Compare two comments based on date
         * @param comment comment begin compared
         * @return Returns compareTo result
         */
        public int compareTo(Comment comment) {
            if (!(comment instanceof Comment)) {
                return -1;
            }
            return comment.date.compareTo(this.date);
        }
    }

       /**
        * ViewHolder Class following the ViewHolder Android Pattern. Establishes views held inside
        * the movie cards that this adapter sets.
        */
    public static class UserViewHolder extends RecyclerView.ViewHolder {
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
            this.title = t;
            this.commentString = c;
            this.usernameString = u;
            updateCommentFlag();
        }

        /**
         * Update the flag of the comment
         */
        public void updateCommentFlag() {
            final ParseQuery<ParseObject> query = ParseQuery.getQuery("FlaggedComments");
            query.whereEqualTo("username", usernameString);
            query.whereEqualTo("title", title);
            query.whereEqualTo("comment", commentString);
            try {
                query.getFirst();
                flaggedImage.setImageResource(R.drawable.ic_flag_24dp);
            } catch (ParseException e) {
                flaggedImage.setImageResource(R.drawable.ic_check_24dp);
            }
        }
    }
}
