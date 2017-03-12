package com.example.android.twee;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.CompactTweetView;
import com.twitter.sdk.android.tweetui.TweetUtils;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.round;

public class TweetDetail extends AppCompatActivity {
    private PieChart pieChart;
    private LinearLayout rootLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tweet_detail);
        Intent intent = getIntent();

        pieChart = (PieChart) findViewById(R.id.chart);
        rootLayout = (LinearLayout) findViewById(R.id.tweet_detail_layout);
        setUpPieChart(pieChart);
        TweetAsyncTask myTweetAsync = new TweetAsyncTask();
        myTweetAsync.execute(intent.getStringExtra("selectedTweet"), new Long(intent.getLongExtra("selectedId",0L)).toString());
        
    }

    private class TweetAsyncTask extends AsyncTask<String, Void, TweetEmotion> {
        @Override
        protected TweetEmotion doInBackground(String... search) {
            Log.e("DebugHelp", "Start");

            TweetEmotion tweetEmotion = Utils.getTweetEmotion(search[0], Long.parseLong(search[1]));
            return tweetEmotion;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            View progressIndicator = findViewById(R.id.loading_indicator);
            progressIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(TweetEmotion resp) {
            updateUI(resp);
        }
    }

    private void updateUI(TweetEmotion tweetEmotion) {
        View progressIndicator = findViewById(R.id.loading_indicator);
        progressIndicator.setVisibility(View.GONE);

        TweetUtils.loadTweet(tweetEmotion.getTweetId(), new Callback<Tweet>() {
            @Override
            public void success(Result<Tweet> result) {
                rootLayout.addView(new CompactTweetView(TweetDetail.this, result.data),0);
            }

            @Override
            public void failure(TwitterException exception) {
                exception.printStackTrace();
            }
        });

        List<PieEntry> pieEntries = new ArrayList<PieEntry>();
        pieEntries.add(new PieEntry(round(tweetEmotion.getAnger().floatValue() * 100), "Anger"));
        pieEntries.add(new PieEntry(round(tweetEmotion.getSurprise().floatValue() * 100), "Surprise"));
        pieEntries.add(new PieEntry(round(tweetEmotion.getFear().floatValue() * 100), "Fear"));
        pieEntries.add(new PieEntry(round(tweetEmotion.getJoy().floatValue() * 100), "Joy"));
        pieEntries.add(new PieEntry(round(tweetEmotion.getSadness().floatValue() * 100), "Sadness"));

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

////        colors.add(ColorTemplate.getHoloBlue());
//        colors.add(R.color.md_deep_orange_A700);
//
//        colors.add(R.color.md_purple_A700);
//
//        colors.add(R.color.md_green_900);
//
//        colors.add(R.color.md_amber_700);
//
//        colors.add(R.color.md_indigo_A400);
//
        pieDataSet.setColors(colors);
        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.invalidate();
    }
    private void setUpPieChart(PieChart pieChart) {
        pieChart.setUsePercentValues(true);
        Description description = new Description();
        description.setText("Emotions of the selected tweet");
        pieChart.setDescription(description);
        // enable rotation of the chart by touch
        pieChart.setExtraOffsets(5, 10, 5, 5);

        pieChart.setDragDecelerationFrictionCoef(0.95f);

        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.WHITE);

        pieChart.setTransparentCircleColor(Color.WHITE);
        pieChart.setTransparentCircleAlpha(110);

        pieChart.setHoleRadius(58f);
        pieChart.setTransparentCircleRadius(61f);

        pieChart.setDrawCenterText(true);

        pieChart.setRotationAngle(0);
        // enable rotation of the chart by touch
        pieChart.setRotationEnabled(true);
        pieChart.setHighlightPerTapEnabled(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.log_out) {
            SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove("userName");
            editor.remove("userId");
            editor.remove("token");
            editor.remove("secret");
            editor.commit();
            Twitter.getSessionManager().clearActiveSession();
            Intent mainIntent = new Intent(this, MainActivity.class);
            startActivity(mainIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
