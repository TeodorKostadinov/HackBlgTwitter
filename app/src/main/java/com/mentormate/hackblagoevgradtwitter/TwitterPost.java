package com.mentormate.hackblagoevgradtwitter;

import java.util.Date;

public class TwitterPost {
    public String message;
    public long timestamp;
    public String picUrl;
    public String sender;

    public TwitterPost(String message, long timestamp) {
        this.message = message;
        this.timestamp = timestamp;
    }

    public TwitterPost(String message) {
        this.message = message;
        this.timestamp = new Date().getTime();
        this.sender = UserPrefs.getUserEmail();
    }
}
