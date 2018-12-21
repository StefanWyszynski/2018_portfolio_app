package com.portfolio_app.mvvm_sample.di;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.support.annotation.NonNull;

import com.portfolio_app.mvvm_sample.service.repository.AppDatabase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {
    Application application;

    public ApplicationModule(@NonNull Application application) {
        this.application = application;
    }

    @Provides
    @Singleton
    public Application getApplication() {
        return application;
    }

    @Provides
    @Singleton
    public AppDatabase getInMemoryDatabase(Application application) {
        return Room.inMemoryDatabaseBuilder(application, AppDatabase.class)
                .build();
    }
}
