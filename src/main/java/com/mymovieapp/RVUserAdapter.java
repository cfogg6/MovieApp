package com.mymovieapp;

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
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.InputStream;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Angelo on 3/28/2016.
 */
public class RVUserAdapter extends RecyclerView.Adapter<RVUserAdapter.UserViewHolder> {
    List<AdminUser> users;

    public RVUserAdapter(List<AdminUser> users) {
        this.users = users;
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

    @Override
    public int getItemCount() {
        return users.size();
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.user_card, viewGroup, false);
        UserViewHolder uvh = new UserViewHolder(v);
        return uvh;
    }

    @Override
    public void onBindViewHolder(UserViewHolder userViewHolder, int i) {
        userViewHolder.username.setText(users.get(i).getName());
        userViewHolder.userStatus.setImageResource(R.drawable.ic_check_24dp);
        userViewHolder.profPhoto.setImageResource(R.mipmap.bucket);

        userViewHolder.cvLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
