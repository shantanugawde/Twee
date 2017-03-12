package com.example.android.twee;

import com.twitter.sdk.android.core.models.Tweet;

/**
 * Created by Shantanu on 07-03-2017.
 */

public class TweetEmotion {
    private Double anger;
    private Double fear;
    private Double surprise;
    private Double joy;
    private Double sadness;
    private String tweetText;
    private Long tweetId;

    @Override
    public String toString() {
        return getTweetText() + " " + getAnger() + " " + getFear() + " " + getJoy() + " " + getSadness() + " " + getSurprise();
    }

    public TweetEmotion(Double anger, Double fear, Double surprise, Double joy, Double sadness, String tweetText, Long tweetId) {
        this.anger = anger;
        this.fear = fear;
        this.surprise = surprise;
        this.joy = joy;
        this.sadness = sadness;
        this.tweetText = tweetText;
        this.tweetId = tweetId;
    }

    public Long getTweetId() {  return tweetId;   }

    public Double getAnger() {
        return anger;
    }

    public Double getFear() {
        return fear;
    }

    public Double getSurprise() {
        return surprise;
    }

    public Double getJoy() {
        return joy;
    }

    public Double getSadness() {
        return sadness;
    }

    public String getTweetText() {
        return tweetText;
    }
}
