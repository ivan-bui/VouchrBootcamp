package com.example.ivanbui.twittersearch;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ivanbui on 2017-05-03.
 */

public class Status {
    @SerializedName("id_str")
    private String idStr;
    @SerializedName("text")
    private String mText;
    @SerializedName("user")
    private User user;


    public static String name;

    public String getIdStr() {return idStr;}
    public String getText() {return mText;}
    public User getUser() {return user;}
    public void setIdStr(String idStr) {this.idStr = idStr;}
    public void setText(String mText) {this.mText = mText;}
    public void setUser(User user) {this.user = user;}
}
