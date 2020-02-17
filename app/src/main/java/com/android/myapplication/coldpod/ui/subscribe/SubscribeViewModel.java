package com.android.myapplication.coldpod.ui.subscribe;

import android.util.Log;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.android.myapplication.coldpod.network.RssFeed;
import com.android.myapplication.coldpod.repository.SubscribeRepository;
import com.android.myapplication.coldpod.utils.AbsentLiveData;
import com.android.myapplication.coldpod.utils.Constants;
import com.android.myapplication.coldpod.utils.Resource;



import javax.inject.Inject;


public class SubscribeViewModel extends ViewModel {
    private static final String TAG = "SubscribeViewModel";
    private final SubscribeRepository repository;

    private final MutableLiveData<String> _podcastId = new MutableLiveData<>();
    private final LiveData<Resource<String>> feedURL = Transformations.switchMap(_podcastId, new Function<String, LiveData<Resource<String>>>() {
        @Override
        public LiveData<Resource<String>> apply(String input) {
            return repository.getLookUp(Constants.I_TUNES_LOOKUP,input);
        }
    });

    public final  LiveData<Resource<RssFeed>> rssFeed = Transformations.switchMap(feedURL, new Function<Resource<String>, LiveData<Resource<RssFeed>>>() {
        @Override
        public LiveData<Resource<RssFeed>> apply(Resource<String> input) {
            if(input!=null && input.status== Resource.Status.SUCCESS && input.data!=null){
                Log.d(TAG, "apply: " + input.data);
               return repository.getRssfeed(input.data);
            }
            return AbsentLiveData.create();
        }
    });

    @Inject
    public SubscribeViewModel(SubscribeRepository repository) {
        this.repository = repository;
    }



   public void setPodCastId(String podCastId){
       Log.d(TAG, "setPodCastId: " + podCastId);
        _podcastId.setValue(podCastId);
   }
}
