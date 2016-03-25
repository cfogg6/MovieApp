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
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.Switch;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;

/**
 * Created by Corey on 3/13/16.
 */
public class UserListAdapter extends ArrayAdapter {
    int count = 0;
    LayoutInflater inflater;
    Context context;
    ArrayList<AdminUser> users = new ArrayList<>();

    public UserListAdapter(Activity parentActivity, int textViewResourceId) {
        super(parentActivity, textViewResourceId);
        context = parentActivity;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void updateUsers(ArrayList<AdminUser> users) {
        this.users = users;
    }

    public int getCount() {
        return users.size();
    }

    public View getView(final int position, View convertView, final ViewGroup parent) {
        final ViewHolder viewHolder;
        if(convertView == null) {
            convertView = inflater.inflate(R.layout.row_admin_user, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.usernameTextView = (TextView) convertView.findViewById(R.id.tv_username);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final AdminUser user = users.get(position);
                    Dialog dialog = new Dialog(context);
                    dialog.setContentView(R.layout.menu_admin_options);
                    SwitchCompat bannedSwitch = (SwitchCompat)dialog.findViewById(R.id.sw_banned);
                    bannedSwitch.setChecked(user.isBanned());
                    bannedSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            user.setBanned(isChecked);
                            ParseQuery<ParseObject> bannedQuery = ParseQuery.getQuery("Banned");
                            bannedQuery.whereEqualTo("username", user.getName());
                            try {
                                ParseObject bannedObj = bannedQuery.getFirst();
                                if (isChecked) {
                                    bannedObj.put("username", user.getName());
                                    bannedObj.saveInBackground();
                                } else {
                                    ParseObject.createWithoutData("Banned", bannedObj.getObjectId()).deleteInBackground();
                                }
                            } catch (ParseException e) {
                                if (isChecked) {
                                    ParseObject bannedObj = new ParseObject("Banned");
                                    bannedObj.put("username", user.getName());
                                    bannedObj.saveInBackground();
                                }
                            }
                        }
                    });
                    final Button lockButton = (Button) dialog.findViewById(R.id.btn_banned);
                    lockButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            user.setLocked(false);
                            ParseQuery<ParseObject> bannedQuery = ParseQuery.getQuery("Locked");
                            bannedQuery.whereEqualTo("username", user.getName());
                            try {
                                ParseObject lockedObj = bannedQuery.getFirst();
                                ParseObject.createWithoutData("Locked", lockedObj.getObjectId()).deleteInBackground();
                                lockedObj.saveInBackground();
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            lockButton.setVisibility(View.GONE);
                        }
                    });
                    if (!user.isLocked) {
                        lockButton.setVisibility(View.GONE);
                    } else {
                        lockButton.setVisibility(View.VISIBLE);
                    }
                    dialog.show();
                }
            });
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)convertView.getTag();
        }
        viewHolder.usernameTextView.setText(users.get(position).getName());
        return convertView;
    }

    private static class ViewHolder {
        TextView usernameTextView;
    }
}
