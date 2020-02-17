package com.android.myapplication.coldpod.di;

import android.app.Application;

import androidx.room.Room;

import com.android.myapplication.coldpod.R;
import com.android.myapplication.coldpod.database.AppDatabase;
import com.android.myapplication.coldpod.database.PodCastDao;
import com.android.myapplication.coldpod.utils.Constants;
import com.android.myapplication.coldpod.utils.LiveDataCallAdapterFactory;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.android.myapplication.coldpod.utils.Constants.DATABASE_NAME;

@Module
public class AppModule {

    @Singleton
    @Provides
    static Retrofit provideRetrofitInstance() {
        return new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addCallAdapterFactory(new LiveDataCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
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

}
