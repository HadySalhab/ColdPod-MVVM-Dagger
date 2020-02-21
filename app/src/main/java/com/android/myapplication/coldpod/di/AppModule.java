package com.android.myapplication.coldpod.di;

import android.app.Application;

import androidx.room.Room;

import com.android.myapplication.coldpod.R;
import com.android.myapplication.coldpod.persistence.AppDatabase;
import com.android.myapplication.coldpod.persistence.PodCastDao;
import com.android.myapplication.coldpod.utils.Constants;
import com.android.myapplication.coldpod.utils.LiveDataCallAdapterFactory;
import com.android.myapplication.coldpod.utils.XmlOrJsonConverterFactory;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

import static com.android.myapplication.coldpod.utils.Constants.DATABASE_NAME;

@Module
public class AppModule {

    @Singleton
    @Provides
    static OkHttpClient provideOkHttpClient(){
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return new OkHttpClient.Builder().addInterceptor(interceptor).build();
    }

    @Singleton
    @Provides
    static XmlOrJsonConverterFactory provideXmlOrJsonConverterFactory(){
        return new XmlOrJsonConverterFactory();
    }

    @Singleton
    @Provides
    static Retrofit.Builder provideRetrofitBuilder(OkHttpClient client,XmlOrJsonConverterFactory xjf) {
        return new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .client(client)
                .addConverterFactory(xjf)
                .addCallAdapterFactory(new LiveDataCallAdapterFactory());
    }

    @Singleton
    @Provides
    static RequestOptions provideRequestOptions() {
        return RequestOptions
                .placeholderOf(R.drawable.white_background)
                .error(R.drawable.white_background);
    }

    @Singleton
    @Provides
    static RequestManager provideGlideInstance(Application application, RequestOptions requestOptions) {
        return Glide.with(application)
                .setDefaultRequestOptions(requestOptions);
    }

    @Singleton
    @Provides
    static AppDatabase provideAppDb(Application application) {
        return Room.databaseBuilder(application, AppDatabase.class, DATABASE_NAME)
                .fallbackToDestructiveMigration() // get correct db version if schema changed
                .build();
    }

    @Singleton
    @Provides
    static PodCastDao providePodCastDao(AppDatabase db){
        return db.getPodCastDao();
    }

    @Singleton
    @Provides
    @Named("diskIO")
    static Executor provideDiskIOExecutor(){
        return Executors.newSingleThreadExecutor();
    }
    @Singleton
    @Provides
    @Named("networkIO")
    static Executor provideNetworkIOExecutor(){
        return Executors.newFixedThreadPool(3);
    }

}
