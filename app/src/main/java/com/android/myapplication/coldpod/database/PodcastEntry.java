package com.android.myapplication.coldpod.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.android.myapplication.coldpod.network.Item;

import java.util.Date;
import java.util.List;
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

    private List<Item> items;

    private Date date;

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
                Objects.equals(artworkImageUrl, that.artworkImageUrl) &&
                Objects.equals(items, that.items) &&
                Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, podcastId, title, description, author, artworkImageUrl, items, date);
    }

    public List<Item> getItems() {
        return items;
    }

    public Date getDate() {
        return date;
    }

    /**
     * Constructor
     *
     * @param podcastId
     * @param title
     * @param description
     * @param author
     */
    @Ignore
    public PodcastEntry(String podcastId, String title, String description, String author,String artworkImageUrl,List<Item> items, Date date) {
        this.podcastId = podcastId;
        this.title = title;
        this.description = description;
        this.author = author;
        this.artworkImageUrl = artworkImageUrl;
        this.items = items;
        this.date = date;
    }

    public PodcastEntry(int id, String podcastId, String title, String description, String author, String artworkImageUrl, List<Item> items, Date date) {
        this.id = id;
        this.podcastId = podcastId;
        this.title = title;
        this.description = description;
        this.author = author;
        this.artworkImageUrl = artworkImageUrl;
        this.items = items;
        this.date = date;
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

}
