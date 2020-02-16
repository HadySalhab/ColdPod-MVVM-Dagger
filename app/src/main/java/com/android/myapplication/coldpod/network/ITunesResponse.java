package com.android.myapplication.coldpod.network;

import com.google.gson.annotations.SerializedName;

public class ITunesResponse {

    @SerializedName("feed")
    private Feed mFeed;

    public Feed getFeed() {
        return mFeed;
    }

    public void setFeed(Feed feed) {
        mFeed = feed;
    }
}
