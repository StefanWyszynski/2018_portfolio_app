package com.portfolio_app.mvvm_sample.service.repository.state;

import com.portfolio_app.base.DownloadResult;
import com.portfolio_app.mvvm_sample.service.model.UserList;
import com.portfolio_app.mvvm_sample.service.repository.DBUserListHelper;
import com.portfolio_app.mvvm_sample.service.repository.MVVMRepository;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


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

/**
 * the state is for downloading data from the internet
 */
public class DataProcessingStateDownloader extends DataProcessingState {

    public DataProcessingStateDownloader(MVVMRepository repository) {
        super(repository);
    }

    @Override
    public void execute() {
        repository.disposeDownload();

        Observable<UserList> jsonForSelectedWeek =
                repository.getRetrofitDownloader().getService().getJsonForSelectedWeek();
        DBUserListHelper dbDataHelper = repository.getDBUserListHelper();
        repository.setDownloadDisposable(jsonForSelectedWeek
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(userList -> {
                    dbDataHelper.saveDownloadedUsersToDB(userList);
                    repository.setValue(new DownloadResult<>(userList, DownloadResult.ResultStatus.DOWNLOADED));
                }, throwable -> {
                    // we couldn't load the data so let's try to load file
                    if (dbDataHelper.isDataAvailableToLoad()) {
                        String json = dbDataHelper.convertUsersBlobToJSONString();
                        repository.setValue(new DownloadResult<>(UserList.fromJson(json),
                                DownloadResult.ResultStatus.LOADED));
                    } else {
                        repository.setValue(new DownloadResult<>(null, DownloadResult.ResultStatus.NULL));
                    }
                    throwable.printStackTrace();
                }));
    }
}