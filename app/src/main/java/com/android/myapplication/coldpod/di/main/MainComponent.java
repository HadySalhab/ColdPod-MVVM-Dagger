package com.android.myapplication.coldpod.di.main;

import com.android.myapplication.coldpod.ui.details.PodCastDetailActivity;
import com.android.myapplication.coldpod.ui.main.BaseFragment;
import com.android.myapplication.coldpod.ui.main.MainActivity;
import com.android.myapplication.coldpod.ui.podcast_entry.PodCastEntryActivity;
import com.android.myapplication.coldpod.ui.podcasts.PodCastListActivity;

import dagger.Subcomponent;

@MainScope
@Subcomponent(modules ={
        MainViewModelsModule.class,
        MainModule.class
})
public interface MainComponent {
    void inject(MainActivity mainActivity);
    void injectPodCastListActivity(PodCastListActivity podCastListActivity);
    void injectPodCastDetailActivity(PodCastDetailActivity podCastDetailActivity);
    void injectBaseFragment(BaseFragment baseFragment);
    void injectPodCastEntryActivity(PodCastEntryActivity podCastEntryActivity);
}
