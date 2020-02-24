package com.android.myapplication.coldpod.network.data;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class SearchResult {

    @SerializedName("feedUrl")
    private String mFeedUrl;


    /** The Podcast ID */
    @SerializedName("collectionId")
    private int mCollectionId;

    @SerializedName("artistName")
    private String mArtistName;

    /** The Podcast Name */
    @SerializedName("collectionName")
    private String mCollectionName;

    @SerializedName("artworkUrl600")
    private String mArtworkUrl600;

    public String getFeedUrl() {
        return mFeedUrl;
    }

    public void setFeedUrl(String feedUrl) {
        mFeedUrl = feedUrl;
    }

    public int getCollectionId() {
        return mCollectionId;
    }

    public void setCollectionId(int collectionId) {
        mCollectionId = collectionId;
    }

    public String getArtistName() {
        return mArtistName;
    }

    public void setArtistName(String artistName) {
        mArtistName = artistName;
    }

    public String getCollectionName() {
        return mCollectionName;
    }

    public void setCollectionName(String collectionName) {
        mCollectionName = collectionName;
    }

    public String getArtworkUrl600() {
        return mArtworkUrl600;
    }

    public void setArtworkUrl600(String artworkUrl600) {
        mArtworkUrl600 = artworkUrl600;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SearchResult that = (SearchResult) o;
        return mCollectionId == that.mCollectionId &&
                Objects.equals(mFeedUrl, that.mFeedUrl) &&
                Objects.equals(mArtistName, that.mArtistName) &&
                Objects.equals(mCollectionName, that.mCollectionName) &&
                Objects.equals(mArtworkUrl600, that.mArtworkUrl600);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mFeedUrl, mCollectionId, mArtistName, mCollectionName, mArtworkUrl600);
    }
}
