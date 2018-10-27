package com.simple.sqldatabase.fields;


public class DBFieldString extends DBField<String> {
    public DBFieldString(String key, Class<String> valueType, boolean isPrimary) {
        super(key, valueType, isPrimary);
    }
}
