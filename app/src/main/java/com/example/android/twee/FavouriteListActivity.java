package com.example.android.twee;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.models.User;
import com.twitter.sdk.android.tweetui.SearchTimeline;

import io.fabric.sdk.android.Fabric;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class FavouriteListActivity extends AppCompatActivity {

    private TextView mSearchText;
    private Button mSearchFavouritesButton;
    private ListView mListView;
    private FavouriteListAdapter mAdapter;
    private TextView mEmptyStateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, Twitter.getInstance());

        setContentView(R.layout.activity_favourite_list);

        mListView = (ListView) findViewById(R.id.fav_list);
        mEmptyStateTextView = (TextView) findViewById(R.id.fav_empty_view);
        mEmptyStateTextView.setText("No favourites to display");
        mListView.setEmptyView(mEmptyStateTextView);
        mAdapter = new FavouriteListAdapter(this,0);
        Realm.init(this);
        Realm realm = Realm.getDefaultInstance();
        RealmResults<FavouriteUser> favouriteUsers = realm.where(FavouriteUser.class).findAll();
        Log.d(Utils.LOG_TAG, new Integer(favouriteUsers.size()).toString());
        mAdapter.addAll(favouriteUsers);
        favouriteUsers.addChangeListener(new RealmChangeListener<RealmResults<FavouriteUser>>() {
            @Override
            public void onChange(RealmResults<FavouriteUser> element) {
                mAdapter.clear();
                mAdapter.addAll(element);
            }
        });
        mListView.setAdapter(mAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FavouriteUser user = (FavouriteUser) parent.getAdapter().getItem(position);
                Toast.makeText(FavouriteListActivity.this, user.getScreenName().toString(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(FavouriteListActivity.this, UserDetail.class);
                intent.putExtra("screen_name", user.getScreenName());
                intent.putExtra("name", user.getUserName());
                intent.putExtra("profile_image_url", user.getProfileImageUrl());
                startActivity(intent);
            }
        });
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
        else if (item.getItemId() == R.id.search_opt){
            Intent searchIntent = new Intent(this, SearchActivity.class);
            startActivity(searchIntent);
            return true;
        }
        else if (item.getItemId() == R.id.favourites_opt){
            Intent favouriteIntent = new Intent(this, FavouriteListActivity.class);
            startActivity(favouriteIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
