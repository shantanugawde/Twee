package com.example.android.twee;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.OAuthSigning;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.internal.oauth.OAuth1aHeaders;
import com.twitter.sdk.android.core.models.Tweet;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Shantanu on 05-03-2017.
 */

public class Utils {
    final static String LOG_TAG = "Rally";
    final static String indicoSearchURL = "https://apiv2.indico.io/emotion";
    final static String indicoApiKey = "6a42dec2821a7489c22b6acd530f9789";
    static Tweet currentTweet = null;

    public static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    public static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e("My Error", "Problem building the URL ", e);
        }
        return url;
    }


    public static TweetEmotion getTweetEmotion(String tweetText, Long tweetId) {

        URL url = createUrl(indicoSearchURL);
        TweetEmotion tweetEmotion = null;
        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequestQuery(url, tweetText);
            JSONObject baseJsonResponse = new JSONObject(jsonResponse);
            JSONObject resultsJsonResponse = baseJsonResponse.getJSONObject("results");

            Double anger = resultsJsonResponse.getDouble("anger");
            Double fear = resultsJsonResponse.getDouble("fear");
            Double surprise = resultsJsonResponse.getDouble("surprise");
            Double joy = resultsJsonResponse.getDouble("joy");
            Double sadness = resultsJsonResponse.getDouble("sadness");
            tweetEmotion = new TweetEmotion(anger, fear, surprise, joy, sadness, tweetText, tweetId);
            Log.d(LOG_TAG, tweetEmotion.toString());
        } catch (IOException e) {
            Log.e("err", "Problem making the HTTP request.", e);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (TextUtils.isEmpty(jsonResponse)) {
            return null;
        }
        return tweetEmotion;
    }

    private static String makeHttpRequestQuery(URL url, String tweetText) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            urlConnection.addRequestProperty("X-ApiKey", indicoApiKey);

            OutputStreamWriter out = new OutputStreamWriter(urlConnection.getOutputStream());
            out.write("{\"data\":\"" + tweetText + "\"}");
            out.close();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);

            urlConnection.connect();
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e("My success", "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    public static Map<String, String> getOauth1Header() {
        TwitterAuthConfig authConfig = TwitterCore.getInstance().getAuthConfig();
        TwitterAuthToken authToken = Twitter.getInstance().getSessionManager().getActiveSession().getAuthToken();
        OAuthSigning oauthSigning = new OAuthSigning(authConfig, authToken);

        Map<String, String> authHeaders = oauthSigning.getOAuthEchoHeadersForVerifyCredentials();
        Map<String, String> myMap = new HashMap<>();

        myMap.put("Authorization", authHeaders.get(OAuth1aHeaders.HEADER_AUTH_CREDENTIALS));
        return myMap;
    }

    public static Boolean isLoggedIn(Activity activity){
        SharedPreferences sharedPreferences = activity.getSharedPreferences(activity.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        if(sharedPreferences.contains("userName"))
            return true;
        else
            return false;
    }
}
