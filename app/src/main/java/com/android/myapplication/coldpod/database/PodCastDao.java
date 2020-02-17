package com.android.myapplication.coldpod.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

@Dao
public interface PodCastDao {
    @Query("SELECT * FROM podcast")
    LiveData<List<PodcastEntry>> loadPodcasts();
}
