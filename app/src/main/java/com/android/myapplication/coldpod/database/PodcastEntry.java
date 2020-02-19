package com.android.myapplication.coldpod.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity(tableName = "podcast")
public class PodcastEntry {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "podcast_id")
    private String podcastId;

    private String title;

    private String description;

    private String author;

    @ColumnInfo(name = "artwork_image_url")
    private String artworkImageUrl;

    /**
     * Constructor
     *
     * @param podcastId
     * @param title
     * @param description
     * @param author
     */
    @Ignore
    public PodcastEntry(String podcastId, String title, String description, String author,String artworkImageUrl) {
        this.podcastId = podcastId;
        this.title = title;
        this.description = description;
        this.author = author;
        this.artworkImageUrl = artworkImageUrl;
    }

    public PodcastEntry(int id, String podcastId, String title, String description, String author, String artworkImageUrl) {
        this.id = id;
        this.podcastId = podcastId;
        this.title = title;
        this.description = description;
        this.author = author;
        this.artworkImageUrl = artworkImageUrl;
    }

    public int getId() {
        return id;
    }

    public String getPodcastId() {
        return podcastId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getAuthor() {
        return author;
    }

    public String getArtworkImageUrl() {
        return artworkImageUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PodcastEntry that = (PodcastEntry) o;
        return id == that.id &&
                Objects.equals(podcastId, that.podcastId) &&
                Objects.equals(title, that.title) &&
                Objects.equals(description, that.description) &&
                Objects.equals(author, that.author) &&
                Objects.equals(artworkImageUrl, that.artworkImageUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, podcastId, title, description, author, artworkImageUrl);
    }
}
