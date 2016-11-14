package com.mentormate.hackblagoevgradtwitter;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Date;

public class TwitterPost {
    public String message;
    public long timestamp;
    public String picUrl;
    public String sender;

    public TwitterPost() {
    }

    public TwitterPost(String message, long timestamp) {
        this.message = message;
        this.timestamp = timestamp;
    }

    public TwitterPost(String message) {
        this.message = message;
        this.timestamp = new Date().getTime();
        this.sender = FirebaseAuth.getInstance().getCurrentUser().getEmail();
    }

    public TwitterPost(String message, String picUrl) {
        this(message);
        this.picUrl = picUrl;
    }
}
