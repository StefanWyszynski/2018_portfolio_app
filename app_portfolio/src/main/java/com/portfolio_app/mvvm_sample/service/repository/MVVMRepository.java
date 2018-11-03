package com.portfolio_app.mvvm_sample.service.repository;

import com.portfolio_app.base.PortfolioRepositoryBase;
import com.portfolio_app.mvvm_sample.di.MVVMFragmentComponent;
import com.portfolio_app.mvvm_sample.service.repository.state.DataProcessingStateLoader;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
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

    private final CompositeDisposable disposables = new CompositeDisposable();
    private DBUserListHelper dbUserListHelper;

    @Inject
    public MVVMRepository(DBUserListHelper dbUserListHelper) {
        this.dbUserListHelper = dbUserListHelper;
    }

    public DBUserListHelper getDbUserListHelper() {
        return dbUserListHelper;
    }

    public void downloadUserList(MVVMFragmentComponent mvvmFragmentComponent) {
        new DataProcessingStateLoader(this).execute(mvvmFragmentComponent);
    }

    public void addDisposable(Disposable disposable) {
        disposables.add(disposable);
    }

    public void disposeDownload() {
        disposables.clear();
    }
}
