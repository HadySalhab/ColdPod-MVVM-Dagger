package com.android.myapplication.coldpod.ui.main;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

public class MainActivityViewModel extends ViewModel {
    private final MutableLiveData<Boolean> _navToPodcastsActivity = new MutableLiveData<Boolean>();

    LiveData<Boolean> getNavToPodcastsActivity() {
        return _navToPodcastsActivity;
    }

    private final MutableLiveData<Boolean> _isFabVisible = new MutableLiveData<Boolean>();

    public LiveData<Boolean> isFabVisible() {
        return _isFabVisible;
    }

    @Inject
    public MainActivityViewModel() {
        _navToPodcastsActivity.setValue(false);
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
