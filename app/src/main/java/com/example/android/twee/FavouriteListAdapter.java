package com.example.android.twee;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.twitter.sdk.android.core.models.User;

import io.realm.Realm;

/**
 * Created by Shantanu on 28-03-2017.
 */

public class FavouriteListAdapter extends ArrayAdapter<FavouriteUser> {
    //public UserListAdapter(Activity context, List<User> users){
    //    super(context, 0, users);
    //}=bb
    Realm realm;

    public FavouriteListAdapter(@NonNull Context context, @LayoutRes int resource) {
        super(context, resource);
        Realm.init(context);
        realm = Realm.getDefaultInstance();
    }

    @NonNull
    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.user_search_item, parent, false);
        }

        final FavouriteUser currentUser = getItem(position);

        TextView userNameTextView = (TextView) listItemView.findViewById(R.id.user_name);

        userNameTextView.setText(currentUser.getUserName());

        TextView screenNameTextView = (TextView) listItemView.findViewById(R.id.screen_name);
        screenNameTextView.setText("@" + currentUser.getScreenName());
        ImageView imageView = (ImageView) listItemView.findViewById(R.id.profile_img);
        LikeButton lb = (LikeButton) listItemView.findViewById(R.id.star_button);

        lb.setLiked(true);

        lb.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                Utils.setUserFavourite(getContext(), currentUser.getScreenName(), currentUser.getUserName(), currentUser.getProfileImageUrl());
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                Utils.removeUserFavourite(getContext(), currentUser.getScreenName());
            }
        });

        Glide.with(imageView.getContext())
                .load(currentUser.getProfileImageUrl())
                .into(imageView);
        return listItemView;
    }

}

