package com.simple.sqldatabase.fields;

public class DBFieldLong extends DBField<Long> {
    public DBFieldLong(String key, Class<Long> valueType, boolean isPrimary) {
        super(key, valueType, isPrimary);
    }
}
