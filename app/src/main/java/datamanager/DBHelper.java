package datamanager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * class that will implement the database based on the @DBContract.java class
 */

public class DBHelper extends SQLiteOpenHelper {

    private Context context;

    public DBHelper(Context context) {
//        calling the SQLiteOpenHelper constructor and sending him the parameters that it needs from the contract class
//        this call will ensure that the database will be instantiated or created depending on the version number
        super(context, DBContract.DATABASE_NAME, null, DBContract.DATABASE_VERSION);
//        setting the context IV of this class with the given parameter
        this.context = context;
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
                DBContract.FamilyTable.COL_UNIQUE_ROW + " " + "INTEGER PRIMARY KEY AUTOINCREMENT," + " " +
//                this ID must not be null and will match one specific primary key of the memory table
                DBContract.FamilyTable.COL_MEMORY_ID + " " + "INTEGER NOT NULL," + " " +
//                this ID must not be null and will match one specific primary key of the memory table
                DBContract.FamilyTable.COL_CHILD_MEMORY_ID + " " + "INTEGER NOT NULL" +
                ");";

        String[] fakeStringArr = {"BRAIN TEST",
        "Memory number 2",
        "Memory number 3",
        "Memory number 4",
        "Memory number 5",
        "Memory number 6"};

//        This is the String that holds the SQL statement for inserting the first memory into the DB
        final String SQL_INSERT_FIRST_MEMORY = "INSERT INTO" + " " + DBContract.MemoryTable.TABLE_NAME +  " " +
                "(" + DBContract.MemoryTable.COL_MEMORY_TEXT + ")" + " " +
                "VALUES('" + fakeStringArr[0] + "');";

        final String SQL_INSERT_SECOND_MEMORY = "INSERT INTO" + " " + DBContract.MemoryTable.TABLE_NAME +  " " +
                "(" + DBContract.MemoryTable.COL_MEMORY_TEXT + ")" + " " +
                "VALUES('" + fakeStringArr[1] + "');";

        final String SQL_INSERT_THIRD_MEMORY = "INSERT INTO" + " " + DBContract.MemoryTable.TABLE_NAME +  " " +
                "(" + DBContract.MemoryTable.COL_MEMORY_TEXT + ")" + " " +
                "VALUES('" + fakeStringArr[2] + "');";

        final String SQL_INSERT_FOURTH_MEMORY = "INSERT INTO" + " " + DBContract.MemoryTable.TABLE_NAME +  " " +
                "(" + DBContract.MemoryTable.COL_MEMORY_TEXT + ")" + " " +
                "VALUES('" + fakeStringArr[3] + "');";

        final String SQL_INSERT_FIFTH_MEMORY = "INSERT INTO" + " " + DBContract.MemoryTable.TABLE_NAME +  " " +
                "(" + DBContract.MemoryTable.COL_MEMORY_TEXT + ")" + " " +
                "VALUES('" + fakeStringArr[4] + "');";

        final String SQL_INSERT_SIXTH_MEMORY = "INSERT INTO" + " " + DBContract.MemoryTable.TABLE_NAME +  " " +
                "(" + DBContract.MemoryTable.COL_MEMORY_TEXT + ")" + " " +
                "VALUES('" + fakeStringArr[5] + "');";

//        executing the creation of the memory table
        db.execSQL(SQL_CREATE_MEMORY_TABLE);
//        executing the creation of the family table
        db.execSQL(SQL_CREATE_FAMILY_TABLE);
//        inserting the first memory into the DB. This will be the parent of all other memories.
        db.execSQL(SQL_INSERT_FIRST_MEMORY);
        db.execSQL(SQL_INSERT_SECOND_MEMORY);
        db.execSQL(SQL_INSERT_THIRD_MEMORY);
        db.execSQL(SQL_INSERT_FOURTH_MEMORY);
        db.execSQL(SQL_INSERT_FIFTH_MEMORY);
        db.execSQL(SQL_INSERT_SIXTH_MEMORY);
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
