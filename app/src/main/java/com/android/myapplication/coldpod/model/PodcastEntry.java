package com.android.myapplication.coldpod.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "podcast")
public class PodcastEntry {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "podcast_id")
    private int podcastId;

    private String title;

    private String description;

    private String author;

    /**
     * Constructor
     *
     * @param podcastId
     * @param title
     * @param description
     * @param author
     */
    @Ignore
    public PodcastEntry(int podcastId, String title, String description, String author) {
        this.podcastId = podcastId;
        this.title = title;
        this.description = description;
        this.author = author;
    }

    public PodcastEntry(int id, int podcastId, String title, String description, String author) {
        this.id = id;
        this.podcastId = podcastId;
        this.title = title;
        this.description = description;
        this.author = author;
    }

    public int getId() {
        return id;
    }

    public int getPodcastId() {
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
}
