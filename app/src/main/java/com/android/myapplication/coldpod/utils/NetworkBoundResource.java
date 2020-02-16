package com.android.myapplication.coldpod.utils;

import android.util.Log;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

import com.android.myapplication.coldpod.network.ApiResponse;

// RequestObject: Type for the API response. (network request)
public abstract class NetworkBoundResource<UiObject, RequestObject> {

    private static final String TAG = "NetworkBoundResource";

    protected MediatorLiveData<Resource<UiObject>> results = new MediatorLiveData<>();

    public NetworkBoundResource() {
        init();
    }

    private void init(){
        // update LiveData for loading status
        results.setValue(new Resource<UiObject>(Resource.Status.LOADING,null,null));
        fetchFromNetwork();
    }

    private void fetchFromNetwork() {

        final LiveData<ApiResponse<RequestObject>> apiResponse = createCall();

        results.addSource(apiResponse, new Observer<ApiResponse<RequestObject>>() {
            @Override
            public void onChanged(@Nullable final ApiResponse<RequestObject> requestObjectApiResponse) {
                results.removeSource(apiResponse);
                handleNetworkCall(requestObjectApiResponse);
            }
        });

    }
    void handleNetworkCall(ApiResponse<RequestObject> response){
        if(response instanceof ApiResponse.ApiSuccessResponse){
            handleApiSuccessResponse((ApiResponse.ApiSuccessResponse) response);
        }else if(response instanceof ApiResponse.ApiEmptyResponse){
            handleApiEmptyResponse((ApiResponse.ApiEmptyResponse) response);
        }else{
            handleApiErrorResponse((ApiResponse.ApiErrorResponse) response);
        }
    }
    protected abstract void handleApiSuccessResponse(ApiResponse.ApiSuccessResponse response);
    protected abstract void handleApiEmptyResponse(ApiResponse.ApiEmptyResponse response);
    protected abstract void handleApiErrorResponse(ApiResponse.ApiErrorResponse response);




    // Called to create the API call.
    @NonNull @MainThread
    protected abstract LiveData<ApiResponse<RequestObject>> createCall();

    // Returns a LiveData object that represents the resource that's implemented
    // in the base class.
    public final LiveData<Resource<UiObject>> getAsLiveData(){
        return results;
    };
}