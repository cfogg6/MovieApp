package com.mymovieapp;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Angelo on 3/28/2016.
 */
public class RVUserAdapter extends RecyclerView.Adapter<RVUserAdapter.UserViewHolder> {
    List<AdminUser> users  = new ArrayList<>();
    List<AdminUser> unlockedUsers  = new ArrayList<>();
    List<AdminUser> lockedUsers  = new ArrayList<>();
    List<AdminUser> bannedUsers  = new ArrayList<>();
    Context context;
    public String mode = "ALL";

    public RVUserAdapter(Activity parentActivity, List<AdminUser> users)  {
        context = parentActivity;
        this.users = users;
        updateLists();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        RelativeLayout cvLayout;
        TextView username;
        ImageView userStatus;
        CircleImageView profPhoto;

        UserViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.userCardView);
            cvLayout = (RelativeLayout) itemView.findViewById(R.id.cv_layout);
            username = (TextView) itemView.findViewById(R.id.user_name);
            userStatus = (ImageView) itemView.findViewById(R.id.status_image);
            profPhoto = (CircleImageView) itemView.findViewById(R.id.profile_image);
        }
    }

    public void setTabMode(String mode) {
        this.mode = mode;
    }

    public void updateLists() {
        for (AdminUser user: users) {
            if (user.isLocked()) {
                if (!lockedUsers.contains(user)) {
                    lockedUsers.add(user);
                }
                unlockedUsers.remove(user);
                if (user.isBanned() && !bannedUsers.contains(user)) {
                    bannedUsers.add(user);
                }
            } else if (user.isBanned()) {
                unlockedUsers.remove(user);
                if (!bannedUsers.contains(user)) {
                    bannedUsers.add(user);
                }
            } else {
                if (!unlockedUsers.contains(user)) {
                    unlockedUsers.add(user);
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        final List<AdminUser> currentList;
        switch (mode) {
            case "ALL": {
                currentList = users;
                break;
            }
            case "UNLOCKED": {
                currentList = unlockedUsers;
                break;
            }
            case "LOCKED": {
                currentList = lockedUsers;
                break;
            }
            case "BANNED": {
                currentList = bannedUsers;
                break;
            }
            default: {
                currentList = users;
                break;
            }
        }
        return currentList.size();
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.user_card, viewGroup, false);
        UserViewHolder uvh = new UserViewHolder(v);
        uvh.username.setText(users.get(i).getName());
//        uvh.profPhoto.setImageDrawable(users.get(i).profilePic);
//        uvh.userStatus.setImageDrawable(users.get(i).statusImage);
        uvh.profPhoto.setImageResource(R.mipmap.bucket);
        return uvh;
    }

    @Override
    public void onBindViewHolder(final UserViewHolder userViewHolder, final int i) {
        final List<AdminUser> currentList;
        switch (mode) {
            case "ALL": {
                currentList = users;
                break;
            }
            case "UNLOCKED": {
                currentList = unlockedUsers;
                break;
            }
            case "LOCKED": {
                currentList = lockedUsers;
                break;
            }
            case "BANNED": {
                currentList = bannedUsers;
                break;
            }
            default: {
                currentList = users;
                break;
            }
        }
        userViewHolder.username.setText(currentList.get(i).getName());
        userViewHolder.userStatus.setImageResource(R.drawable.ic_check_24dp);
        userViewHolder.profPhoto.setImageResource(R.mipmap.bucket);
        if (currentList.get(i).isBanned()) {
            userViewHolder.userStatus.setImageResource(R.drawable.ic_not_interested_24dp);
        } else if (currentList.get(i).isLocked()) {
            userViewHolder.userStatus.setImageResource(R.drawable.ic_lock_24dp);
        } else {
            userViewHolder.userStatus.setImageResource(R.drawable.ic_check_24dp);
        }
        userViewHolder.cvLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AdminUser user = currentList.get(i);
                final Dialog dialog = new Dialog(context);
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
                                userViewHolder.userStatus.setImageResource(R.drawable.ic_not_interested_24dp);
                                if (!bannedUsers.contains(currentList.get(i))) {
                                    bannedUsers.add(currentList.get(i));
                                }
                                unlockedUsers.remove(currentList.get(i));
                                if (mode.equals("UNLOCKED")) {
                                    dialog.dismiss();
                                    notifyDataSetChanged();
                                }
                            } else {
                                if (!currentList.get(i).isLocked() && !unlockedUsers.contains(currentList.get(i))) {
                                    unlockedUsers.add(currentList.get(i));
                                }
                                bannedUsers.remove(currentList.get(i));
                                userViewHolder.userStatus.setImageResource(R.drawable.ic_check_24dp);
                                ParseObject.createWithoutData("Banned", bannedObj.getObjectId()).deleteInBackground();
                                if (mode.equals("BANNED")) {
                                    dialog.dismiss();
                                    notifyDataSetChanged();
                                }
                            }
                        } catch (ParseException e) {
                            if (isChecked) {
                                ParseObject bannedObj = new ParseObject("Banned");
                                bannedObj.put("username", user.getName());
                                bannedObj.saveInBackground();
                                if (!bannedUsers.contains(currentList.get(i))) {
                                    bannedUsers.add(currentList.get(i));
                                }
                                unlockedUsers.remove(currentList.get(i));
                                userViewHolder.userStatus.setImageResource(R.drawable.ic_not_interested_24dp);
                                if (mode.equals("UNLOCKED")) {
                                    dialog.dismiss();
                                    notifyDataSetChanged();
                                }
                            } else {
                                bannedUsers.remove(currentList.get(i));
                                if (!currentList.get(i).isLocked() && !unlockedUsers.contains(currentList.get(i))) {
                                    unlockedUsers.add(currentList.get(i));
                                }
                                userViewHolder.userStatus.setImageResource(R.drawable.ic_check_24dp);
                                if (mode.equals("BANNED")) {
                                    dialog.dismiss();
                                    notifyDataSetChanged();
                                }
                            }
                        }
                    }
                });
                final SwitchCompat lockSwitch = (SwitchCompat) dialog.findViewById(R.id.sw_locked);
                lockSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        user.setLocked(false);
                        ParseQuery<ParseObject> bannedQuery = ParseQuery.getQuery("Locked");
                        bannedQuery.whereEqualTo("username", user.getName());
                        try {
                            ParseObject lockedObj = bannedQuery.getFirst();
                            ParseObject.createWithoutData("Locked", lockedObj.getObjectId()).deleteInBackground();
                            lockedObj.saveInBackground();
                            if (!lockedUsers.contains(currentList.get(i))) {
                                lockedUsers.add(currentList.get(i));
                            }
                            unlockedUsers.remove(currentList.get(i));
                            userViewHolder.userStatus.setImageResource(R.drawable.ic_check_24dp);
                            if (mode.equals("UNLOCKED")) {
                                dialog.dismiss();
                                notifyDataSetChanged();
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        lockSwitch.setVisibility(View.GONE);
                    }
                });
                if (!user.isLocked) {
                    lockSwitch.setVisibility(View.GONE);
                } else {
                    lockSwitch.setVisibility(View.VISIBLE);
                }
                dialog.show();
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
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }

    }
}
