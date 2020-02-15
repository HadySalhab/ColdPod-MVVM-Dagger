package com.android.myapplication.coldpod.di.main;

import dagger.Subcomponent;

@MainScope
@Subcomponent(modules ={
        MainViewModelsModule.class,
        MainModule.class
})
public interface MainComponent {
}
