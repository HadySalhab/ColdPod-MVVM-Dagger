package com.android.myapplication.coldpod.di.main;

import androidx.recyclerview.widget.DiffUtil;

import com.android.myapplication.coldpod.persistence.PodcastEntry;
import com.android.myapplication.coldpod.network.data.Podcasts;
import com.android.myapplication.coldpod.network.ITunesApi;
import com.android.myapplication.coldpod.persistence.Item;
import com.android.myapplication.coldpod.ui.details.PodCastDetailDiffUtil;
import com.android.myapplication.coldpod.ui.main.subscribed.PodCastEntryListDiffUtil;
import com.android.myapplication.coldpod.ui.podcasts.PodCastsDiffUtil;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

@Module
public class MainModule {

    //bind an implementation to an interface
    @Provides
    static DiffUtil.ItemCallback<Podcasts> bindPodCastDiffUtil(PodCastsDiffUtil podCastsDiffUtil) {
        return podCastsDiffUtil;
    }

    //bind an implementation to an interface
    @Provides
    static DiffUtil.ItemCallback<Item> bindPodCastDetailDiffUtil(PodCastDetailDiffUtil podCastDetailDiffUtil) {
        return podCastDetailDiffUtil;
    }

    //bind an implementation to an interface
    @Provides
    static DiffUtil.ItemCallback<PodcastEntry> bindPodcastEntryDiffUtil(PodCastEntryListDiffUtil podCastEntryListDiffUtil) {
        return podCastEntryListDiffUtil;
    }

    @MainScope
    @Provides
    static ITunesApi provideITunesApi(Retrofit.Builder retrofitBuilder) {
        return retrofitBuilder.build().create(ITunesApi.class);
    }


}
