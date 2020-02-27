package com.android.myapplication.coldpod.ui.playing;

import android.app.Application;
import android.util.Log;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.android.myapplication.coldpod.R;
import com.android.myapplication.coldpod.persistence.FavoriteEntry;
import com.android.myapplication.coldpod.repository.MainRepository;

import javax.inject.Inject;

public class PlayingViewModel extends ViewModel {
    private final MainRepository mMainRepository;


    private final Application mApp;
    private static final String TAG = "PlayingViewModel";

     private LiveData<FavoriteEntry> mFavoriteEntryLiveData;
    public LiveData<FavoriteEntry> getFavoriteEntry() {
        return mFavoriteEntryLiveData;
    }

    private final MutableLiveData<String> toast = new MutableLiveData<>();
    final LiveData<String> getToastMessage(){
        return toast;
    }


    @Inject
    public PlayingViewModel(MainRepository mainRepository, Application app) {
        mMainRepository = mainRepository;
        mApp = app;
        toast.setValue("");

    }
    public void setEnclosureUrl(String enclosureUrl){
        executeQuery(enclosureUrl);
    }

    private void executeQuery(String enclosureUrl){
     mFavoriteEntryLiveData = mMainRepository.getFavByEnclosureUrl(enclosureUrl);
    }

    public void updateFavorite(FavoriteEntry fav){
        Log.d(TAG, "updateFavorite: "+fav.getTitle());

        if(mFavoriteEntryLiveData.getValue()!=null){
            Log.d(TAG, "updateFavorite: delete");
            mMainRepository.deletFavorite(fav);
            toast.setValue(mApp.getString(R.string.toast_removed_fav));
        }else{
            Log.d(TAG, "updateFavorite: insert");
           mMainRepository.insertFavoriteEpisode(fav);
            toast.setValue(mApp.getString(R.string.toast_fav));
        }


    }
    public void resetToast(){
        toast.setValue("");
    }



}
