package com.mymovieapp;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class FlaggedCommentsAdapter extends ArrayAdapter {
    int count = 0;
    LayoutInflater inflater;
    Context context;
    ArrayList<FlaggedComment> comments = new ArrayList<>();

    public FlaggedCommentsAdapter(Activity parentActivity, int textViewResourceId) {
        super(parentActivity, textViewResourceId);
        context = parentActivity;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void updateComments(ArrayList<FlaggedComment> comments) {
        this.comments = comments;
    }

    public int getCount() {
        return comments.size();
    }

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
                    Dialog dialog = new Dialog(context);
                    dialog.setContentView(R.layout.menu_flagged_comments_options);

                    final Button deleteButton = (Button) dialog.findViewById(R.id.btn_delete);
                    deleteButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            comment.setDeleted(true);
                            ParseQuery<ParseObject> deletedQuery = ParseQuery.getQuery("FlaggedComments");
                            deletedQuery.whereEqualTo("username", comment.getUsername());
                            deletedQuery.whereEqualTo("title", comment.getTitle());
                            deletedQuery.whereEqualTo("comment", comment.getComment());

                            try {
                                ParseObject lockedObj = deletedQuery.getFirst();
                                ParseObject.createWithoutData("FlaggedComments", lockedObj.getObjectId()).deleteInBackground();
                                lockedObj.saveInBackground();
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            deleteButton.setVisibility(View.GONE);
                        }
                    });
                    if (!comment.isDeleted) {
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
        TextView usernameTextView;
        TextView titleTextView;
        TextView commentTextView;
    }
}
