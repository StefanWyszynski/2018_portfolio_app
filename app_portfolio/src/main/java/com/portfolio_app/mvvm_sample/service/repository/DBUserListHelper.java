package com.portfolio_app.mvvm_sample.service.repository;

import com.google.gson.Gson;
import com.portfolio_app.mvvm_sample.service.model.UserList;
import com.portfolio_app.mvvm_sample.service.model.database.DBUsersManager;
import com.portfolio_app.mvvm_sample.service.model.database.DBUsersTable;

import javax.inject.Inject;
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

/**
 * Simple helper class to save/load UserList to/from SQLlite database
 */
public class DBUserListHelper {
    // 10 seconds - should be bigger but this is only for demonstration purposes
    private static final String USERS_DB_ID = "users_mvvm";

    private DBUsersManager dbUsersManager;
    private DBUsersTable dbUsersTable;

    @Inject
    public DBUserListHelper(DBUsersManager dbUsersManager) {
        this.dbUsersManager = dbUsersManager;
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
