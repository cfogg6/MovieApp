package com.mymovieapp;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
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
    * Adapter for the RecyclerView regarding horizontal user cards for admin purposes.
    */
    public class RVCommentsAdapter extends RecyclerView.Adapter<RVCommentsAdapter.UserViewHolder> {
       private List<Comment> comments = new ArrayList<>();
       private List<AdminUser> users  = new ArrayList<>();
       private Context context;
       private String title;

    /**
     * Constructor that sets the context of the adapter and the list of users to the argument list.
     * @param parentActivity The parent activity of the callee
     */
    public RVCommentsAdapter(Activity parentActivity, String t)  {
        context = parentActivity;
        this.title = t;
    }

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
        userViewHolder.updateFlag();
    }

    public void addComment(String comment, String username, double rating) {
        comments.add(new Comment(comment, rating, users.get(users.lastIndexOf(new AdminUser(username)))));
    }

    private class Comment {
        private String comment;
        private double rating;
        private AdminUser user;
        public Comment(String c, double r, AdminUser u) {
            this.comment = c;
            this.rating = r;
            this.user = u;
        }
    }

       /**
        * ViewHolder Class following the ViewHolder Android Pattern. Establishes views held inside
        * the movie cards that this adapter sets.
        */
       public static class UserViewHolder extends RecyclerView.ViewHolder {
           private RelativeLayout cvLayout;
           private TextView username;
           private TextView comment;
           private CircleImageView profPhoto;
           private RatingBar starBar;
           private ImageView flaggedImage;
           private boolean isFlagged;
           private String title;
           private String commentString;
           private String usernameString;

           UserViewHolder(View itemView, String t, String c, String u) {
               super(itemView);
               cvLayout = (RelativeLayout) itemView.findViewById(R.id.cv_layout);
               username = (TextView) itemView.findViewById(R.id.user_name);
               profPhoto = (CircleImageView) itemView.findViewById(R.id.profile_image);
               comment = (TextView) itemView.findViewById(R.id.tv_comment);
               starBar = (RatingBar) itemView.findViewById(R.id.rb_star_bar);
               flaggedImage = (ImageView) itemView.findViewById(R.id.iv_flagged);
               this.title = t;
               this.commentString = c;
               this.usernameString = u;
               updateFlag();
           }

           public void updateFlag() {
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
