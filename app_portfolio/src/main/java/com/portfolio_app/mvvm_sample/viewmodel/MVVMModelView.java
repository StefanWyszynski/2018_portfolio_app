package com.portfolio_app.mvvm_sample.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.portfolio_app.base.DownloadResult;
import com.portfolio_app.mvvm_sample.service.model.UserList;
import com.portfolio_app.mvvm_sample.service.repository.MVVMRepository;

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

