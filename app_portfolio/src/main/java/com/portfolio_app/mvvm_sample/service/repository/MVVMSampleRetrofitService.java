package com.portfolio_app.mvvm_sample.service.repository;

import com.portfolio_app.mvvm_sample.service.model.UserList;

import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * @author Stefan Wyszynski
 */
public interface MVVMSampleRetrofitService {
    @GET("/api/users")
    Observable<UserList> getJsonForSelectedWeek();
}
