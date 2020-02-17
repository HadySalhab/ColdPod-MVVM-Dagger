package com.android.myapplication.coldpod.ui.subscribe;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.android.myapplication.coldpod.model.Podcasts;
import com.android.myapplication.coldpod.repository.PodCastsRepository;
import com.android.myapplication.coldpod.repository.SubscribeRepository;
import com.android.myapplication.coldpod.utils.Constants;
import com.android.myapplication.coldpod.utils.Resource;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

public class SubscribeViewModel extends ViewModel {
    private static final String TAG = "SubscribeViewModel";
    private final SubscribeRepository repository;
    private final String podCastId;
    private final MediatorLiveData<Resource<String>> _feedUrl = new MediatorLiveData<>();

    public final LiveData<Resource<String>> getFeedUrl() {
        return _feedUrl;

    }

    @Inject
    public SubscribeViewModel(SubscribeRepository repository, @Named("podCast_Id") String podCastId) {
        this.repository = repository;
        this.podCastId = podCastId;
        getLookup();
    }

    private void getLookup() {
        Log.d(TAG, "getLookup: " + this.podCastId);
        LiveData<Resource<String>> repositorySource = repository.getLookUp(Constants.I_TUNES_LOOKUP, podCastId);
        registerMediatorLiveData(repositorySource);
    }

    private void registerMediatorLiveData(LiveData<Resource<String>> repositorySource) {
        _feedUrl.addSource(repositorySource, new Observer<Resource<String>>() {
            @Override
            public void onChanged(Resource<String> resourceFeedUrl) {
                Log.d(TAG, "onChanged: ");
                if (resourceFeedUrl != null) {
                    _feedUrl.setValue(resourceFeedUrl);
                    //we know that fetching is finished
                    if (resourceFeedUrl.status == Resource.Status.SUCCESS || resourceFeedUrl.status == Resource.Status.ERROR) {
                        Log.d(TAG, "onChanged: "+resourceFeedUrl.data);
                        unregisterMediatorLiveData(repositorySource);

                    }
                } else {
                    unregisterMediatorLiveData(repositorySource);

                }
            }
        });
    }

    private void unregisterMediatorLiveData(LiveData<Resource<String>> repositorySource) {
        _feedUrl.removeSource(repositorySource);
    }
}
