package com.mymovieapp;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Adapter for the RecyclerView regarding horizontal user cards for admin purposes.
 */
public class RVUserAdapter extends RecyclerView.Adapter<RVUserAdapter.UserViewHolder> {
    /**
     * List of users
     */
    private List<AdminUser> users  = new ArrayList<>();
    /**
     * List of unlocked users
     */
    private List<AdminUser> unlockedUsers  = new ArrayList<>();
    /**
     * List of locked users
     */
    private List<AdminUser> lockedUsers  = new ArrayList<>();
    /**
     * List of banned users
     */
    private List<AdminUser> bannedUsers  = new ArrayList<>();
    /**
     * current context
     */
    private Context context;
    /**
     * what mode the admin is viewing
     */
    private String mode = "ALL";

    /**
     * Getter for mode
     * @return String mode
     */
    public String getMode() {
        return mode;
    }

    /**
     * Constructor that sets the context of the adapter and the list of users to the argument list.
     * @param parentActivity The parent activity of the callee
     * @param u The list of users for the cards to populate from
     */
    public RVUserAdapter(Activity parentActivity, List<AdminUser> u)  {
        context = parentActivity;
        this.users = u;
        updateLists();
    }

    /**
     * Return the list of users
     * @return list of users
     */
    public List<AdminUser> getUsers() {
        return users;
    }

    /**
     * Method to update the lists regarding changes made in the tabs.
     */
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
        Collections.sort(users);
        Collections.sort(lockedUsers);
        Collections.sort(unlockedUsers);
        Collections.sort(bannedUsers);
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
        final View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.user_card, viewGroup, false);
        final UserViewHolder uvh = new UserViewHolder(v);
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
                Log.d("all count", String.valueOf(unlockedUsers.size()));
                break;
            }
            case "UNLOCKED": {
                currentList = unlockedUsers;
                Log.d("unlocked count", String.valueOf(unlockedUsers.size()));
                break;
            }
            case "LOCKED": {
                currentList = lockedUsers;
                Log.d("locked count", String.valueOf(lockedUsers.size()));
                break;
            }
            case "BANNED": {
                currentList = bannedUsers;
                Log.d("banned count", String.valueOf(bannedUsers.size()));
                break;
            }
            default: {
                currentList = users;
                break;
            }
        }
        userViewHolder.username.setText(currentList.get(i).getName());
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
                final SwitchCompat bannedSwitch = (SwitchCompat) dialog.findViewById(R.id.sw_banned);
                bannedSwitch.setChecked(user.isBanned());
                bannedSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        user.setBanned(isChecked);
                        final ParseQuery<ParseObject> bannedQuery = ParseQuery.getQuery("Banned");
                        bannedQuery.whereEqualTo("username", user.getName());
                        try {
                            final ParseObject bannedObj = bannedQuery.getFirst();
                            if (isChecked) {
                                bannedObj.put("username", user.getName());
                                bannedObj.saveInBackground();
                                userViewHolder.userStatus.setImageResource(R.drawable.ic_not_interested_24dp);
                                if (!bannedUsers.contains(currentList.get(i))) {
                                    bannedUsers.add(currentList.get(i));
                                }
                                unlockedUsers.remove(currentList.get(i));
                                if ("UNLOCKED".equals(mode)) {
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
                                if ("BANNED".equals(mode)) {
                                    dialog.dismiss();
                                    notifyDataSetChanged();
                                }
                            }
                        } catch (ParseException e) {
                            if (isChecked) {
                                final ParseObject bannedObj = new ParseObject("Banned");
                                bannedObj.put("username", user.getName());
                                bannedObj.saveInBackground();
                                if (!bannedUsers.contains(currentList.get(i))) {
                                    bannedUsers.add(currentList.get(i));
                                }
                                unlockedUsers.remove(currentList.get(i));
                                userViewHolder.userStatus.setImageResource(R.drawable.ic_not_interested_24dp);
                                if ("UNLOCKED".equals(mode)) {
                                    dialog.dismiss();
                                    notifyDataSetChanged();
                                }
                            } else {
                                bannedUsers.remove(currentList.get(i));
                                if (!currentList.get(i).isLocked() && !unlockedUsers.contains(currentList.get(i))) {
                                    unlockedUsers.add(currentList.get(i));
                                }
                                userViewHolder.userStatus.setImageResource(R.drawable.ic_check_24dp);
                                if ("BANNED".equals(mode)) {
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
                        user.setUserIsLocked(false);
                        final ParseQuery<ParseObject> bannedQuery = ParseQuery.getQuery("Locked");
                        bannedQuery.whereEqualTo("username", user.getName());
                        try {
                            final ParseObject lockedObj = bannedQuery.getFirst();
                            ParseObject.createWithoutData("Locked", lockedObj.getObjectId()).deleteInBackground();
                            lockedObj.saveInBackground();
                            if (!lockedUsers.contains(currentList.get(i))) {
                                unlockedUsers.add(currentList.get(i));
                            }
                            lockedUsers.remove(currentList.get(i));
                            userViewHolder.userStatus.setImageResource(R.drawable.ic_check_24dp);
                            if ("UNLOCKED".equals(mode)) {
                                dialog.dismiss();
                                notifyDataSetChanged();
                            }
                        } catch (ParseException e) {
                            Log.d("e", String.valueOf(e));
                        }
                        lockSwitch.setVisibility(View.GONE);
                    }
                });
                if (!user.isLocked()) {
                    lockSwitch.setVisibility(View.GONE);
                } else {
                    lockSwitch.setVisibility(View.VISIBLE);
                }
                dialog.show();
            }
        });
    }

    /**
     * ViewHolder Class following the ViewHolder Android Pattern. Establishes views held inside
     * the movie cards that this adapter sets.
     */
    public static class UserViewHolder extends RecyclerView.ViewHolder {
        //        private CardView cv;
        /**
         * Rel Layout
         */
        private RelativeLayout cvLayout;
        /**
         * Username field
         */
        private TextView username;
        /**
         * User status field
         */
        private ImageView userStatus;
        /**
         * User's photo
         */
        private CircleImageView profPhoto;

        /**
         * view
         * @param itemView view
         */
        UserViewHolder(View itemView) {
            super(itemView);
            //cv = (CardView) itemView.findViewById(R.id.userCardView);
            cvLayout = (RelativeLayout) itemView.findViewById(R.id.cv_layout);
            username = (TextView) itemView.findViewById(R.id.user_name);
            userStatus = (ImageView) itemView.findViewById(R.id.status_image);
            profPhoto = (CircleImageView) itemView.findViewById(R.id.profile_image);
        }
    }

    public void setMode(String mode) {
        this.mode = mode;
    }
}
