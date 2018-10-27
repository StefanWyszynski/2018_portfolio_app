package com.simple.sqldatabase.fields;

import android.content.ContentValues;
import android.database.Cursor;

public class DBField<V> {
    protected String dbColumn;
    protected String dbFieldType;
    protected boolean isPrimary;
    protected DBFieldType fieldType;
    protected V value;
    protected boolean modified;
    private Class<V> valueType;
    private boolean dbAutoIncrement;

    // TODO zmienic typy danych wg. bazy danych
    public DBField() {
    }

    // TODO zmienic typy danych wg. bazy danych
    public DBField(String dbColumn, Class<V> valueType, boolean isPrimary) {
        this.dbColumn = dbColumn;
        this.isPrimary = isPrimary;
        setValueType(valueType);
    }

    public static <T extends DBField> void setFromDatabaseCursor(T databaseField, Cursor cursor, int
            indexInCursor) {

        switch (databaseField.fieldType) {
            case INT:
                databaseField.set(cursor.getInt(indexInCursor));
                break;
            case BOOLEAN:
                databaseField.set(cursor.getShort(indexInCursor) > 0);
                break;
            case STRING:
                databaseField.set(cursor.getString(indexInCursor));
                break;
            case FLOAT:
                databaseField.set(cursor.getFloat(indexInCursor));
                break;
            case LONG:
                databaseField.set(cursor.getLong(indexInCursor));
                break;
            case BLOB:
                DBBlobType blobType = new DBBlobType();
                blobType.bytes = cursor.getBlob(indexInCursor);
                databaseField.set(blobType);
                break;
        }
    }

    public boolean isPrimary() {
        return isPrimary;
    }

    public void setPrimary(boolean primary) {
        isPrimary = primary;
    }

    public DBField getClone() {
        DBField dbField = null;
        try {
            dbField = getClass().newInstance();
            dbField.setValueType(valueType);
            dbField.setColumn(dbColumn);
            dbField.setPrimary(isPrimary);
            dbField.setAutoIncrement(dbAutoIncrement);
            dbField.set(get());
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return dbField;
    }

    private void setValueType(Class<V> valueType) {
        this.valueType = valueType;
        if (valueType == int.class) {
            fieldType = DBFieldType.INT;
            dbFieldType = "INTEGER";
        } else if (valueType == boolean.class) {
            fieldType = DBFieldType.BOOLEAN;
            dbFieldType = "NUMERIC";
        } else if (valueType == String.class) {
            fieldType = DBFieldType.STRING;
            dbFieldType = "TEXT";
        } else if (valueType == float.class) {
            fieldType = DBFieldType.FLOAT;
            dbFieldType = "REAL";
        } else if (valueType == long.class) {
            fieldType = DBFieldType.LONG;
            dbFieldType = "LONG";
        } else if (valueType == DBBlobType.class) {
            fieldType = DBFieldType.BLOB;
            dbFieldType = "BLOB";
        }
        setDefaultValue();
    }

    public String getColumn() {
        return dbColumn;
    }

    public void setColumn(String dbColumn) {
        this.dbColumn = dbColumn;
    }

    public boolean isAutoIncrement() {
        return dbAutoIncrement;
    }

    public void setAutoIncrement(boolean dbAutoIncrement) {
        boolean allowAutoincrement = isPrimary && fieldType == DBFieldType.INT;
        this.dbAutoIncrement = allowAutoincrement ? dbAutoIncrement : false;
    }

    public String getDbFieldType() {
        return dbFieldType;
    }

    public String getDbFullFieldNameAndType() {
        String autoincrement = dbAutoIncrement ? "AUTOINCREMENT" : "";
        return dbColumn + " " + dbFieldType + ((isPrimary) ? " PRIMARY KEY " + autoincrement : "");
    }

    public void set(V value) {
        if (value instanceof byte[] && fieldType == DBFieldType.BLOB) {
            ((DBBlobType) this.value).bytes = (byte[]) value;
        } else {
            this.value = value;
        }
        modified = true;
    }

    public V get() {
        return value;
    }

    public boolean isModified() {
        if (modified) {
            modified = false;
            return true;
        }
        return modified;
    }

    public void putFieldInContentValues(ContentValues values) {
        switch (fieldType) {
            case INT:
                values.put(dbColumn, (Integer) value);
                break;
            case BOOLEAN:
                values.put(dbColumn, (Boolean) value);
                break;
            case STRING:
                values.put(dbColumn, (String) value);
                break;
            case FLOAT:
                values.put(dbColumn, (Float) value);
                break;
            case LONG:
                values.put(dbColumn, (Long) value);
                break;
            case BLOB:
                if (value instanceof DBBlobType) {
                    values.put(dbColumn, ((DBBlobType) value).bytes);
                } else {
                    values.put(dbColumn, (byte[]) value);
                }
                break;
        }
    }

    public void setDefaultValue() {
        switch (fieldType) {
            case INT:
                set((V) new Integer(0));
                break;
            case BOOLEAN:
                set((V) new Boolean(false));
                break;
            case STRING:
                set((V) new String(""));
                break;
            case FLOAT:
                set((V) new Float(0.0f));
                break;
            case LONG:
                set((V) new Long(0L));
                break;
            case BLOB:
                DBBlobType blobType = new DBBlobType();
                blobType.bytes = new byte[]{0};
                set((V) blobType);
                break;
        }
    }

    public <T extends DBField> String getStringValue() {

        switch (fieldType) {
            case INT:
                return String.valueOf(get());
            case BOOLEAN:
                boolean b = (Boolean) get();
                return String.valueOf(b ? 1 : 0);
            case STRING:
                return String.valueOf(get());
            case FLOAT:
                return String.valueOf(get());
            case LONG:
                return String.valueOf(get());
            case BLOB:
                return "" + ((DBBlobType) get()).bytes;
        }
        return "";
    }

}
