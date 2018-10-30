package com.portfolio_app.mvvm_sample.service.repository;

import com.portfolio_app.base.PortfolioRepositoryBase;
import com.portfolio_app.mvvm_sample.service.repository.state.DataProcessingStateLoader;

import io.reactivex.disposables.Disposable;

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
public class MVVMRepository extends PortfolioRepositoryBase {

    protected Disposable downloadDisposable;
    private DBUserListHelper DBUserListHelper;
    private MVVMRetrofit retrofitDownloader;

    public MVVMRepository() {
        retrofitDownloader = new MVVMRetrofit();
        DBUserListHelper = new DBUserListHelper();
    }

    public DBUserListHelper getDBUserListHelper() {
        return DBUserListHelper;
    }

    public MVVMRetrofit getRetrofitDownloader() {
        return retrofitDownloader;
    }

    public void downloadUserList() {
        new DataProcessingStateLoader(this).execute();
    }

    public void disposeDownload() {
        if (downloadDisposable != null && !downloadDisposable.isDisposed()) {
            downloadDisposable.dispose();
        }
    }

    public void setDownloadDisposable(Disposable subscribe) {
        disposeDownload();
        downloadDisposable = subscribe;
    }
}
