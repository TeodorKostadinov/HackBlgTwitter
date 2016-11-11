package com.mentormate.hackblagoevgradtwitter;

public interface DatabaseHelper {
    public void getAllFollowing(TwitterUser user, TwitterFollowListener listener);
    public void getAllFollowers(TwitterUser user, TwitterFollowListener listener);
    public void getLastTweets(TwitterUser user, int count, TwitterPostListener listener);
    public void postTweet(TwitterPost post);
}

