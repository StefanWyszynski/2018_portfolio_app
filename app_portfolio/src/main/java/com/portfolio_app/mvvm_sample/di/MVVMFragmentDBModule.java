package com.portfolio_app.mvvm_sample.di;

import com.portfolio_app.mvvm_sample.service.repository.DBUsersHelperImpl;
import com.portfolio_app.mvvm_sample.service.repository.IDBUsersHelper;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class MVVMFragmentDBModule {

    @Binds
    @MVVMFragmentScope
    abstract IDBUsersHelper getUsersHelper(DBUsersHelperImpl dbUsersHelper);
}
