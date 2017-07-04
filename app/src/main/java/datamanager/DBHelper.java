package datamanager;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * class that will implement the database based on the @DBContract.java class
 */

class DBHelper extends SQLiteOpenHelper {

    DBHelper(Context context) {
//        calling the SQLiteOpenHelper constructor and sending him the parameters that it needs from the contract class
//        this call will ensure that the database will be instantiated or created depending on the version number
        super(context, DBContract.DATABASE_NAME, null, DBContract.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

//        this String holds the SQL statement for creating the memory table using the information from the contract class
        final String SQL_CREATE_MEMORY_TABLE = "CREATE TABLE" +  " " + DBContract.MemoryTable.TABLE_NAME + " (" +
//                setting the ID as the primary key. Therefore it will autoincrement and must not be set by the user
                DBContract.MemoryTable.COL_MEMORY_ID +  " " + "INTEGER PRIMARY KEY," + " " +
//                setting the memory content as a text that cannot be null. This will ensure that we are not creating an empty memory
                DBContract.MemoryTable.COL_MEMORY_TEXT +  " " + "TEXT NOT NULL," + " " +
//                the file path of a memory can be true if it does not have a file attached to it. Setting the default value to 'void' in that case
                DBContract.MemoryTable.COL_MEMORY_FILE_PATH +  " " + "VARCHAR(500) DEFAULT 'void'," + " " +
//                the user should also not set the memory creation time. This will be done automatically by the database
                DBContract.MemoryTable.COL_CREATION_TIME +  " " + "TEXT DEFAULT CURRENT_TIMESTAMP" +
                ");";

//        this String holds the SQL statement for creating the family table using the information from the contract class
        final String SQL_CREATE_FAMILY_TABLE = "CREATE TABLE" + " " + DBContract.FamilyTable.TABLE_NAME + " (" +
//                creating a unique id field for easy information access
                DBContract.FamilyTable.COL_UNIQUE_ROW + " " + "INTEGER PRIMARY KEY," + " " +
//                this ID must not be null and will match one specific primary key of the memory table
                DBContract.FamilyTable.COL_PARENT_MEMORY_ID + " " + "INTEGER NOT NULL," + " " +
//                this ID must not be null and will match one specific primary key of the memory table
                DBContract.FamilyTable.COL_CHILD_MEMORY_ID + " " + "INTEGER NOT NULL" +
                ");";
//        fake array containing the first memories to be inserted inside the DB for testing purposes
        String[] fakeStringArr = {"BRAIN TEST",
        "Child Memory 1",
        "Child Memory 2",
        "Child Memory 3",
        "Child Memory 4",
        "Child Memory 5"};

//        This is the String that holds the SQL statement for inserting the first memory into the DB
        final String SQL_INSERT_FIRST_MEMORY = "INSERT INTO" + " " + DBContract.MemoryTable.TABLE_NAME +  " " +
                "(" + DBContract.MemoryTable.COL_MEMORY_TEXT + ")" + " " +
                "VALUES('" + fakeStringArr[0] + "');";
//        This is the String that holds the SQL statement for inserting the second memory into the DB
        final String SQL_INSERT_SECOND_MEMORY = "INSERT INTO" + " " + DBContract.MemoryTable.TABLE_NAME +  " " +
                "(" + DBContract.MemoryTable.COL_MEMORY_TEXT + ")" + " " +
                "VALUES('" + fakeStringArr[1] + "');";
//        This SQL is the query for retrieving the first 2 memories from the DB that just got created
        final String SQL_GET_FIRSTS_MEMORY_IDS = "SELECT" + " " + DBContract.MemoryTable.COL_MEMORY_ID + " " +
                "FROM" + " " + DBContract.MemoryTable.TABLE_NAME;

//        executing the creation of the memory table
        db.execSQL(SQL_CREATE_MEMORY_TABLE);
//        executing the creation of the family table
        db.execSQL(SQL_CREATE_FAMILY_TABLE);
//        inserting the first memory into the DB. This will be the parent of all other memories.
        db.execSQL(SQL_INSERT_FIRST_MEMORY);
//        inserting the second memory into the DB.
        db.execSQL(SQL_INSERT_SECOND_MEMORY);

//        this code block here is just getting the information from the first table so we can populate properly the second table
//        getting a Cursor with the result after getting from the memory table the first 2 memories
        Cursor cursor = db.rawQuery(SQL_GET_FIRSTS_MEMORY_IDS, null);
//        getting the index of the column _ID from the cursor
        int id_Col_index = cursor.getColumnIndex(DBContract.MemoryTable.COL_MEMORY_ID);
//        jumping the cursor to the first position so we can start reading
        cursor.moveToNext();
//        reading the ID of the first memory (the mother memory)
        int first_id = cursor.getInt(id_Col_index);
//        moving the cursor to the next position for reading
        cursor.moveToNext();
//        reading the ID of the second memory
        int second_id = cursor.getInt(id_Col_index);
//        closing the cursor so we don't have a memory leak
        cursor.close();

//        SQL command to insert the proper relationship between the mother and the second memory into the family table
        final String SQL_INSERT_FIRST_FAMILY_RELATION = "INSERT INTO" + " " + DBContract.FamilyTable.TABLE_NAME +  " " +
                "(" + DBContract.FamilyTable.COL_PARENT_MEMORY_ID + ", " +
                DBContract.FamilyTable.COL_CHILD_MEMORY_ID + ")" + " " +
                "VALUES(" + "'" + first_id + "'" + ", " + "'" + second_id + "'" + ");";
//        executing the SQL so we can proper populate the family table
        db.execSQL(SQL_INSERT_FIRST_FAMILY_RELATION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // For now simply drop the tables and create new ones. This means if you change the
        // DATABASE_VERSION the table will be dropped!!
        // In a production app, this method might be modified to ALTER the table
        // instead of dropping it, so that existing data is not deleted.
        db.execSQL("DROP TABLE IF EXISTS " + DBContract.MemoryTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DBContract.FamilyTable.TABLE_NAME);
        onCreate(db);
    }
}
