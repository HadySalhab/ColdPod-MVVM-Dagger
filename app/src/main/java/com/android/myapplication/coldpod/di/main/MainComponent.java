package com.android.myapplication.coldpod.di.main;

import androidx.annotation.Nullable;

import com.android.myapplication.coldpod.MainActivity;
import com.android.myapplication.coldpod.ui.add.AddFragment;
import com.android.myapplication.coldpod.ui.subscribe.SubscribeFragment;

import javax.inject.Named;

import dagger.BindsInstance;
import dagger.Subcomponent;

@MainScope
@Subcomponent(modules ={
        MainViewModelsModule.class,
        MainModule.class
})
public interface MainComponent {
    void inject(MainActivity mainActivity);
    void injectAddFragment(AddFragment addFragment);
    void injectSubscribeFragment(SubscribeFragment subscribeFragment);
}
