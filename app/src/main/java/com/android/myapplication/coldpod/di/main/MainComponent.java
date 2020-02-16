package com.android.myapplication.coldpod.di.main;

import com.android.myapplication.coldpod.MainActivity;

import dagger.Subcomponent;

@MainScope
@Subcomponent(modules ={
        MainViewModelsModule.class,
        MainModule.class
})
public interface MainComponent {
    void inject(MainActivity mainActivity);
}
