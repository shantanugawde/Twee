package com.example.android.twee;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.*;
import com.twitter.sdk.android.core.identity.*;
import com.twitter.sdk.android.core.internal.oauth.OAuth1aHeaders;

import io.fabric.sdk.android.Fabric;


public class MainActivity extends AppCompatActivity {
    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.

    private static final String TWITTER_KEY = "VU9thAKm4rWvNdS90LTryku9c";
    private static final String TWITTER_SECRET = "9NEOybGYuZkOiXPVU2bXdMBaXAWG1LasLpWoAWbZRdn2D4j276";
    //private static final String TWITTER_KEY = "1UgtQmI41hSibPRkTvSUcA3DI";
    //private static final String TWITTER_SECRET = "JAC4PeXeI7wXGL3rjt6208AM2MsvKPkj7xRrkXZUw4tT4fwCGv";

    private TwitterLoginButton loginButton;
    SharedPreferences sharedPreferences;
    private TwitterSession session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(Utils.LOG_TAG, new Boolean(Utils.isLoggedIn(this)).toString());
        if(!Utils.isLoggedIn(this)) {
            loginButton = (TwitterLoginButton) findViewById(R.id.twitter_login_button);
            loginButton.setCallback(new Callback<TwitterSession>() {
                @Override
                public void success(Result<TwitterSession> result) {
                    session = result.data;
                    sharedPreferences = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putLong("userId", session.getUserId());
                    editor.putString("userName", session.getUserName());
                    editor.putString("token", session.getAuthToken().token);
                    editor.putString("secret", session.getAuthToken().secret);
                    editor.commit();
                    String msg = "@" + session.getUserName() + " logged in! (#" + session.getUserId() + ")";
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                    startActivity(intent);
                }

                @Override
                public void failure(TwitterException exception) {
                    Log.d("TwitterKit", "Login with Twitter failure", exception);
                }
            });
        }
        else{
            sharedPreferences = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
            TwitterAuthToken twitterAuthToken = new TwitterAuthToken(sharedPreferences.getString("token",""), sharedPreferences.getString("secret",""));
            session = new TwitterSession(twitterAuthToken, sharedPreferences.getLong("userId",0L), sharedPreferences.getString("userName",""));
            Twitter.getSessionManager().setActiveSession(session);
            String msg = "@" + session.getUserName() + " logged in! (#" + session.getUserId() + ")";
            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();

            Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
            startActivity(intent);
        }


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Make sure that the loginButton hears the result from any
        // Activity that it triggered.
        loginButton.onActivityResult(requestCode, resultCode, data);
    }

}