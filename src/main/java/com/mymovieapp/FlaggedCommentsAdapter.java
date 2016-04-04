package com.mymovieapp;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import java.util.ArrayList;

public class FlaggedCommentsAdapter extends ArrayAdapter {
    /**
     * Layout Inflator
     */
    private LayoutInflater inflater;
    /**
     * Context
     */
    private Context context;
    /**
     * Comments list
     */
    private ArrayList<FlaggedComment> comments = new ArrayList<>();

    /**
     * Creates a new Flagged Comments Adapter
     * @param parentActivity the parent activity
     * @param textViewResourceId text view resource id
     */
    public FlaggedCommentsAdapter(Activity parentActivity, int textViewResourceId) {
        super(parentActivity, textViewResourceId);
        context = parentActivity;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    /**
     * Update the list of comments
     * @param c new list of comments
     */
    public void updateComments(ArrayList<FlaggedComment> c) {
        this.comments = c;
    }

    /**
     * Get size of comments
     * @return size of comments
     */
    public int getCount() {
        return comments.size();
    }

    /**
     * Get the view to populate
     * @param position position
     * @param convertView convert view
     * @param parent parent
     * @return View that has beenn populated
     */
    public View getView(final int position, View convertView, final ViewGroup parent) {
        final ViewHolder viewHolder;
        if(convertView == null) {
            convertView = inflater.inflate(R.layout.row_flagged_comments, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.usernameTextView = (TextView) convertView.findViewById(R.id.tv_username);
            viewHolder.titleTextView = (TextView) convertView.findViewById(R.id.tv_title);
            viewHolder.commentTextView = (TextView) convertView.findViewById(R.id.tv_comment);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final FlaggedComment comment = comments.get(position);
                    final Dialog dialog = new Dialog(context);
                    dialog.setContentView(R.layout.menu_flagged_comments_options);

                    final Button deleteButton = (Button) dialog.findViewById(R.id.btn_delete);
                    deleteButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
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
                            deleteButton.setVisibility(View.GONE);
                        }
                    });
                    if (!comment.isDeleted()) {
                        deleteButton.setVisibility(View.VISIBLE);
                    } else {
                        deleteButton.setVisibility(View.GONE);
                    }
                    dialog.show();
                }
            });
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)convertView.getTag();
        }
        viewHolder.usernameTextView.setText(comments.get(position).getUsername());
        viewHolder.titleTextView.setText(comments.get(position).getTitle());
        viewHolder.commentTextView.setText(comments.get(position).getComment());
        return convertView;
    }

    private static class ViewHolder {
        /**
         * Username Text View
         */
        private TextView usernameTextView;
        /**
         * Title Text View
         */
        private TextView titleTextView;
        /**
         * Comment Text View
         */
        private TextView commentTextView;

        /**
         * Getter for username text view
         * @return username text view
         */
        public TextView getUsernameTextView() {
            return usernameTextView;
        }

        /**
         * Getter for title text view
         * @return title text view
         */
        public TextView getTitleTextView() {
            return titleTextView;
        }

        /**
         * Getter for comment text view
         * @return comment text view
         */
        public TextView getCommentTextView() {
            return commentTextView;
        }
    }
}
