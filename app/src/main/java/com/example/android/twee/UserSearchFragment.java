package com.example.android.twee;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.User;

import java.util.List;

import io.fabric.sdk.android.Fabric;
import retrofit2.Call;


public class UserSearchFragment extends Fragment {

    private TextView mSearchText;
    private Button mSearchButton;
    private ListView mListView;
    private UserListAdapter mAdapter;
    private TextView mEmptyStateTextView;

    public UserSearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Fabric.with(getActivity(), Twitter.getInstance());
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.search_fragment, container, false);

        mSearchButton = (Button) rootView.findViewById(R.id.go_btn);
        mSearchText = (TextView) rootView.findViewById(R.id.search_text);
        mListView = (ListView) rootView.findViewById(R.id.list_view);
        mEmptyStateTextView = (TextView) rootView.findViewById(R.id.empty_view);
        mEmptyStateTextView.setText("No users to display");
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
                loadUsersOnSearch();
            }
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                User user = (User) parent.getAdapter().getItem(position);
                Toast.makeText(getActivity(), user.screenName.toString(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), UserDetail.class);
                startActivity(intent);
            }
        });
        return rootView;

    }

    private void loadUsersOnSearch(){
        final MyTwitterApiClient apiClient = new MyTwitterApiClient(Twitter.getInstance().getSessionManager().getActiveSession());

        Call<List<User>> myCall = apiClient.getCustomService().show(Utils.getOauth1Header(), mSearchText.getText().toString());
        myCall.enqueue(new Callback<List<User>>() {
            @Override
            public void success(Result<List<User>> result) {
                mAdapter = new UserListAdapter(getActivity(),result.data);
                mListView.setAdapter(mAdapter);
            }

            @Override
            public void failure(TwitterException exception) {
                exception.printStackTrace();
                Log.e(Utils.LOG_TAG, exception.getMessage());
            }
        });


    }
}