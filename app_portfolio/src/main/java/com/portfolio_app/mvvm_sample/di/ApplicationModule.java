package com.portfolio_app.mvvm_sample.di;

import android.app.Application;
import android.support.annotation.NonNull;

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
}
