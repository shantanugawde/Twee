package com.example.android.twee;

import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.models.User;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Query;

class MyTwitterApiClient extends TwitterApiClient {
    public MyTwitterApiClient(TwitterSession session) {
        super(session);
    }

    /**
     * Provide UserSearchService with defined endpoints
     */
    public UserSearchService getCustomService() {
        return getService(UserSearchService.class);
    }
}

//// example users/show service endpoint

// example users/show service endpoint
interface UserSearchService {
    @GET("/1.1/users/search.json")
    Call<List<User>> show(@HeaderMap Map<String, String> headers, @Query("q") String screen_name);
}