package com.android.myapplication.coldpod.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.android.myapplication.coldpod.di.main.MainScope;
import com.android.myapplication.coldpod.network.ApiResponse;
import com.android.myapplication.coldpod.network.Channel;
import com.android.myapplication.coldpod.network.ITunesApi;
import com.android.myapplication.coldpod.network.LookupResponse;
import com.android.myapplication.coldpod.network.LookupResult;
import com.android.myapplication.coldpod.network.RssFeed;
import com.android.myapplication.coldpod.utils.NetworkBoundResource;
import com.android.myapplication.coldpod.utils.Resource;

import java.util.List;

import javax.inject.Inject;

@MainScope
public class PodCastDetailRepository {

    private final ITunesApi mITunesApi;

    private static final String TAG = "PodCastDetailRepository";


    @Inject
    public PodCastDetailRepository(ITunesApi iTunesApi) {
        mITunesApi = iTunesApi;
    }

    public LiveData<Resource<String>> getLookUp(String url, String podcastId) {
        return new NetworkBoundResource<String, LookupResponse>() {
            @Override
            protected void handleApiSuccessResponse(ApiResponse.ApiSuccessResponse response) {
                LookupResponse lookupResponse = (LookupResponse) response.getBody();
                List<LookupResult> lookupResults = lookupResponse.getLookupResults();
                LookupResult lookupResult = lookupResults.get(0);
                String feeUrl = lookupResult.getFeedUrl();
                results.setValue(new Resource<String>(Resource.Status.SUCCESS, feeUrl, null));

            }

            @Override
            protected void handleApiEmptyResponse(ApiResponse.ApiEmptyResponse response) {
                results.setValue(new Resource<String>(Resource.Status.SUCCESS, null, null));

            }

            @Override
            protected void handleApiErrorResponse(ApiResponse.ApiErrorResponse response) {
                results.setValue(new Resource<String>(Resource.Status.ERROR, null, response.getErrorMessage()));
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<LookupResponse>> createCall() {
                return mITunesApi.getLookupResponse(url, podcastId);
            }
        }.getAsLiveData();

    }

    public LiveData<Resource<Channel>> getRssfeed(String url) {
        return new NetworkBoundResource<Channel, RssFeed>() {

            @Override
            protected void handleApiSuccessResponse(ApiResponse.ApiSuccessResponse response) {
                Log.d(TAG, "handleApiSuccessResponse: ");
                RssFeed rssFeed = (RssFeed) response.getBody();
                Channel channel = rssFeed.getChannel();
                results.setValue(new Resource<Channel>(Resource.Status.SUCCESS, channel, null));
            }

            @Override
            protected void handleApiEmptyResponse(ApiResponse.ApiEmptyResponse response) {
                Log.d(TAG, "handleApiEmptyResponse: ");
                results.setValue(new Resource<Channel>(Resource.Status.SUCCESS, null, null));
            }

            @Override
            protected void handleApiErrorResponse(ApiResponse.ApiErrorResponse response) {
                Log.d(TAG, "handleApiErrorResponse: " + response.getErrorMessage());
                results.setValue(new Resource<Channel>(Resource.Status.ERROR, null, response.getErrorMessage()));

            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<RssFeed>> createCall() {
                return mITunesApi.getRssFeed(url);
            }
        }.getAsLiveData();
    }

}
