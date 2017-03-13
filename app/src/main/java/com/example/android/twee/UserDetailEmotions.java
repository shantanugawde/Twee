package com.example.android.twee;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

import static java.lang.Math.round;


public class UserDetailEmotions extends Fragment {
    private PieChart pieChart;
    private UserEmotion userEmotion;

    public UserDetailEmotions() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_user_detail_emotions, container, false);
        pieChart = (PieChart) rootView.findViewById(R.id.userChart);
        Utils.setUpPieChart(pieChart);
        userEmotion = new UserEmotion(getActivity().getIntent().getStringExtra("screen_name"));
        setUpUserEmotions();
        return rootView;
    }

    private void setUpUserEmotions() {

        final MyTwitterApiClient apiClient = new MyTwitterApiClient(Twitter.getInstance().getSessionManager().getActiveSession());

        final Call<List<Tweet>> myTweetsCall = apiClient.getUserTweetsService().show(getActivity().getIntent().getStringExtra("screen_name"), 5);
        myTweetsCall.enqueue(new Callback<List<Tweet>>() {
            @Override
            public void success(Result<List<Tweet>> result) {
                for (Tweet tw : result.data) {
                    userEmotion.addTweet(tw);
                    Log.d(Utils.LOG_TAG, new Integer(userEmotion.getRecentTweets().size()).toString());
                }

                final TweetAsyncTask myTweetAsync = new TweetAsyncTask();
                myTweetAsync.execute();
            }

            @Override
            public void failure(TwitterException exception) {

            }
        });
    }


    private class TweetAsyncTask extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            Utils.setUserEmotions(userEmotion);
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            updateUI();
        }
    }

    private void updateUI() {
        List<PieEntry> pieEntries = new ArrayList<PieEntry>();
        pieEntries.add(new PieEntry(round(userEmotion.getAnger().floatValue() * 100), "Anger"));
        pieEntries.add(new PieEntry(round(userEmotion.getSurprise().floatValue() * 100), "Surprise"));
        pieEntries.add(new PieEntry(round(userEmotion.getFear().floatValue() * 100), "Fear"));
        pieEntries.add(new PieEntry(round(userEmotion.getJoy().floatValue() * 100), "Joy"));
        pieEntries.add(new PieEntry(round(userEmotion.getSadness().floatValue() * 100), "Sadness"));

        PieDataSet pieDataSet = new PieDataSet(pieEntries, "Emotions");

        ArrayList<Integer> colors = new ArrayList<>();
        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        pieDataSet.setColors(colors);
        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);

        pieChart.invalidate();
        pieChart.setRotationEnabled(false);
        pieChart.setTouchEnabled(false);
    }
}
