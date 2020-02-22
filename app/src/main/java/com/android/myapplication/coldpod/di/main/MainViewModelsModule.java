package com.android.myapplication.coldpod.di.main;

import androidx.lifecycle.ViewModel;

import com.android.myapplication.coldpod.ui.main.MainActivityViewModel;
import com.android.myapplication.coldpod.di.ViewModelKey;
import com.android.myapplication.coldpod.ui.playing.PlayingViewModel;
import com.android.myapplication.coldpod.ui.podcast_entry.PodCastEntryViewModel;
import com.android.myapplication.coldpod.ui.podcasts.PodCastListViewModel;
import com.android.myapplication.coldpod.ui.details.PodCastDetailViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class MainViewModelsModule {
    @Binds
    @IntoMap
    @ViewModelKey(MainActivityViewModel.class)
    public abstract ViewModel bindMainActivityViewModel(MainActivityViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(PodCastListViewModel.class)
    public abstract ViewModel bindPodCastListViewModel(PodCastListViewModel viewModel);


    @Binds
    @IntoMap
    @ViewModelKey(PodCastDetailViewModel.class)
    public abstract ViewModel bindPodCastDetailViewModel(PodCastDetailViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(PodCastEntryViewModel.class)
    public abstract ViewModel bindPodCastEntryViewModel(PodCastEntryViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(PlayingViewModel.class)
    public abstract ViewModel bindPlayingViewModel(PlayingViewModel viewModel);
}




