package com.mentormate.hackblagoevgradtwitter;

import android.content.Context;
import android.content.SharedPreferences;

public class UserPrefs {
    private static final String SP_USER_FILE = "sp_user";
    private static final String SP_USER_TOKEN = "sp_token";
    private static final String SP_USER_EMAIL = "sp_email";

    public static void saveUser(String useremail, String usertoken) {
        SharedPreferences.Editor editor = TwitterApplication.getContext().getSharedPreferences(SP_USER_FILE,
                Context.MODE_PRIVATE).edit();
        editor.putString(SP_USER_EMAIL, useremail);
        editor.putString(SP_USER_TOKEN, usertoken);
        editor.commit();
    }

    public static String getUserTocken() {
        return TwitterApplication.getContext().getSharedPreferences(SP_USER_FILE,
                Context.MODE_PRIVATE).getString(SP_USER_TOKEN, "");
    }

    public static String getUserEmail() {
        return TwitterApplication.getContext().getSharedPreferences(SP_USER_FILE,
                Context.MODE_PRIVATE).getString(SP_USER_EMAIL, "");
    }
}
