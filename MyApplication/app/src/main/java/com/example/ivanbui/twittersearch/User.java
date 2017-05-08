package com.example.ivanbui.twittersearch;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ivanbui on 2017-05-03.
 */

public class User {
    @SerializedName("nameTextView")
    private String name;
    @SerializedName("screen_name")
    private String screenName;
    @SerializedName("profile_image_url")
    private String profileImageUrl;

    public String getName() {
        return name;
    }
    public String getScreenName() {
        return screenName;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }
}