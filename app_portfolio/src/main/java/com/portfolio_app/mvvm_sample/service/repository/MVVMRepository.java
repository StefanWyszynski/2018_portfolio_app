package com.portfolio_app.mvvm_sample.service.repository;

import com.portfolio_app.base.PortfolioRepositoryBase;
import com.portfolio_app.mvvm_sample.service.repository.state.DataProcessingStateLoader;

import io.reactivex.disposables.Disposable;

/**
 * @author Stefan Wyszynski
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
