package com.example.android.twee;

import com.twitter.sdk.android.core.models.Tweet;

import java.util.ArrayList;

/**
 * Created by Shantanu on 13-03-2017.
 */

public class UserEmotion {
    private String screenName;
    private ArrayList<Tweet> recentTweets;
    private Double anger;
    private Double sadness;
    private Double joy;
    private Double fear;
    private Double surprise;

    public ArrayList<Tweet> getRecentTweets() {
        return recentTweets;
    }

    public Double getAnger() {
        return anger;

    }

    public void setAnger(Double anger) {
        this.anger = anger;
    }

    public Double getSadness() {
        return sadness;
    }

    public void setSadness(Double sadness) {
        this.sadness = sadness;
    }

    public Double getJoy() {
        return joy;
    }

    public void setJoy(Double joy) {
        this.joy = joy;
    }

    public Double getFear() {
        return fear;
    }

    public void setFear(Double fear) {
        this.fear = fear;
    }

    public Double getSurprise() {
        return surprise;
    }

    public void setSurprise(Double surprise) {
        this.surprise = surprise;
    }

    public UserEmotion(String screenName) {
        this.screenName = screenName;
        this.recentTweets = new ArrayList<>();
    }

    public String getScreenName() {
        return screenName;
    }


    public void addTweet(Tweet tweet){
        recentTweets.add(tweet);
    }

}
