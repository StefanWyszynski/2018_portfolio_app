package com.simple.sqldatabase;

import com.simple.sqldatabase.fields.DBField;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public abstract class DBDAOTableBase {

    final List<DBField> dbFieldList;

    protected DBDAOTableBase() {
        dbFieldList = new ArrayList<>();
    }

    /**
     * @return iterator on declared DBFields in the class
     */
    public ListIterator<DBField> getDBFieldListIterator() {
        return dbFieldList.listIterator();
    }

    /**
     * @return list of declared DBFields
     */
    public List<DBField> getDBFieldList() {
        return dbFieldList;
    }

    /**
     * @return override to return your table name (tamplate method)
     */
    public abstract String getTableName();

}
