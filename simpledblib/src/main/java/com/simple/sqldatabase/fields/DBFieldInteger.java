package com.simple.sqldatabase.fields;

public class DBFieldInteger extends DBField<Integer> {
    public DBFieldInteger(String key, Class<Integer> valueType, boolean isPrimary) {
        super(key, valueType, isPrimary);
    }
}
