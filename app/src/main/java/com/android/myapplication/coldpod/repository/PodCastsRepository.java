package com.android.myapplication.coldpod.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.android.myapplication.coldpod.di.main.MainScope;
import com.android.myapplication.coldpod.model.Podcasts;
import com.android.myapplication.coldpod.network.ApiResponse;
import com.android.myapplication.coldpod.network.Feed;
import com.android.myapplication.coldpod.network.ITunesApi;
import com.android.myapplication.coldpod.network.ITunesResponse;
import com.android.myapplication.coldpod.utils.NetworkBoundResource;
import com.android.myapplication.coldpod.utils.Resource;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

@MainScope
public class PodCastsRepository {

    private final ITunesApi mITunesApi;


    @Inject
    public PodCastsRepository(ITunesApi iTunesApi) {
        mITunesApi = iTunesApi;
    }

   public LiveData<Resource<List<Podcasts>>> getPodCasts(String country){
        return new NetworkBoundResource<List<Podcasts>, ITunesResponse>(){
            @Override
            protected void handleApiSuccessResponse(ApiResponse.ApiSuccessResponse response) {
                ITunesResponse iTunesResponse = (ITunesResponse) response.getBody();
                Feed feed = iTunesResponse.getFeed();
                List<Podcasts> podcasts = feed.getPodCasts();
                results.setValue(new Resource<List<Podcasts>>(Resource.Status.SUCCESS,podcasts,null));
            }

            @Override
            protected void handleApiEmptyResponse(ApiResponse.ApiEmptyResponse response) {
                List<Podcasts> emptyPodCasts = new ArrayList<>();
                results.setValue(new Resource<List<Podcasts>>(Resource.Status.SUCCESS,emptyPodCasts,null));
            }

            @Override
            protected void handleApiErrorResponse(ApiResponse.ApiErrorResponse response) {
                results.setValue(new Resource<List<Podcasts>>(Resource.Status.ERROR,null,response.getErrorMessage()));

            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<ITunesResponse>> createCall() {
                return  mITunesApi.getTopPodcasts(country);
            }
        }.getAsLiveData();

    }
}
