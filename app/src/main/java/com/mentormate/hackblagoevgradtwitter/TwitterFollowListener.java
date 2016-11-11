package com.mentormate.hackblagoevgradtwitter;

import java.util.List;

public interface TwitterFollowListener {
    public List<TwitterUser> onUsersReceived();
    public void onError();
}
