package com.example.android.twee;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.SearchTimeline;
import com.twitter.sdk.android.tweetui.Timeline;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;

import io.fabric.sdk.android.Fabric;

public class SearchFragment extends Fragment {

    private TextView mSearchText;
    private Button mSearchButton;
    private ListView mListView;
    private SearchTimeline searchTimeline;
    private CustomTweetTimelineListAdapter adapter;
    private TextView mEmptyStateTextView;

    public SearchFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        Fabric.with(getActivity(), Twitter.getInstance());
        View rootView = inflater.inflate(R.layout.search_fragment, container, false);

        mSearchText = (EditText) rootView.findViewById(R.id.search_text);
        mSearchButton = (Button) rootView.findViewById(R.id.go_btn);
        mListView = (ListView) rootView.findViewById(R.id.list_view);
        mEmptyStateTextView = (TextView) rootView.findViewById(R.id.empty_view);
        mEmptyStateTextView.setText("No tweets to display");
        mListView.setEmptyView(mEmptyStateTextView);
        mSearchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mSearchText.getText().toString().isEmpty()) {
                    mSearchButton.setEnabled(false);
                } else {
                    mSearchButton.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadTweetsOnListView();
            }
        });
        return rootView;
    }


    private void loadTweetsOnListView(){
        searchTimeline = new SearchTimeline.Builder()
                .query(mSearchText.getText().toString()).languageCode("en")
                .build();
        adapter = new CustomTweetTimelineListAdapter(getActivity(), searchTimeline);
        mListView.setAdapter(adapter);

    }

    class CustomTweetTimelineListAdapter extends TweetTimelineListAdapter {

        public CustomTweetTimelineListAdapter(Context context, Timeline<Tweet> timeline) {
            super(context, timeline);
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
                    Intent intent = new Intent(getActivity(), TweetDetail.class);
                    intent.putExtra("selectedTweet", temp.text);
                    intent.putExtra("selectedId", temp.id);
                    Toast.makeText(getActivity(), new Long(temp.getId()).toString(), Toast.LENGTH_SHORT).show();
                    startActivity(intent);
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
}

