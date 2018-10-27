package com.portfolio_app;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.portfolio_app.mvvm_sample.service.model.database.DBUsersManager;
import com.portfolio_app.services.ObjectsProvider;

/**
 * @author Stefan Wyszynski
 */
public class PortfolioApp extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        registerDBUsersManager();
    }

    private void registerDBUsersManager() {
        DBUsersManager dbUsersManager = new DBUsersManager(getApplicationContext());
        ObjectsProvider.getInstance().registerObject(DBUsersManager.class, dbUsersManager);
    }
}