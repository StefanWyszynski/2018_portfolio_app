package com.portfolio_app.mvvm_sample.service.repository;

import com.google.gson.Gson;
import com.portfolio_app.mvvm_sample.service.model.UserList;
import com.portfolio_app.mvvm_sample.service.model.database.Users;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.schedulers.Schedulers;

@Singleton
public class DBUsersHelperImpl implements IDBUsersHelper {

    public static final String USERS_ID = "user1";
    private Users users;
    private AppDatabase appDatabase;

    @Inject
    public DBUsersHelperImpl(AppDatabase appDatabase) {
        this.appDatabase = appDatabase;
    }

    @Override
    public void fetchUsers() {
        users = appDatabase.userModel().get(USERS_ID);
        if (users == null) {
            users = new Users();
        }
    }

    @Override
    public boolean needToBeRedownloaded() {
        return users.needToBeDownloaded();
    }

    @Override
    public String convertUsersBlobToJSONString() {
        return users.convertBlobToString();
    }

    /**
     * @return true if blob data exists
     */
    @Override
    public boolean isDataAvailableToLoad() {
        return users.blobExists();
    }

    /**
     * Saves user list data to database
     *
     * @param userList
     */
    @Override
    public void saveDownloadedUsersToDB(UserList userList) {
        String json = new Gson().toJson(userList);
        if (json == null) {
            throw new NullPointerException("Cannot convert user list to a json string");
        }
        users.putInfo(USERS_ID, users.convertToBlob(json));
        appDatabase.userModel().insert(users);
    }

    @Override
    public Maybe<UserList> tryLoadUsersFromSQLDB() {
        return Maybe.create(e -> {
            fetchUsers();
            if (isDataAvailableToLoad()) {
                if (needToBeRedownloaded()) {
                    e.onComplete();
                } else {
                    String json = convertUsersBlobToJSONString();
                    e.onSuccess(UserList.fromJson(json));
                }
            } else {
                e.onComplete();
            }
        });
    }

    @Override
    public void saveUsersToSQLDB(final UserList userList) {
        Observable.create((ObservableOnSubscribe<Boolean>) e -> {
            saveDownloadedUsersToDB(userList);
            e.onComplete();
        }).subscribeOn(Schedulers.io()).subscribe();
    }
}