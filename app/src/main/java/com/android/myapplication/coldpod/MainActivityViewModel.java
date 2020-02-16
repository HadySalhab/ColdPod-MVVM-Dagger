package com.android.myapplication.coldpod;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

public class MainActivityViewModel extends ViewModel {
    private final MutableLiveData<Boolean> _navToAddFragment = new MutableLiveData<Boolean>();

    LiveData<Boolean> getNavToAddFragment() {
        return _navToAddFragment;
    }

    @Inject
    public MainActivityViewModel() {
        _navToAddFragment.setValue(false);
    }

    public void onFabClicked() {
        setNavToAddFragment(true);
    }

    void setNavToAddFragment(boolean value){
        _navToAddFragment.setValue(value);
    }
}
