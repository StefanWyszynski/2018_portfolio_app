package com.portfolio_app.mvvm_sample.service.repository.state;

import com.portfolio_app.base.DownloadResult;
import com.portfolio_app.mvvm_sample.service.model.UserList;
import com.portfolio_app.mvvm_sample.service.repository.DBUserListHelper;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * the state is for downloading data from the internet
 */
public class DataProcessingStateDownloader extends DataProcessingState {

    @Override
    public void execute() {
        repository.disposeDownload();

        Observable<UserList> jsonForSelectedWeek =
                repository.getRetrofitDownloader().getService().getJsonForSelectedWeek();
        DBUserListHelper dbDataHelper = repository.getDBUserListHelper();
        repository.setDownloadDisposable(jsonForSelectedWeek
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(jsonUserList -> {
                    dbDataHelper.saveDownloadedUsersToDB(jsonUserList);
                    repository.setValue(new DownloadResult<>(jsonUserList, DownloadResult.ResultStatus.DOWNLOADED));
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