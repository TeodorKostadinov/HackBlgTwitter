package com.mentormate.hackblagoevgradtwitter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Bind(R.id.edt_tweet)
    EditText edtTweet;
    @Bind(R.id.rec_view)
    RecyclerView recyclerView;

    FirebaseDatabaseHelper db;
    private TweetsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        db = FirebaseDatabaseHelper.getInstance();
        setupRecView();
        getTweets();
    }

    private void setupRecView() {
        this.adapter = new TweetsAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void getTweets() {
        db.getLastTweets(100, twitterPostListener);
    }

    TwitterPostListener twitterPostListener = new TwitterPostListener() {
        @Override
        public void onPostsReceived(List<TwitterPost> tweets) {
            adapter.setTweets(tweets);
        }

        @Override
        public void onError() {
            Toast.makeText(MainActivity.this, "Failed to get tweets", Toast.LENGTH_SHORT).show();
        }
    };

    @OnClick(R.id.btn_tweet)
    public void onTweetClicked() {
        if (!edtTweet.getText().toString().equals("")) {
            db.postTweet(new TwitterPost(edtTweet.getText().toString()));
        }
        edtTweet.setText("");
        logButtonClicked("tweetText");
    }

    @OnClick(R.id.btn_tweet_photo)
    public void onTweetPhotoClicked() {
        startActivity(new Intent(this, TweetPhotoActivity.class));
        logButtonClicked("tweetPhoto");
    }

    @OnClick(R.id.btn_logout)
    public void onLogoutClicked() {
        FirebaseAuth.getInstance().signOut();
        logButtonClicked("logout");
        finish();
    }

    @OnClick(R.id.btn_crash)
    public void onCrashClicked() {
        throw new RuntimeException("Opa");
    }

    private void logButtonClicked(String buttonName) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, buttonName);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, buttonName);
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "button");
        FirebaseAnalytics.getInstance(this)
                .logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }
}
