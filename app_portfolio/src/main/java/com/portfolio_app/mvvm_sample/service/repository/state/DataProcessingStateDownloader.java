package com.portfolio_app.mvvm_sample.service.repository.state;

import com.portfolio_app.base.DownloadResult;
import com.portfolio_app.mvvm_sample.service.model.UserList;
import com.portfolio_app.mvvm_sample.service.repository.DBUserListHelper;
import com.portfolio_app.mvvm_sample.service.repository.MVVMRepository;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

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
                .subscribe(new Consumer<UserList>() {
                    @Override
                    public void accept(UserList userList) throws Exception {
                        dbDataHelper.saveDownloadedUsersToDB(userList);
                        repository.setValue(new DownloadResult<>(userList, DownloadResult.ResultStatus.DOWNLOADED));
                    }
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