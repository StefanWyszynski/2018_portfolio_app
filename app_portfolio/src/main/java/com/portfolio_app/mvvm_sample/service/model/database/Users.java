package com.portfolio_app.mvvm_sample.service.model.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;


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
 */
@Entity
public class Users {

    @PrimaryKey
    @NonNull
    public String id;

    @ColumnInfo
    public Long downloadTime;

    @ColumnInfo
    public byte[] userListJsonBlob;

    @Ignore
    private int waitTimeInMilisecounds = 1000 * 60;

    /**
     * specify how long you will have to wait to allow re-downloading the data
     *
     * @param waitTimeInMilisecounds
     */
    public void setAllowDownloadAfter(int waitTimeInMilisecounds) {
        this.waitTimeInMilisecounds = waitTimeInMilisecounds;
    }

    public boolean needToBeDownloaded() {
        Long date = downloadTime;
        if (date == null || date == 0L) {
            return true;
        }
        return (System.currentTimeMillis() - date) > waitTimeInMilisecounds;
    }

    public void putInfo(String primary_key, byte[] bytes) {
        id = primary_key;
        downloadTime = System.currentTimeMillis();
        userListJsonBlob = bytes;
    }

    public byte[] convertToBlob(String str) {
        return str.getBytes();
    }

    public String convertBlobToString() {
        if (!blobExists()) {
            return null;
        }
        return new String(userListJsonBlob);
    }

    public boolean blobExists() {
        byte[] bytes = userListJsonBlob;
        if (bytes != null && bytes.length > 20) {
            return true;
        }
        return false;
    }
}
