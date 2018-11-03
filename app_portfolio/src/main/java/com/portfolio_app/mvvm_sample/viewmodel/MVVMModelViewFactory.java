package com.portfolio_app.mvvm_sample.viewmodel;

import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.portfolio_app.mvvm_sample.service.repository.MVVMRepository;

import javax.inject.Inject;

public class MVVMModelViewFactory implements ViewModelProvider.Factory {

    private MVVMRepository repository;

    @Inject
    public MVVMModelViewFactory(@NonNull MVVMRepository repository) {
        this.repository = repository;
    }

    @NonNull
    @Override
    public MVVMModelView create(@NonNull Class modelClass) {
        return new MVVMModelView(repository);
    }
}