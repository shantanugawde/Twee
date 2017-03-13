package com.example.android.twee;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.twitter.sdk.android.tweetui.UserTimeline;


/**
 * A simple {@link Fragment} subclass.
 */
public class UserDetailTweets extends Fragment {

    private ListView mListView;
    private TextView mEmptyStateTextView;

    public UserDetailTweets() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_user_tweets, container, false);
        mListView = (ListView) rootView.findViewById(R.id.user_tweets_view);
        mEmptyStateTextView = (TextView) rootView.findViewById(R.id.empty_user_view);
        mEmptyStateTextView.setText("No tweets to display");
        mListView.setEmptyView(mEmptyStateTextView);
        setUpListView();
        return rootView;
    }

    private void setUpListView(){
        final UserTimeline userTimeline = new UserTimeline.Builder()
                .screenName(getActivity().getIntent().getStringExtra("screen_name")).includeReplies(false).includeRetweets(false)
                .build();

        final CustomTweetTimelineListAdapter adapter = new CustomTweetTimelineListAdapter(getActivity(), userTimeline);

        mListView.setAdapter(adapter);

    }
}
