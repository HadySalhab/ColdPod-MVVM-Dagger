package com.android.myapplication.coldpod.network.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Objects;

public class Podcasts {

    @SerializedName("artistName")
    private String mArtistName;

    @SerializedName("id")
    private String mId;

    @SerializedName("releaseDate")
    private String mReleaseDate;

    @SerializedName("name")
    private String mName;

    @SerializedName("kind")
    private String mKind;

    @SerializedName("copyright")
    private String mCopyright;

    @SerializedName("artistId")
    private String mArtistId;

    @SerializedName("contentAdvisoryRating")
    private String mContentAdvisoryRating;

    @SerializedName("artistUrl")
    private String mArtistUrl;

    @SerializedName("artworkUrl100")
    private String mArtworkUrl;

    @SerializedName("genres")
    private List<Genre> mGenres;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Podcasts podcasts = (Podcasts) o;
        return Objects.equals(mArtistName, podcasts.mArtistName) &&
                Objects.equals(mId, podcasts.mId) &&
                Objects.equals(mReleaseDate, podcasts.mReleaseDate) &&
                Objects.equals(mName, podcasts.mName) &&
                Objects.equals(mKind, podcasts.mKind) &&
                Objects.equals(mCopyright, podcasts.mCopyright) &&
                Objects.equals(mArtistId, podcasts.mArtistId) &&
                Objects.equals(mContentAdvisoryRating, podcasts.mContentAdvisoryRating) &&
                Objects.equals(mArtistUrl, podcasts.mArtistUrl) &&
                Objects.equals(mArtworkUrl, podcasts.mArtworkUrl) &&
                Objects.equals(mGenres, podcasts.mGenres) &&
                Objects.equals(mUrl, podcasts.mUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mArtistName, mId, mReleaseDate, mName, mKind, mCopyright, mArtistId, mContentAdvisoryRating, mArtistUrl, mArtworkUrl, mGenres, mUrl);
    }

    @SerializedName("url")
    private String mUrl;

    public String getArtistName() {
        return mArtistName;
    }

    public void setArtistName(String artistName) {
        mArtistName = artistName;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getReleaseDate() {
        return mReleaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        mReleaseDate = releaseDate;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getKind() {
        return mKind;
    }

    public void setKind(String kind) {
        mKind = kind;
    }

    public String getCopyright() {
        return mCopyright;
    }

    public void setCopyright(String copyright) {
        mCopyright = copyright;
    }

    public String getArtistId() {
        return mArtistId;
    }

    public void setArtistId(String artistId) {
        mArtistId = artistId;
    }

    public String getContentAdvisoryRating() {
        return mContentAdvisoryRating;
    }

    public void setContentAdvisoryRating(String contentAdvisoryRating) {
        mContentAdvisoryRating = contentAdvisoryRating;
    }

    public String getArtistUrl() {
        return mArtistUrl;
    }

    public void setArtistUrl(String artistUrl) {
        mArtistUrl = artistUrl;
    }

    public String getArtworkUrl() {
        return mArtworkUrl;
    }

    public void setArtworkUrl(String artworkUrl) {
        mArtworkUrl = artworkUrl;
    }

    public List<Genre> getGenres() {
        return mGenres;
    }

    public void setGenres(List<Genre> genres) {
        mGenres = genres;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }
}
