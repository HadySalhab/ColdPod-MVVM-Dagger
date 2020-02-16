package com.android.myapplication.coldpod;

import android.app.Application;

import androidx.annotation.UiThread;

import com.android.myapplication.coldpod.di.AppComponent;
import com.android.myapplication.coldpod.di.DaggerAppComponent;

import timber.log.Timber;


public class BaseApplication extends Application {
    private AppComponent mAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        Timber.plant(new Timber.DebugTree());
    }

    @UiThread
    public AppComponent getAppComponent() {
        if (mAppComponent == null) {
            mAppComponent = DaggerAppComponent.builder()
                    .application(this)
                    .build();
        }
        return mAppComponent;
    }
}
