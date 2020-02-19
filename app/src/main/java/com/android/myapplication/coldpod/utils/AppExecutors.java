package com.android.myapplication.coldpod.utils;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

import java.util.concurrent.Executor;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import dagger.multibindings.IntKey;

@Singleton
public class AppExecutors {
    private final Executor diskIO;
    private final Executor mainThread = new MainThreadExecutor();
    private final Executor networkIO;

    @Inject
    public AppExecutors(@Named("diskIO") Executor diskIO,
                        @Named("networkIO") Executor networkIO) {
        this.diskIO = diskIO;
        this.networkIO = networkIO;
    }

    public Executor diskIO() {
        return diskIO;
    }

    public Executor mainThread() {
        return mainThread;
    }

    public Executor networkIO() {
        return networkIO;
    }

    private static class MainThreadExecutor implements Executor {
        private Handler mainThreadHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(@NonNull Runnable command) {
            mainThreadHandler.post(command);
        }
    }
}
