package com.portfolio_app.mvvm_sample.service.repository;

import com.portfolio_app.mvvm_sample.service.model.UserList;

import io.reactivex.Maybe;

public interface IDBUsersHelper {
    void fetchUsers();

    boolean needToBeRedownloaded();

    String convertUsersBlobToJSONString();

    boolean isDataAvailableToLoad();

    void saveDownloadedUsersToDB(UserList userList);

    Maybe<UserList> tryLoadUsersFromSQLDB();

    void saveUsersToSQLDB(final UserList userList);
}
