package com.android.myapplication.coldpod.persistence;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.android.myapplication.coldpod.persistence.PodcastEntry;

import java.util.List;

@Dao
public interface PodCastDao {
    @Query("SELECT * FROM podcast")
    LiveData<List<PodcastEntry>> loadPodcasts();


    @Query("SELECT * FROM podcast WHERE podcast_id = :podcastId")
    LiveData<PodcastEntry> loadPodcastByPodcastId(String podcastId);

    @Insert
    void insertPodcast(PodcastEntry podcastEntry);

    @Delete
    void deletePodcast(PodcastEntry podcastEntry);
}
