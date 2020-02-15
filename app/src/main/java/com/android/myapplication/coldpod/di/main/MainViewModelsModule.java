package com.android.myapplication.coldpod.di.main;

import androidx.lifecycle.ViewModel;

import com.android.myapplication.coldpod.MainActivityViewModel;
import com.android.myapplication.coldpod.di.ViewModelKey;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class MainViewModelsModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainActivityViewModel.class)
    public abstract ViewModel bindMainActivityViewModel(MainActivityViewModel viewModel);
}




