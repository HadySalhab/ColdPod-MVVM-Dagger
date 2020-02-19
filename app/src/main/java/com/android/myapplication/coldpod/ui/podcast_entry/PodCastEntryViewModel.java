package com.android.myapplication.coldpod.ui.podcast_entry;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.android.myapplication.coldpod.database.PodcastEntry;
import com.android.myapplication.coldpod.repository.MainRepository;

import java.util.List;

import javax.inject.Inject;

public class PodCastEntryViewModel extends ViewModel {
    private final MainRepository mRepository;

    private final MutableLiveData<String> _podcastId = new MutableLiveData<String>();
    public final LiveData<PodcastEntry> dbPodcastEntry =
            Transformations.switchMap(_podcastId, new Function<String, LiveData<PodcastEntry>>() {
                @Override
                public LiveData<PodcastEntry> apply(String input) {
                    if(input!=null) {
                        return mRepository.getPodCastById(input);
                    }else{
                        return null;
                    }
                }
            });


    @Inject
    public PodCastEntryViewModel(MainRepository repository) {
        mRepository = repository;
    }


    public void setPodCastId(String podCastId){
        _podcastId.setValue(podCastId);
    }
}
