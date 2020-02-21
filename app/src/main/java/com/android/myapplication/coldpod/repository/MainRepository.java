package com.android.myapplication.coldpod.repository;

import androidx.lifecycle.LiveData;

import com.android.myapplication.coldpod.persistence.PodCastDao;
import com.android.myapplication.coldpod.persistence.PodcastEntry;
import com.android.myapplication.coldpod.di.main.MainScope;
import com.android.myapplication.coldpod.utils.AppExecutors;

import java.util.List;

import javax.inject.Inject;


@MainScope
public class MainRepository {
    private final PodCastDao mPodCastDao;
    private final AppExecutors mAppExecutors;

    @Inject
    public MainRepository(PodCastDao podCastDao, AppExecutors appExecutors) {
        mPodCastDao = podCastDao;
        mAppExecutors = appExecutors;
    }

    public LiveData<List<PodcastEntry>> getPodcasts() {
        return mPodCastDao.loadPodcasts();
    }

    public LiveData<PodcastEntry> getPodCastById(String podCastId) {
        return mPodCastDao.loadPodcastByPodcastId(podCastId);
    }

    public void deletePodCastEntry(PodcastEntry podcastEntry) {
        mAppExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mPodCastDao.deletePodcast(podcastEntry);
            }
        });

    }
}
