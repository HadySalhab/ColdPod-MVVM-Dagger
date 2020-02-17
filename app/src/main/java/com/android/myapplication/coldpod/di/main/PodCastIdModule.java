package com.android.myapplication.coldpod.di.main;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
public class PodCastIdModule {

    private String podCastId;

    public PodCastIdModule() {
    }
    public PodCastIdModule(String podCastId){
        this.podCastId = podCastId;
    }
    @Provides
    @Named("podCast_Id")
    String providePodCastId(){
        return this.podCastId;
    }
}
