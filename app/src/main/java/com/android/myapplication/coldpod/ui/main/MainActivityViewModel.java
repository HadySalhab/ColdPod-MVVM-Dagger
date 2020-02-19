package com.android.myapplication.coldpod.ui.main;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.android.myapplication.coldpod.database.PodcastEntry;
import com.android.myapplication.coldpod.repository.MainRepository;

import java.util.List;

import javax.inject.Inject;

public class MainActivityViewModel extends ViewModel {
    private final MainRepository mMainRepository;
    private final MutableLiveData<Boolean> _navToPodcastsActivity = new MutableLiveData<Boolean>();
    private LiveData<List<PodcastEntry>> mPodcastEntries;

    public LiveData<List<PodcastEntry>> getPodcastEntries() {
        return mPodcastEntries;
    }

    LiveData<Boolean> getNavToPodcastsActivity() {
        return _navToPodcastsActivity;
    }


    private LiveData<Boolean> isListNullOrEmpty;


    public LiveData<Boolean> getIsListNullOrEmpty() {
        return isListNullOrEmpty;
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
        checkIfNullOrEmpty();
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

    public void deletePodCastEntry(PodcastEntry podcastEntry) {
        mMainRepository.deletePodCastEntry(podcastEntry);
    }

    private void checkIfNullOrEmpty() {
        isListNullOrEmpty = Transformations.map(mPodcastEntries, new Function<List<PodcastEntry>, Boolean>() {
            @Override
            public Boolean apply(List<PodcastEntry> input) {
                return input == null || input.size() == 0;
            }
        });
    }
}
