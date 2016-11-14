package com.mentormate.hackblagoevgradtwitter;

public interface DatabaseHelper {
    public void getLastTweets(TwitterUser user, int count, TwitterPostListener listener);
    public void postTweet(TwitterPost post);
}

