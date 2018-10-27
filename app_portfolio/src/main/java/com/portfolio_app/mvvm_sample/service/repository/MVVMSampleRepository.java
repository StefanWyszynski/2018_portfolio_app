package com.portfolio_app.mvvm_sample.service.repository;

import com.google.gson.Gson;
import com.portfolio_app.base.DownloadResult;
import com.portfolio_app.base.PortfolioRepositoryBase;
import com.portfolio_app.mvvm_sample.service.model.UserList;
import com.portfolio_app.mvvm_sample.service.model.database.DBUsersManager;
import com.portfolio_app.mvvm_sample.service.model.database.DBUsersTable;
import com.portfolio_app.services.ObjectsProvider;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @author Stefan Wyszynski
 */
public class MVVMSampleRepository extends PortfolioRepositoryBase {
    // 10 seconds - should be bigger but this is only for demonstration purposes
    private static final int ALLOW_REFRESH_AFTER = 10;
    private static final String USERS_DB_ID = "users_mvvm";
    private final DBUsersManager dbUsersManager;
    private Disposable downloadDisposable;
    private MVVMSampleRetrofit retrofitDownloader;
    private DBUsersTable dbUsersTable;

    public MVVMSampleRepository() {
        dbUsersManager = ObjectsProvider.getInstance().get(DBUsersManager.class);
        retrofitDownloader = new MVVMSampleRetrofit();
    }

    public void downloadUserList() {
        dbUsersTable = dbUsersManager.getSetting(USERS_DB_ID);
        dbUsersTable.setAllowDownloadAfter(ALLOW_REFRESH_AFTER);
        new DBLoaderState().process();
    }

    public void disposeDownload() {
        if (downloadDisposable != null && !downloadDisposable.isDisposed()) {
            downloadDisposable.dispose();
        }
    }

    //
    public boolean isDataAvailableToLoad() {
        return (dbUsersTable.blobExists());
    }

    private interface ProcessingState {
        void process();
    }

    /**
     * This state is for loading json from database and convert it to WeekData
     * If there is no json
     */
    private class DBLoaderState implements ProcessingState {

        @Override
        public void process() {
            DownloaderState downloaderState = new DownloaderState();

            // if this is current week then we will be able to downloadUserList otherwise we won't because
            // it could make a huge influence on traffic when user will be able to downloadUserList multiple weeks
            // so user can downloadUserList only current week to save bandwith
            boolean isCurrentWeek = true;

            // if there is no file and this is current week then downloadUserList it
            if (isCurrentWeek && !isDataAvailableToLoad()) {
                downloaderState.process();
                return;
            }

            // if downloaded long time ago and this is current week
            if (isCurrentWeek && dbUsersTable.needToBeDownloaded()) {
                // then downloadUserList json
                downloaderState.process();
                return;
            } else {
                // file was downloadUserList recently so check if exists
                if (isDataAvailableToLoad()) {
                    // decode from blob to json and finally to DaysContainer
                    String json = dbUsersTable.convertBlobToString();
                    setValue(new DownloadResult<>(UserList.fromJson(json), DownloadResult.ResultStatus.LOADED));
                } else {
                    // if this is current week then we can downloadUserList
                    if (isCurrentWeek) {
                        downloaderState.process();
                        return;
                    } else {
                        // this is no current week and there is no data to load so return null
                        setValue(new DownloadResult<>(null, DownloadResult.ResultStatus.NULL));
                    }
                }
            }
        }
    }

    private class DownloaderState implements ProcessingState {

        @Override
        public void process() {
            disposeDownload();

            Observable<UserList> jsonForSelectedWeek = retrofitDownloader.getService().getJsonForSelectedWeek();
            downloadDisposable = jsonForSelectedWeek
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<UserList>() {
                        @Override
                        public void accept(UserList jsonUserList) throws Exception {
                            // save data and return it to data listener
                            saveDownloadedUsersToDB(jsonUserList);
                            setValue(new DownloadResult<>(jsonUserList, DownloadResult.ResultStatus.DOWNLOADED));
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            // we couldn't load the data so let's try to load file
                            if (isDataAvailableToLoad()) {
                                String json = dbUsersTable.convertBlobToString();
                                setValue(new DownloadResult<>(UserList.fromJson(json),
                                        DownloadResult.ResultStatus.LOADED));
                            } else {
                                setValue(new DownloadResult<>(null, DownloadResult.ResultStatus.NULL));
                            }
                            throwable.printStackTrace();
                        }
                    });
        }

        /**
         * Saves user list data to database
         *
         * @param userList
         */
        private void saveDownloadedUsersToDB(UserList userList) {
            String json = new Gson().toJson(userList);
            if (json == null) {
                throw new NullPointerException("Cannot convert user list to json string");
            }
            byte[] blobData = dbUsersTable.convertToBlob(json);
            dbUsersTable.putInfo(USERS_DB_ID, blobData);
            dbUsersManager.saveUsersToDB(USERS_DB_ID);
        }
    }
}
