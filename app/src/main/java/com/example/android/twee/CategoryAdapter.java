package com.example.android.twee;

/**
 * Created by Shantanu on 09-03-2017.
 */

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Shantanu on 22-01-2017.
 */

public class CategoryAdapter extends FragmentPagerAdapter {

    private Context mContext;

    public CategoryAdapter(Context context, FragmentManager fm){
        super(fm);
        mContext = context;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if(position == 0){
            return mContext.getString(R.string.search);
        } else {
            return mContext.getString(R.string.lookup);
        }
    }

    @Override
    public Fragment getItem(int position) {
        if(position == 0){
            return new SearchFragment();
        }
        else{
            return new UserSearchFragment();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}