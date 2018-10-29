package com.portfolio_app.mvvm_sample.service.model.database;

import com.simple.sqldatabase.DBDAOTableBase;
import com.simple.sqldatabase.annotations.DBColumn;
import com.simple.sqldatabase.fields.DBBlobType;
import com.simple.sqldatabase.fields.DBFieldBlob;
import com.simple.sqldatabase.fields.DBFieldLong;
import com.simple.sqldatabase.fields.DBFieldString;

/**
 * @author Stefan Wyszynski
 */
public class DBUsersTable extends DBDAOTableBase {

    @DBColumn(primaryKey = true, autoIncrement = false)
    public DBFieldString id;
    @DBColumn
    public DBFieldLong downloadTime;
    @DBColumn
    public DBFieldBlob userListJsonBlob;

    private int waitTimeInMilisecounds = 1000 * 10;

    @Override
    public String getTableName() {
        return "users";
    }

    /**
     * specify how long you will have to wait to allow re-downloading the data
     *
     * @param waitTimeInMilisecounds
     */
    public void setAllowDownloadAfter(int waitTimeInMilisecounds) {
        this.waitTimeInMilisecounds = waitTimeInMilisecounds;
    }

    public boolean needToBeDownloaded() {
        Long date = downloadTime.get();
        if (date == null || date == 0L) {
            return true;
        }
        return (System.currentTimeMillis() - date) > waitTimeInMilisecounds;
    }

    public void putInfo(String primary_key, byte[] bytes) {
        id.set(primary_key);
        downloadTime.set(System.currentTimeMillis());
        DBBlobType value = new DBBlobType();
        value.bytes = bytes;
        userListJsonBlob.set(value);
    }

    public byte[] convertToBlob(String str) {
        return str.getBytes();
    }

    public String convertBlobToString() {
        if (!blobExists()) {
            return null;
        }
        return new String(userListJsonBlob.get().bytes);
    }

    public boolean blobExists() {
        byte[] bytes = userListJsonBlob.get().bytes;
        if (bytes != null && bytes.length > 40) {
            return true;
        }
        return false;
    }
}
