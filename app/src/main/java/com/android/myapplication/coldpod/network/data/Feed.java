package com.android.myapplication.coldpod.network.data;

import com.android.myapplication.coldpod.network.data.Podcasts;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Feed {

    @SerializedName("title")
    private String mTitle;

    @SerializedName("country")
    private String mCountry;

    @SerializedName("results")
    private List<Podcasts> mPodcasts;

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getCountry() {
        return mCountry;
    }

    public void setCountry(String country) {
        mCountry = country;
    }

    public List<Podcasts> getPodCasts() {
        return mPodcasts;
    }

    public void setResults(List<Podcasts> podcasts) {
        mPodcasts = podcasts;
    }
}
