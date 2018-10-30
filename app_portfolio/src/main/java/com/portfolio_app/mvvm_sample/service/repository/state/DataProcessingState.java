package com.portfolio_app.mvvm_sample.service.repository.state;

import com.portfolio_app.mvvm_sample.service.repository.MVVMRepository;

public abstract class DataProcessingState {
    protected MVVMRepository repository;

    public DataProcessingState(MVVMRepository repository) {
        this.repository = repository;
    }

    public abstract void execute();
}