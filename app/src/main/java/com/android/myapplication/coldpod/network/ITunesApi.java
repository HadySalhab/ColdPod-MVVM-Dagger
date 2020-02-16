package com.android.myapplication.coldpod.network;

import androidx.lifecycle.LiveData;

import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ITunesApi {
    @GET("{country}/podcasts/top-podcasts/all/50/explicit.json")
    LiveData<ApiResponse<ITunesResponse>> getTopPodcasts(
            @Path("country") String country
    );
}
