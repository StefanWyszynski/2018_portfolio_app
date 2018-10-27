package com.simple.sqldatabase;

import com.simple.sqldatabase.annotations.DBColumn;
import com.simple.sqldatabase.fields.DBBlobType;
import com.simple.sqldatabase.fields.DBField;
import com.simple.sqldatabase.fields.DBFieldBlob;
import com.simple.sqldatabase.fields.DBFieldBoolean;
import com.simple.sqldatabase.fields.DBFieldFloat;
import com.simple.sqldatabase.fields.DBFieldInteger;
import com.simple.sqldatabase.fields.DBFieldLong;
import com.simple.sqldatabase.fields.DBFieldString;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DBFieldRegistry {

    private static DBFieldRegistry instance;
    private Map<String, Class> registry;

    private DBFieldRegistry() {
        registry = new HashMap<>(1);
    }

    public static DBFieldRegistry getInstance() {
        if (instance == null) {
            instance = new DBFieldRegistry();
        }
        return instance;
    }

    public <T extends DBDAOTableBase> void registerClass(Class<T> sqlDatabaseDAOTable) {
        String name = sqlDatabaseDAOTable.getName();
        if (!registry.containsKey(name)) {
            registry.put(name, sqlDatabaseDAOTable);
        }
    }

    public <T extends DBDAOTableBase> List<T> getRegistredClasses() {
        Set<String> keySet = registry.keySet();
        List<T> daoList = new ArrayList<>();
        for (String className : keySet) {
            T daoTableObject = (T) createNewDAOTableObject(registry.get(className));
            daoList.add(daoTableObject);
        }
        return daoList;
    }

    public <T extends DBDAOTableBase> T createNewDAOTableObject(Class<T> sqlDatabaseDAOTable) {
        T sqlDatabaseDAOTableInstance = instantiateDAOTable(sqlDatabaseDAOTable);
        initializeDAOFields(sqlDatabaseDAOTableInstance);
        return sqlDatabaseDAOTableInstance;
    }

    private <T extends DBDAOTableBase> T instantiateDAOTable(Class<T> sqlDatabaseDAOTable) {
        try {
            return sqlDatabaseDAOTable.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    private <T extends DBDAOTableBase> void initializeDAOFields(T sqlDatabaseDAOTable) {
        Class<? extends DBDAOTableBase> daoObjectClass = sqlDatabaseDAOTable.getClass();
        Field[] fields = daoObjectClass.getDeclaredFields();
        List<DBField> dbFieldList = sqlDatabaseDAOTable.getDBFieldList();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            DBColumn dbColumn = field.getAnnotation(DBColumn.class);
            if (dbColumn != null) {
                boolean isPrimary = dbColumn.primaryKey();
                boolean autoIncrement = dbColumn.autoIncrement();
                String columnName = field.getName();
                DBField dbField = initializeDBField(field, columnName, isPrimary);
                dbField.setAutoIncrement(autoIncrement);
                try {
                    // assign new databaseField instance to member variable
                    if (field.isAccessible())
                        field.set(sqlDatabaseDAOTable, dbField);
                    else {
                        field.setAccessible(true);
                        field.set(sqlDatabaseDAOTable, dbField);
                        field.setAccessible(false);
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                dbFieldList.add(dbField);
            }
        }
    }

    private DBField initializeDBField(Field field, String columnName, boolean isPrimary) {
        Class<?> fieldType = field.getType();

        if (fieldType == DBFieldBoolean.class) {
            return new DBFieldBoolean(columnName, boolean.class, isPrimary);
        } else if (fieldType == DBFieldInteger.class) {
            return new DBFieldInteger(columnName, int.class, isPrimary);
        } else if (fieldType == DBFieldFloat.class) {
            return new DBFieldFloat(columnName, float.class, isPrimary);
        } else if (fieldType == DBFieldLong.class) {
            return new DBFieldLong(columnName, long.class, isPrimary);
        } else if (fieldType == DBFieldString.class) {
            return new DBFieldString(columnName, String.class, isPrimary);
        } else if (fieldType == DBFieldBlob.class) {
            return new DBFieldBlob(columnName, DBBlobType.class, isPrimary);
        }
        return null;
    }

//    public void forEachDBField(DBDAOTableBase dbdaoTableBase, IForEachFieldCallback callback) {
//        Class<?> daoObjectClass = dbdaoTableBase.getClass();
//        Field[] fields = daoObjectClass.getDeclaredFields();
//        DBField prevField = null;
//        for (int i = 0; i < fields.length; i++) {
//            Field field = fields[i];
//            DBColumn dbColumn = field.getAnnotation(DBColumn.class);
//            if (dbColumn != null) {
//                boolean isPrimary = dbColumn.primaryKey();
//                boolean autoIncrement = dbColumn.autoIncrement();
//                DBField dbField = null;
//
//                try {
//                    dbField = (DBField) field.get(dbdaoTableBase);
//                } catch (IllegalAccessException e) {
//                    e.printStackTrace();
//                }
//                if (callback != null) {
//                    callback.onDBFieldFound(field, dbField, isPrimary, autoIncrement);
//                }
//                dbField.setPrevField(prevField);
//                if (prevField != null) {
//                    prevField.setNextField(dbField);
//                }
//                prevField = dbField;
//            }
//        }
//    }
//
//    public interface IForEachFieldCallback {
//        void onDBFieldFound(Field field, DBField dbField, boolean isPrimaryKey, boolean autoIncrement);
//    }

}
