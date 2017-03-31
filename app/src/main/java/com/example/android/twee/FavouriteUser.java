package com.example.android.twee;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Shantanu on 24-03-2017.
 */

public class FavouriteUser extends RealmObject {
    @PrimaryKey
    private String screenName;
    private String userName;
    private Float anger;
    private Float joy;
    private Float surprise;
    private Float sadness;
    private Float fear;
    private String profileImageUrl;

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getUserName() {
        return userName;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Float getAnger() {
        return anger;
    }

    public void setAnger(Float anger) {
        this.anger = anger;
    }

    public Float getJoy() {
        return joy;
    }

    public void setJoy(Float joy) {
        this.joy = joy;
    }

    public Float getSurprise() {
        return surprise;
    }

    public void setSurprise(Float surprise) {
        this.surprise = surprise;
    }

    public Float getSadness() {
        return sadness;
    }

    public void setSadness(Float sadness) {
        this.sadness = sadness;
    }

    public Float getFear() {
        return fear;
    }

    public void setFear(Float fear) {
        this.fear = fear;
    }

}
