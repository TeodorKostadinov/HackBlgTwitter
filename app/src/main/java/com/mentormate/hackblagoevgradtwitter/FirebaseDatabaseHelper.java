package com.mentormate.hackblagoevgradtwitter;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class FirebaseDatabaseHelper implements DatabaseHelper {

    private static FirebaseDatabaseHelper instance;
    private final FirebaseDatabase database;
    private final DatabaseReference dbUsers;
    private DatabaseReference dbThisUser;

    public static FirebaseDatabaseHelper getInstance() {
        if (instance == null) {
            instance = new FirebaseDatabaseHelper();
        }
        return instance;
    }

    private FirebaseDatabaseHelper() {
        database = FirebaseDatabase.getInstance();
        dbUsers = database.getReference("users");
        loadUser(FirebaseAuth.getInstance().getCurrentUser().getEmail());
    }

    private void loadUser(final String email) {
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
                    dbThisUser.setValue(new TwitterUser(
                            FirebaseAuth.getInstance().getCurrentUser().getEmail(),
                            FirebaseAuth.getInstance().getCurrentUser().getDisplayName()));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void getLastTweets(final TwitterUser user, int count, final TwitterPostListener listener) {
        if (dbThisUser != null) {
            dbThisUser.child("tweets").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    List<TwitterPost> tweets = new ArrayList<TwitterPost>();
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        tweets.add(data.getValue(TwitterPost.class));
                    }
                    listener.onPostsReceived(user, tweets);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    listener.onError();
                }
            });
        }
    }

    @Override
    public void postTweet(TwitterPost post) {
        dbThisUser.child("tweets").push().setValue(post);
    }

        public StorageReference getImageStorage(String imageName) {
        return FirebaseStorage.getInstance().getReference().child("images/" + imageName);
    }
}
