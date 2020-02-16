package com.android.myapplication.coldpod.di.main;

import com.android.myapplication.coldpod.MainActivity;
import com.android.myapplication.coldpod.ui.add.AddFragment;

import dagger.Subcomponent;

@MainScope
@Subcomponent(modules ={
        MainViewModelsModule.class,
        MainModule.class
})
public interface MainComponent {
    void inject(MainActivity mainActivity);
    void injectAddFragment(AddFragment addFragment);
}
