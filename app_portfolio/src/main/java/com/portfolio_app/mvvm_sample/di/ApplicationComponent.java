package com.portfolio_app.mvvm_sample.di;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Component;

@Component(modules = ApplicationModule.class)
@Singleton
public interface ApplicationComponent {
    Application getApplication();

    MVVMFragmentComponent.Builder mvvmFragmentComponentBuilder();
}
