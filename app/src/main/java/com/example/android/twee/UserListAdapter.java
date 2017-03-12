package com.example.android.twee;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.twitter.sdk.android.core.models.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shantanu on 12-03-2017.
 */

public class UserListAdapter extends ArrayAdapter<User> {
    public UserListAdapter(Activity context, List<User> users){
        super(context, 0, users);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.user_search_item, parent, false);
        }

        User currentUser = getItem(position);

        TextView userNameTextView = (TextView) listItemView.findViewById(R.id.user_name);

        userNameTextView.setText(currentUser.name);

        TextView screenNameTextView = (TextView) listItemView.findViewById(R.id.screen_name);
        screenNameTextView.setText("@"+currentUser.screenName);
        ImageView imageView = (ImageView) listItemView.findViewById(R.id.profile_img);

        Glide.with(imageView.getContext())
                .load(currentUser.profileImageUrl)
                .into(imageView);
        return listItemView;
    }
}
