package com.example.ivanbui.twittersearch;

/**
 * Created by ivanbui on 2017-05-04.
 */

public class MyTweet {
    public String text;
    public String name;
    public String screenName;
    public String imgUrl;

    public static class Builder {

        private MyTweet tweet;

        public Builder() {
            tweet = new MyTweet();
        }

        public Builder text(String text) {
            tweet.text = text;
            return this;
        }
        public Builder name(String name) {
            tweet.name = name;
            return this;
        }
        public Builder screenName(String screenName) {
            tweet.screenName = "@" + screenName;
            return this;
        }
        public Builder image(String imageUrl) {
            tweet.imgUrl = imageUrl;
            return this;
        }

        public MyTweet build() {
            return tweet;
        }
    }

}
