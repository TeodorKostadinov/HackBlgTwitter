package com.mentormate.hackblagoevgradtwitter;

import java.util.List;

public interface TwitterPostListener {
    void onPostsReceived(TwitterUser user, List<TwitterPost> tweets);

    void onError();
}
