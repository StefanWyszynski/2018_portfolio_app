package com.portfolio_app;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.portfolio_app.mvvm_sample.di.ApplicationComponent;
import com.portfolio_app.mvvm_sample.di.ApplicationModule;
import com.portfolio_app.mvvm_sample.di.DaggerApplicationComponent;

/*
 * Copyright 2018, The Portfolio project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * @author Stefan Wyszynski
 *
 */
public class PortfolioApp extends Application {

    private static ApplicationComponent appComponent;

    public static ApplicationComponent getAppComponent() {
        return appComponent;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        appComponent =
                DaggerApplicationComponent.builder().applicationModule(new ApplicationModule(this)).build();

    }


}