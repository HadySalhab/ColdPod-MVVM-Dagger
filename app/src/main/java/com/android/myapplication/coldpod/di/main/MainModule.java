package com.android.myapplication.coldpod.di.main;

import androidx.recyclerview.widget.DiffUtil;

import com.android.myapplication.coldpod.model.Podcasts;
import com.android.myapplication.coldpod.network.ITunesApi;
import com.android.myapplication.coldpod.ui.diffUtils.PodCastsDiffUtil;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

@Module
public class MainModule {

    @Provides
    static DiffUtil.ItemCallback<Podcasts> bindDiffUtilItemCallback(PodCastsDiffUtil podCastsDiffUtil) {
        return podCastsDiffUtil;
    }

    @MainScope
    @Provides
    static ITunesApi provideITunesApi(Retrofit retrofit) {
        return retrofit.create(ITunesApi.class);
    }

}
