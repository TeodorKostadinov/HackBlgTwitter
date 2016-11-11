package com.mentormate.hackblagoevgradtwitter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Bind(R.id.edt_tweet)
    EditText edtTweet;
    @Bind(R.id.rec_view)
    RecyclerView recyclerView;

    DatabaseHelper db;
    private TweetsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        db = FirebaseDatabaseHelper.getInstance();
        setupRecView();
    }

    private void setupRecView() {
        this.adapter = new TweetsAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    @OnClick(R.id.btn_tweet)
    public void onTweetClicked() {
        if(!edtTweet.getText().toString().equals("")) {
            db.postTweet(new TwitterPost(edtTweet.getText().toString()));
        }
    }

    @OnClick(R.id.btn_logout)
    public void onLogoutClicked() {
        FirebaseAuth.getInstance().signOut();
        finish();
    }
}
