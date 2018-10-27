# Simple Database Library

THIS PROJECT IS A RESULT OF MY LEARNING PROCESS OF Android SQLLib (Older project). 

TODO: Unit test wasn't added currently, so it wasn't created using TDD.
TODO: improve test application

This is really simple database library for Android (simple yet powerful ;) ), which will allow you to create objects with fields
 represented as columns in SQLite database.

The library supports CRUD operations - so you can create, read, update and delete table rows in a really simple manner.

The library is in really early stages so it may evolve in the future.

There is also android test kotlin project with the library.

So, what you can do with the library? for example:

#### let's create test table DAO object

    public class TestTable extends DBDAOTableBase {
            
        // this will be primary field handled by database automatically 
        // because of autoincrement flag
        // when a field is autoincrement you will not set any value 
        // to it because the value will be generated automatically,
        // you should only read from this field (if you want to)
        // all fields have to be public
        @DBColumn(primaryKey = true, autoIncrement = true)
        DBFieldInteger id_key;
        
        @DBColumn
        DBFieldString message;
        
        @DBColumn
        DBFieldBlob image;
        
        @Override
        public String getTableName() {
            return "table_item";
        }
        
        // There are other field types available:
        // DBFieldBlob, DBFieldString, DBFieldLong, DBFieldBoolean, DBFieldFloat, DBFieldInteger,
    }

##### Now we can use it. Let's initialize the library

    // the last parameter is a class of our TestTable database table, We have to pass a list of classes from our custom
    // table types. So the las parameter is an array parameter. The Database manager will register it for easier usage 
    // inside the library
    DBManager dbManager = new DBManager(this, "testdatabase.db", TestTable.class);

##### You can now access all rows from the database

    List<TestTable> allRows = dbManager.getAllRows(TestTable.class);

##### You can create and add single row

    TestTable table = dbManager.createNewDAOTable(TestTable.class);
    table.message.set("some text");
        
    DBBlobType blob = new DBBlobType();
    blob.bytes = new byte[10000]; // you can put here some data
    table.image.set(blob);
        
    // now you can add the row
    dbManager.add(table);
        
    // or if you will add table without autoincrement field you can also
    table.id_key.set(10);
    dbManager.add(table, table.id_key);
        
##### To read table row, you should use

    dbManager.get(table, table.id_key);
           
##### You can also save changes which will automatically try to add the row if it does not exist or update existing row

    table.message.set("update row");
    dbManager.saveChanges(table);
    
##### To update single row:
    
    table.message.set("update row");
        
    // pass id_key primary field so the database will know where to find a row to update
    dbManager.update(table, table.id_key);
    
##### To delet row/rows:
    
    // pass id_key primary field so the database will know where to find a row to delete
    dbManager.delete(table, table.id_key);
    
    
I hope you will find this lib useful. I don't have time to improve it right now, but who knows what the future will bring :).

##### Proguard support

To compile with proguard add:

    -keep class com.simple.sqldatabase.** { *; }

### License

Simple Database Library is released under the [Apache 2.0 license](LICENSE).

   Copyright 2018 Stefan Wyszyński

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
