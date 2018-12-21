package com.portfolio_app.mvvm_sample.service.repository;

import com.portfolio_app.base.DownloadResult;
import com.portfolio_app.base.PortfolioRepositoryBase;
import com.portfolio_app.mvvm_sample.di.MVVMFragmentComponent;
import com.portfolio_app.mvvm_sample.service.model.UserList;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
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
public class MVVMRepository extends PortfolioRepositoryBase {

    private final CompositeDisposable disposables = new CompositeDisposable();

    public IDBUsersHelper dbHelper;

    @Inject
    public MVVMRepository(IDBUsersHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    public void downloadUserList(MVVMFragmentComponent mvvmFragmentComponent) {

        disposeDownload();

        MVVMRetrofitService retrofitService = mvvmFragmentComponent.getRetrofitService();
        Single<UserList> retrofitServiceUsers = retrofitService.getUsers();

        Flowable<Object> concat = Maybe.concat(dbHelper.tryLoadUsersFromSQLDB(), retrofitServiceUsers.toMaybe());
        Disposable disposable = concat
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .firstElement()
                .subscribe(userList -> {
                    dbHelper.saveUsersToSQLDB((UserList) userList);
                    setValue(DownloadResult.success(userList));
                }, throwable -> {
                    setValue(DownloadResult.failure());
                    throwable.printStackTrace();
                });
        addDisposable(disposable);
    }

    public void addDisposable(Disposable disposable) {
        disposables.add(disposable);
    }

    public void disposeDownload() {

        disposables.clear();
    }

}
