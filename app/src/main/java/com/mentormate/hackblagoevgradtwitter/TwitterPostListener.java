package com.mentormate.hackblagoevgradtwitter;

import java.util.List;

public interface TwitterPostListener {
    public List<TwitterPost> onPostsReceived();
    public void onError();
}
