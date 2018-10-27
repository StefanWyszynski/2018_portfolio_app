package com.simple.sqldatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.simple.sqldatabase.base.DBQueryHelper;
import com.simple.sqldatabase.fields.DBField;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class DBManager extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    /**
     * @param context                 this could be for example activity or application context
     * @param databaseName            name for your database (you can postfix it with ".db" extension)
     * @param tablesClassesToRegister pass all your classes inherited from DBDAOTableBase, so the library will know
     *                                how to handle database operations on these objects
     */
    public <T extends DBDAOTableBase> DBManager(Context context, String databaseName,
                                                Class<T>... tablesClassesToRegister) {
        super(context, databaseName, null, DATABASE_VERSION);

        DBFieldRegistry dbFieldRegistry = DBFieldRegistry.getInstance();
        for (Class<T> tClass : tablesClassesToRegister) {
            dbFieldRegistry.registerClass(tClass);
        }
    }

    // Upgrading database
    private <T extends DBDAOTableBase> void onSQLUpgrade(SQLiteDatabase db, T tableObject, int oldVersion, int
            newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + tableObject.getTableName());

        // Create tables again
        onSQLCreate(db, tableObject);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        List<DBDAOTableBase> list = DBFieldRegistry.getInstance().getRegistredClasses();
        for (DBDAOTableBase dbdaoTableBase : list) {
            onSQLCreate(db, dbdaoTableBase);
        }
    }

    // Creating Tables
    private <T extends DBDAOTableBase> void onSQLCreate(SQLiteDatabase db, T tableObject) {
        StringBuilder sqlCreateTableStr = new StringBuilder();
        sqlCreateTableStr.append("CREATE TABLE ");
        sqlCreateTableStr.append(tableObject.getTableName());
        sqlCreateTableStr.append("(");

        ListIterator<DBField> it = tableObject.getDBFieldListIterator();
        while (it.hasNext()) {
            DBField dbField = it.next();
            sqlCreateTableStr.append(dbField.getDbFullFieldNameAndType());
            if (it.hasNext()) sqlCreateTableStr.append(",");
        }
        sqlCreateTableStr.append(")");

        db.execSQL(sqlCreateTableStr.toString());
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        List<DBDAOTableBase> list = DBFieldRegistry.getInstance().getRegistredClasses();
        for (DBDAOTableBase dbdaoTableBase : list) {
            onSQLUpgrade(db, dbdaoTableBase, oldVersion, newVersion);
        }
    }

    public <T extends DBDAOTableBase> T createNewDAOTable(Class<T> tClass) {
        return DBFieldRegistry.getInstance().createNewDAOTableObject(tClass);
    }

    private <T extends DBDAOTableBase> String[] getFieldMapColumns(T tableObject) {
        ListIterator<DBField> it = tableObject.getDBFieldListIterator();
        List<DBField> fields = new ArrayList<>();
        while (it.hasNext()) {
            fields.add(it.next());
        }
        String[] strings = new String[fields.size()];
        for (int i = 0; i < strings.length; i++) {
            strings[i] = fields.get(i).getColumn();
        }
        return strings;
    }

    /**
     * @return number of rows of specified by DBDAOTableBase object
     */
    public <T extends DBDAOTableBase> int getRowsCount(T tableObject) {
        int count = 0;
        try {
            String countQuery = "SELECT * FROM " + tableObject.getTableName();
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = null;
            try {
                cursor = db.rawQuery(countQuery, null);
                count = cursor.getCount();
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
                if (db != null) db.close(); // Closing database connection
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    /**
     * Add your DBDAOTableBase object instance to the database
     *
     * @param tableObject your DAOTableBase inherided class instance
     */
    public <T extends DBDAOTableBase> void add(T tableObject) {
        SQLiteDatabase db = getWritableDatabase();
        DBField autoIncrementField = null;
        long lastRowID;
        try {
            ContentValues values = new ContentValues();
            ListIterator<DBField> it = tableObject.getDBFieldListIterator();
            while (it.hasNext()) {
                DBField dbField = it.next();
                if (!dbField.isAutoIncrement()) {
                    dbField.putFieldInContentValues(values);
                } else {
                    if (autoIncrementField == null) {
                        autoIncrementField = dbField;
                    }
                }
            }

            // Inserting Row
            lastRowID = db.insert(tableObject.getTableName(), null, values);
        } finally {
            if (db != null) db.close(); // Closing database connection
        }
        if (lastRowID >= 0 && autoIncrementField != null) {
            autoIncrementField.set(lastRowID);
            get(tableObject, autoIncrementField);
        }
    }

    /**
     * assign field from db to the instance of your specified DBDAOTableBase object
     *
     * @param tableObject       your DAOTableBase inherided class instance
     * @param whereClauseFields put here any primary key or other fields from your DAO table (from the same object which
     *                          was passed to tableObject parameter). For example, you can pass one or more primary keys
     *                          represented by DBField from your DBDAOTableBase object. Database query will be based on
     *                          these fields. if you pass null then the first row of the database query will be assigned
     *                          to your DAO table  passed in the tableObject parameter
     * @return
     */
    public <T extends DBDAOTableBase> boolean get(T tableObject, DBField... whereClauseFields) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = null;
        try {
            DBQueryHelper.prepareQueryForGet(tableObject, whereClauseFields);
            String tableName = tableObject.getTableName();
            String[] columns = null;//getFieldMapColumns();
            cursor = db.query(tableName, columns, DBQueryHelper.getWhereClause(), DBQueryHelper.getWhereArgs(),
                    null,
                    null, null, null);
//                String sel = "SELECT * FROM " + tableName + " WHERE " + keys;
//                cursor = db.rawQuery(sel, selectionArgs);
            if (cursor != null && cursor.moveToFirst()) {
                assignDBFieldsFromCursor(tableObject, cursor);
                return true;
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) db.close(); // Closing database connection
        }
        return false;
    }

    /**
     * Update rows based on fields from DAO table instance passed in whereClasueField argumet
     * <p>
     * example usage: int numRowsUpdated = update(myDAOTable, myDAOTable.my_primary_field_id);
     * will update all rows in the database which values equals primary field id.
     *
     * @param tableObject       your DAOTableBase inherided class instance
     * @param whereClauseFields put here any primary key or other fields from your DAO table (from the same object which
     *                          was passed to tableObject parameter). For example, you can pass one or more primary keys
     *                          represented by DBField from your DBDAOTableBase object. if you pass null then all rows
     *                          from your database will be updated based on a DAO table object from the first parameter
     *                          tableObject.
     * @return number of updated rows
     */
    public <T extends DBDAOTableBase> int update(T tableObject, DBField... whereClauseFields) {
        int updatedRowsCount = 0;
        try {
            SQLiteDatabase db = getWritableDatabase();
            try {
                DBQueryHelper.prepareQuery(tableObject, whereClauseFields);
                updatedRowsCount = db.update(tableObject.getTableName(), DBQueryHelper.getContentValues(), DBQueryHelper
                        .getWhereClause(), DBQueryHelper.getWhereArgs());
            } finally {
                if (db != null) db.close(); // Closing database connection
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return updatedRowsCount;
    }

    /**
     * Saves your table object as row in the database. The row will be updated if it already exists.
     *
     * @param tableObject your DAOTableBase inherided class instance
     */
    public <T extends DBDAOTableBase> void saveChanges(T tableObject) {
        ListIterator<DBField> it = tableObject.getDBFieldListIterator();
        List<DBField> primaryFields = new ArrayList<>();
        while (it.hasNext()) {
            DBField dbField = it.next();
            if (dbField.isPrimary()) {
                primaryFields.add(dbField);
            }
        }

        DBField[] primFields = primaryFields.toArray(new DBField[primaryFields.size()]);
        boolean exists = contains(tableObject, primFields);
        if (exists) {
            update(tableObject, primFields);
        } else {
            add(tableObject);
        }
    }

    /**
     * @param tClass your DAOTableBase inherided class
     * @return all rows from the database of type of DAOTable class passed as tClass parameter
     */
    public <T extends DBDAOTableBase> List<T> getAllRows(Class<T> tClass) {
        List<T> rowsFromDatabase = new ArrayList<>();
        // Select All Query
        T databaseRow = createNewDAOTable(tClass);
        String selectQuery = "SELECT * FROM " + databaseRow.getTableName();

        try {
            SQLiteDatabase db = getWritableDatabase();
            Cursor cursor = null;
            try {
                cursor = db.rawQuery(selectQuery, null);

                // looping through all rows and adding to list
                if (cursor.moveToFirst()) {
                    do {
                        databaseRow = (T) createNewDAOTable(tClass);
                        assignDBFieldsFromCursor(databaseRow, cursor);
                        // Adding row to list
                        rowsFromDatabase.add(databaseRow);
                    } while (cursor.moveToNext());
                }

            } finally {
                if (cursor != null) {
                    cursor.close();
                }
                db.close(); // Closing database connection
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // return contact list
        return rowsFromDatabase;
    }

    protected <T extends DBDAOTableBase> void assignDBFieldsFromCursor(T tableObject, Cursor cursor) {
        int columnCount = cursor.getColumnCount();
        for (int i = 0; i < columnCount; i++) {
            ListIterator<DBField> it = tableObject.getDBFieldListIterator();
            DBField dbField = null;
            String columnName = cursor.getColumnName(i);
            while (it.hasNext()) {
                DBField currentDBField = it.next();
                if (currentDBField.getColumn().equalsIgnoreCase(columnName)) {
                    dbField = currentDBField;
                    break;
                }
            }
            if (dbField != null) {
                DBField.setFromDatabaseCursor(dbField, cursor, cursor.getColumnIndex(columnName));
            }
        }
    }

    public <T extends DBDAOTableBase> boolean contains(T tableObject, DBField... primaryField) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = null;
        String tableName = tableObject.getTableName();
        try {
            DBQueryHelper.prepareQuery(tableObject, primaryField);
            cursor = db.query(tableName, null, DBQueryHelper.getWhereClause(),
                    DBQueryHelper.getWhereArgs(), null, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                return true;
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) db.close(); // Closing database connection
        }
        return false;
    }

    public int delete(DBDAOTableBase dbdaoTableBase, DBField primaryField) {
        int numRows = 0;
        try {
            SQLiteDatabase db = getWritableDatabase();
            try {
                DBQueryHelper.prepareQuery(dbdaoTableBase, primaryField);
                numRows = db.delete(dbdaoTableBase.getTableName(), DBQueryHelper.getWhereClause(), DBQueryHelper
                        .getWhereArgs());
            } finally {
                if (db != null) db.close(); // Closing database connection
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return numRows;
    }
}