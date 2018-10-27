package com.simple.sqldatabase;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

public class DBSettingsHandler<T extends DBDAOTableBase> {

    private Map<String, T> settings;
    private DBManager database;
    private Class<T> tClass;

    public DBSettingsHandler(Context context, Class<T> tClass) {
        settings = new HashMap<>();
        String name = getClass().getName();
        database = new DBManager(context, name, tClass);
        this.tClass = tClass;
    }

    private void registerSetting(String key) {
        T sd;
        if (settings.containsKey(key)) {
            sd = settings.get(key);
        } else {
            sd = DBFieldRegistry.getInstance().createNewDAOTableObject(tClass);
            settings.put(key, sd);
        }
        database.saveChanges(sd);
    }

    public T loadSetting(String key) {
        if (!settings.containsKey(key)) {
            registerSetting(key);
        }
        T setting = settings.get(key);
        return setting;
    }

    public void saveSetting(String key) {
        T setting = settings.get(key);
        database.saveChanges(setting);
    }
}
