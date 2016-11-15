package com.mentormate.hackblagoevgradtwitter;

import java.util.List;

public interface TwitterPostListener {
    void onPostsReceived(List<TwitterPost> tweets);
    void onError();
}
