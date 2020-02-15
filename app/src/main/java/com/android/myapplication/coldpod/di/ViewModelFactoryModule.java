package com.android.myapplication.coldpod.di;

import androidx.lifecycle.ViewModelProvider;


import com.android.myapplication.coldpod.ViewModelProviderFactory;

import dagger.Binds;
import dagger.Module;


@Module
public abstract class ViewModelFactoryModule {

    @Binds
    public abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelProviderFactory viewModelFactory);

}
