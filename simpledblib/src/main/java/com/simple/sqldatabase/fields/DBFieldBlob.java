package com.simple.sqldatabase.fields;

public class DBFieldBlob extends DBField<DBBlobType> {
    public DBFieldBlob(String key, Class<DBBlobType> valueType, boolean isPrimary) {
        super(key, valueType, isPrimary);
    }
}
