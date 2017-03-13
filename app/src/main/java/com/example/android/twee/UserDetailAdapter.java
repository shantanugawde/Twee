package com.example.android.twee;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Shantanu on 13-03-2017.
 */

public class UserDetailAdapter extends FragmentPagerAdapter {

    private Context mContext;

    public UserDetailAdapter(Context context, FragmentManager fm){
        super(fm);
        mContext = context;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if(position == 0){
            return mContext.getString(R.string.emotions);
        } else {
            return mContext.getString(R.string.search);
        }
    }

    @Override
    public Fragment getItem(int position) {
        if(position == 0){
            return new UserDetailEmotions();
        }
        else{
            return new UserDetailTweets();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}