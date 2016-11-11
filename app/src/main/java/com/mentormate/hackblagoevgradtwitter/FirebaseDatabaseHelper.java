package com.mentormate.hackblagoevgradtwitter;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.value;
import static com.google.android.gms.internal.zzs.TAG;

public class FirebaseDatabaseHelper implements DatabaseHelper {

    private static FirebaseDatabaseHelper instance;
    private final FirebaseDatabase database;
    private final DatabaseReference dbUsers;
    private DatabaseReference dbThisUser;

    List<TwitterFollowListener> followingListeners = new ArrayList<>();

    public static FirebaseDatabaseHelper getInstance() {
        if (instance == null) {
            instance = new FirebaseDatabaseHelper();
        }
        return instance;
    }

    private FirebaseDatabaseHelper() {
        database = FirebaseDatabase.getInstance();
        dbUsers = database.getReference("users");
        dbUsers.addValueEventListener(allFollowingReceiver);
        if (hasUser(UserPrefs.getUserEmail())) {

        } else {
            dbThisUser = dbUsers.push();
            dbThisUser.setValue(new TwitterUser(UserPrefs.getUserEmail(), UserPrefs.getUserTocken()));
        }
    }

    private boolean hasUser(final String email) {
        dbUsers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean hasUSer = false;
                String userId = "";
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    if (child.child("email").getValue().equals(email)) {
                        hasUSer = true;
                        userId = child.getKey();
                        break;
                    }
                }
                if (hasUSer) {
                    dbThisUser = dbUsers.child(userId);
                } else {
                    dbThisUser = dbUsers.push();
                    dbThisUser.setValue(new TwitterUser(UserPrefs.getUserEmail(), UserPrefs.getUserTocken()));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return false;
    }


    @Override
    public void getAllFollowing(TwitterUser user, final TwitterFollowListener listener) {
        followingListeners.add(listener);
        dbUsers.removeEventListener(allFollowingReceiver);
        dbUsers.addValueEventListener(allFollowingReceiver);
    }

    @Override
    public void getAllFollowers(TwitterUser user, TwitterFollowListener listener) {

    }

    @Override
    public void getLastTweets(TwitterUser user, int count, TwitterPostListener listener) {

    }

    @Override
    public void postTweet(TwitterPost post) {
        dbThisUser.child("tweets").push().setValue(post);
    }

    ValueEventListener allFollowingReceiver = new ValueEventListener() {

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            Log.d(TAG, "Value is: " + value);
            updateTwitterFollowingListeners();
        }

        @Override
        public void onCancelled(DatabaseError error) {
            Log.w(TAG, "Failed to read value.", error.toException());
        }
    };

    private void updateTwitterFollowingListeners() {
        for (TwitterFollowListener listener : followingListeners) {
            listener.onUsersReceived();
        }
    }
}
