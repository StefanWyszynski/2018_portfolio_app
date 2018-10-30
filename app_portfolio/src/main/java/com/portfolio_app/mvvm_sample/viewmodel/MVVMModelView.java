package com.portfolio_app.mvvm_sample.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.portfolio_app.base.DownloadResult;
import com.portfolio_app.mvvm_sample.service.model.UserList;
import com.portfolio_app.mvvm_sample.service.repository.MVVMRepository;

/**
 * @author Stefan Wyszynski
 */
public class MVVMModelView extends ViewModel {
    MVVMRepository timelineRepository;

    public LiveData<DownloadResult<UserList>> getWeekLiveData() {
        if (timelineRepository == null) {
            timelineRepository = new MVVMRepository();
        }
        return timelineRepository.getLiveData();
    }

    public void downloadUserList() {
        timelineRepository.downloadUserList();
    }

    @Override
    protected void onCleared() {
        timelineRepository.disposeDownload();
        super.onCleared();
    }
}

