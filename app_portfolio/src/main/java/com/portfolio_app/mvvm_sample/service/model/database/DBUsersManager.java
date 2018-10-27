package com.portfolio_app.mvvm_sample.service.model.database;

import android.content.Context;

import com.simple.sqldatabase.DBFieldRegistry;
import com.simple.sqldatabase.DBManager;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Stefan Wyszynski
 */
public class DBUsersManager {

    DBManager database;
    private Map<String, DBUsersTable> webPageHolders;

    public DBUsersManager(Context context) {
        webPageHolders = new HashMap<>();
        String name = getClass().getName();
        database = new DBManager(context, name, DBUsersTable.class);
    }

    private void registerSetting(String key) {
        DBUsersTable sd;
        if (webPageHolders.containsKey(key)) {
            sd = webPageHolders.get(key);
        } else {
            sd = DBFieldRegistry.getInstance().createNewDAOTableObject(DBUsersTable.class);
            webPageHolders.put(key, sd);
        }
        sd.id.set(key);
        if (!database.get(sd, sd.id)) {
            database.add(sd);
        }
    }

    public DBUsersTable getSetting(String key) {
        if (!webPageHolders.containsKey(key)) {
            registerSetting(key);
        }
        DBUsersTable setting = webPageHolders.get(key);
        database.get(setting, setting.id);
//        List<DBDAOTableBase> sll = setting.getAllRows(database);
//        setting.getJsonForSelectedWeek(database, setting.id);
        return setting;
    }

    public void saveUsersToDB(String key) {

        DBUsersTable setting = webPageHolders.get(key);
        setting.id.set(key);
//        setting.delete(database, setting.id);
//        setting.add(database);
        database.update(setting, setting.id);
        webPageHolders.put(setting.id.get(), setting);
    }
}
