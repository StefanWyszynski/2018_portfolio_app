package com.simple.sqldatabase.base;

import android.content.ContentValues;

import com.simple.sqldatabase.DBDAOTableBase;
import com.simple.sqldatabase.fields.DBField;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/***
 * DBQueryHelper is a helper class used by this library - so you don't have to use it.
 * Its purpose is to prepare arguments for database query function.
 * The arguments are prepared based on DBFields passed as argument to prepareQuery(...) function.
 *
 * Example of usage:
 *
 * DBDAOTableBase db..
 * DBQueryHelper.prepareQuery(db, db.id, db.address, db.postcode);
 * DBQueryHelper.getWhereArgs();
 * DBQueryHelper.getWhereClause();
 * DBQueryHelper.getContentValues();
 *
 */
public class DBQueryHelper {
    private static String[] whereArgs;
    private static String whereClause;
    private static ContentValues contentValues;

    public static String[] getWhereArgs() {
        return whereArgs;
    }

    public static String getWhereClause() {
        return whereClause;
    }

    public static ContentValues getContentValues() {
        return contentValues;
    }

    public static void prepareQuery(DBDAOTableBase dbdaoTableBase, DBField... whereClauseFields) {
        ListIterator<DBField> it = dbdaoTableBase.getDBFieldListIterator();
        whereClause = "";

        contentValues = new ContentValues();

        // build whereClause string from table fields
        List<String> keysValues = new ArrayList<>();
        while (it.hasNext()) {
            DBField dbField = it.next();
            boolean found = false;
            for (DBField whereClauseField : whereClauseFields) {
                if (whereClauseField.getColumn().equals(dbField.getColumn())) { // add primary key column
                    // name and value
                    if (whereClause.length() > 0) {
                        whereClause += ", ";
                    }
                    whereClause += dbField.getColumn() + " = ?";
                    keysValues.add(dbField.getStringValue());
                    found = true;
                    break;
                }
            }
            if (!found) {
                dbField.putFieldInContentValues(contentValues);
            }
        }

        if (!keysValues.isEmpty()) {
            whereArgs = keysValues.toArray(new String[keysValues.size()]);
        } else {
            whereClause = null;
            whereArgs = null;
        }
    }

    public static void prepareQueryForGet(DBDAOTableBase dbdaoTableBase, DBField... whereClauseFields) {
        prepareQuery(dbdaoTableBase, whereClauseFields);
        if (whereClause != null && whereClause.length() > 0) {
            whereClause.replaceAll(", ", " AND ");
        }
    }
}