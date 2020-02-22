package com.android.myapplication.coldpod.ui.podcasts;

import android.view.View;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.android.myapplication.coldpod.network.data.Podcasts;
import com.android.myapplication.coldpod.repository.PodCastsRepository;
import com.android.myapplication.coldpod.utils.Resource;

import java.util.List;

import javax.inject.Inject;

public class PodCastListViewModel extends ViewModel {

    private final PodCastsRepository mPodCastsRepository;
    private final MediatorLiveData<Resource<List<Podcasts>>> _podcasts = new MediatorLiveData<>();
    public final LiveData<Resource<List<Podcasts>>>  getPodcasts(){
        return _podcasts;
    }
    public LiveData<Integer> progress = Transformations.map(_podcasts, new Function<Resource<List<Podcasts>>, Integer>() {
        @Override
        public Integer apply(Resource<List<Podcasts>> input) {
            if(input!=null) {
                if (input.status == Resource.Status.LOADING) {
                    return View.VISIBLE;
                }
                return View.GONE;
            }else{
                return View.GONE;
            }
        }
    });

    @Inject
    public PodCastListViewModel(PodCastsRepository podCastsRepository) {
        mPodCastsRepository = podCastsRepository;
    }

    void getPodCasts(String country){
        LiveData<Resource<List<Podcasts>>> repositorySource=mPodCastsRepository.getPodCasts(country);
        registerMediatorLiveData(repositorySource);
    }
    private void registerMediatorLiveData(LiveData<Resource<List<Podcasts>>> repositorySource){
        _podcasts.addSource(repositorySource, new Observer<Resource<List<Podcasts>>>() {
            @Override
            public void onChanged(Resource<List<Podcasts>> listResource) {
                if(listResource !=null){
                    _podcasts.setValue(listResource);
                    //we know that fetching is finished
                    if (listResource.status == Resource.Status.SUCCESS || listResource.status== Resource.Status.ERROR){
                        unregisterMediatorLiveData(repositorySource);

                    }
                }else{
                    unregisterMediatorLiveData(repositorySource);

                }
            }
        });
    }
    private void unregisterMediatorLiveData(LiveData<Resource<List<Podcasts>>> repositorySource) {
        _podcasts.removeSource(repositorySource);
    }
}
