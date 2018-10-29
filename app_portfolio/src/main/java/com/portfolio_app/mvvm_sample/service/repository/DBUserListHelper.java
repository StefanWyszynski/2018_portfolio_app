package com.portfolio_app.mvvm_sample.service.repository;

import com.google.gson.Gson;
import com.portfolio_app.mvvm_sample.service.model.UserList;
import com.portfolio_app.mvvm_sample.service.model.database.DBUsersManager;
import com.portfolio_app.mvvm_sample.service.model.database.DBUsersTable;
import com.portfolio_app.services.ObjectsProvider;

/**
 * Simple helper class to save/load UserList downloaded data to/from SQLlite database
 */
public class DBUserListHelper {
    // 10 seconds - should be bigger but this is only for demonstration purposes
    private static final String USERS_DB_ID = "users_mvvm";

    private DBUsersManager dbUsersManager;
    private DBUsersTable dbUsersTable;

    public DBUserListHelper() {
        dbUsersManager = ObjectsProvider.getInstance().get(DBUsersManager.class);
        dbUsersTable = dbUsersManager.getSetting(USERS_DB_ID);
    }

    public boolean needToBeRedownloaded() {
        return dbUsersTable.needToBeDownloaded();
    }

    public String convertUsersBlobToJSONString() {
        return dbUsersTable.convertBlobToString();
    }

    /**
     * @return true if blob data exists
     */
    public boolean isDataAvailableToLoad() {
        return (dbUsersTable.blobExists());
    }

    /**
     * Saves user list data to database
     *
     * @param userList
     */
    public void saveDownloadedUsersToDB(UserList userList) {
        String json = new Gson().toJson(userList);
        if (json == null) {
            throw new NullPointerException("Cannot convert user list to json string");
        }
        byte[] blobData = dbUsersTable.convertToBlob(json);
        dbUsersTable.putInfo(USERS_DB_ID, blobData);
        dbUsersManager.saveUsersToDB(USERS_DB_ID);
    }
}
