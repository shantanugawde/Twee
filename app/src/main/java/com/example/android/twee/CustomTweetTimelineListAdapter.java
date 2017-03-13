package com.example.android.twee;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.Timeline;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;

/**
 * Created by Shantanu on 13-03-2017.
 */

public class CustomTweetTimelineListAdapter extends TweetTimelineListAdapter {

    private Context myContext;

    public CustomTweetTimelineListAdapter(Context context, Timeline<Tweet> timeline) {
        super(context, timeline);
        this.myContext = context;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);

        //disable subviews to avoid links are clickable
        if(view instanceof ViewGroup){
            disableViewAndSubViews((ViewGroup) view);
        }

        //enable root view and attach custom listener
        view.setEnabled(true);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: implement your custom behavior when clicks
                Tweet temp = getItem(position);
                Intent intent = new Intent(myContext, TweetDetail.class);
                intent.putExtra("selectedTweet", temp.text);
                intent.putExtra("selectedId", temp.id);
                Toast.makeText(myContext, new Long(temp.getId()).toString(), Toast.LENGTH_SHORT).show();
                myContext.startActivity(intent);
            }
        });
        return view;
    }

    private void disableViewAndSubViews(ViewGroup layout) {
        layout.setEnabled(false);
        for (int i = 0; i < layout.getChildCount(); i++) {
            View child = layout.getChildAt(i);
            if (child instanceof ViewGroup) {
                disableViewAndSubViews((ViewGroup) child);
            } else {
                child.setEnabled(false);
                child.setClickable(false);
                child.setLongClickable(false);
            }
        }
    }

}