package com.portfolio_app.base;

import android.arch.lifecycle.MutableLiveData;

/**
 * @author Stefan Wyszynski
 */
public abstract class PortfolioRepositoryBase<T> {

    protected MutableLiveData<T> liveData;

    public PortfolioRepositoryBase() {
        liveData = new MutableLiveData<>();
    }

    public MutableLiveData<T> getLiveData() {
        return liveData;
    }

    public void setValue(T data) {
        liveData.setValue(data);
    }

    public String getSettingsName() {
        return getClass().getSimpleName();
    }
}
