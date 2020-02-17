package com.android.myapplication.coldpod.di;

import android.app.Application;

import com.android.myapplication.coldpod.di.main.MainComponent;
import com.android.myapplication.coldpod.di.main.PodCastIdModule;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;

@Singleton
@Component(
        modules = {
                AppModule.class,
                ViewModelFactoryModule.class
        }
)
public interface AppComponent {
    MainComponent getMainComponent(PodCastIdModule podCastIdModule);

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application (Application application);
        AppComponent build();
    }
}
