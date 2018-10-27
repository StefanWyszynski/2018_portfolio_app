package com.simple.sqldatabase.fields;


public class DBFieldBoolean extends DBField<Boolean> {
    public DBFieldBoolean(String dataBaseFieldName, Class<Boolean> valueType, boolean isPrimary) {
        super(dataBaseFieldName, valueType, isPrimary);
    }
}
