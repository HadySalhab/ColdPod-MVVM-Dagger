package com.android.myapplication.coldpod.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {PodcastEntry.class}, version = 1, exportSchema = true)
public abstract class AppDatabase extends RoomDatabase {
   public abstract PodCastDao getPodCastDao();
}
