package com.android.myapplication.coldpod.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.android.myapplication.coldpod.di.main.MainScope;
import com.android.myapplication.coldpod.model.Podcasts;
import com.android.myapplication.coldpod.network.ApiResponse;
import com.android.myapplication.coldpod.network.Feed;
import com.android.myapplication.coldpod.network.ITunesApi;
import com.android.myapplication.coldpod.network.ITunesResponse;
import com.android.myapplication.coldpod.network.LookupResponse;
import com.android.myapplication.coldpod.network.LookupResult;
import com.android.myapplication.coldpod.utils.NetworkBoundResource;
import com.android.myapplication.coldpod.utils.Resource;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

@MainScope
public class SubscribeRepository {

    private final ITunesApi mITunesApi;


    @Inject
    public SubscribeRepository(ITunesApi iTunesApi) {
        mITunesApi = iTunesApi;
    }

    public LiveData<Resource<String>> getLookUp(String url,String podcastId){
        return new NetworkBoundResource<String, LookupResponse>(){
            @Override
            protected void handleApiSuccessResponse(ApiResponse.ApiSuccessResponse response) {
                LookupResponse lookupResponse = (LookupResponse) response.getBody();
                List<LookupResult> lookupResults = lookupResponse.getLookupResults();
                LookupResult lookupResult = lookupResults.get(0);
                String feeUrl = lookupResult.getFeedUrl();
                results.setValue(new Resource<String>(Resource.Status.SUCCESS,feeUrl,null));

            }

            @Override
            protected void handleApiEmptyResponse(ApiResponse.ApiEmptyResponse response) {
                results.setValue(new Resource<String>(Resource.Status.SUCCESS,null,null));

            }

            @Override
            protected void handleApiErrorResponse(ApiResponse.ApiErrorResponse response) {
                results.setValue(new Resource<String>(Resource.Status.ERROR,null,response.getErrorMessage()));
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<LookupResponse>> createCall() {
                return mITunesApi.getLookupResponse(url,podcastId);
            }
        }.getAsLiveData();

    }

}
