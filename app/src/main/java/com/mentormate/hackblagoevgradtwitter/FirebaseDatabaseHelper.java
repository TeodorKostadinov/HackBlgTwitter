package com.mentormate.hackblagoevgradtwitter;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class FirebaseDatabaseHelper {

    private static FirebaseDatabaseHelper instance;
    private final FirebaseDatabase database;
    private final DatabaseReference dbTweets;

    public static FirebaseDatabaseHelper getInstance() {
        if (instance == null) {
            instance = new FirebaseDatabaseHelper();
        }
        return instance;
    }

    private FirebaseDatabaseHelper() {
        database = FirebaseDatabase.getInstance();
        dbTweets = database.getReference("tweets");
    }

    public void getLastTweets(int count, final TwitterPostListener listener) {
        dbTweets.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<TwitterPost> tweets = new ArrayList<TwitterPost>();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    tweets.add(data.getValue(TwitterPost.class));
                }
                listener.onPostsReceived(tweets);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onError();
            }
        });
    }

    public void postTweet(TwitterPost post) {
        dbTweets.push().setValue(post);
    }

    public StorageReference getImageStorage(String imageName) {
        return FirebaseStorage.getInstance().getReference().child("images/" + imageName);
    }
}
