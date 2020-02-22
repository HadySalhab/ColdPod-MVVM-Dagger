package com.android.myapplication.coldpod.ui.playing;

import android.util.Log;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.android.myapplication.coldpod.persistence.FavoriteEntry;
import com.android.myapplication.coldpod.repository.MainRepository;

import javax.inject.Inject;

public class PlayingViewModel extends ViewModel {
    private final MainRepository mMainRepository;


    private static final String TAG = "PlayingViewModel";

     private LiveData<FavoriteEntry> mFavoriteEntryLiveData;
    public LiveData<FavoriteEntry> getFavoriteEntry() {
        return mFavoriteEntryLiveData;
    }



    @Inject
    public PlayingViewModel(MainRepository mainRepository) {
        mMainRepository = mainRepository;
    }
    public void setItemTitle(String itemTitle){
        executeQuery(itemTitle);
    }

    private void executeQuery(String itemTitle){
     mFavoriteEntryLiveData = mMainRepository.getFavoriteEpisodeByItemTitle(itemTitle);
    }

    public void updateFavorite(FavoriteEntry fav){
        Log.d(TAG, "updateFavorite: "+fav.getTitle());

        if(mFavoriteEntryLiveData.getValue()!=null){
            Log.d(TAG, "updateFavorite: delete");
            mMainRepository.deletFavorite(fav);
        }else{
            Log.d(TAG, "updateFavorite: insert");
           mMainRepository.insertFavoriteEpisode(fav);
        }


    }


}
