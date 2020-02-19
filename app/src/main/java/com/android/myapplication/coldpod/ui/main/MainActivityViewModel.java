package com.android.myapplication.coldpod.ui.main;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.myapplication.coldpod.database.PodcastEntry;
import com.android.myapplication.coldpod.repository.MainRepository;

import java.util.List;

import javax.inject.Inject;

public class MainActivityViewModel extends ViewModel {
    private final MainRepository mMainRepository;
    private final MutableLiveData<Boolean> _navToPodcastsActivity = new MutableLiveData<Boolean>();
    private final LiveData<List<PodcastEntry>> mPodcastEntries;

    public LiveData<List<PodcastEntry>> getPodcastEntries() {
        return mPodcastEntries;
    }

    LiveData<Boolean> getNavToPodcastsActivity() {
        return _navToPodcastsActivity;
    }

    private final MutableLiveData<Boolean> _isFabVisible = new MutableLiveData<Boolean>();

    public LiveData<Boolean> isFabVisible() {
        return _isFabVisible;
    }

    @Inject
    public MainActivityViewModel(MainRepository mainRepository) {
        _navToPodcastsActivity.setValue(false);
        mMainRepository = mainRepository;
        mPodcastEntries = mainRepository.getPodcasts();
    }

    public void onFabClicked() {
        setNavToPodcastsActivity(true);
    }

    public void setNavToPodcastsActivity(boolean value) {
        _navToPodcastsActivity.setValue(value);
    }

    void setIfFabVisible(boolean value) {
        _isFabVisible.setValue(value);
    }
}
