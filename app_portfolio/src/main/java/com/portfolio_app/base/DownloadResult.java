package com.portfolio_app.base;

/**
 * @author Stefan Wyszynski
 */
public class DownloadResult<R> {
    public R result;
    public ResultStatus status;

    public DownloadResult(R result, ResultStatus status) {
        this.result = result;
        this.status = status;
    }

    public enum ResultStatus {
        DOWNLOADED, LOADED, NULL
    }
}