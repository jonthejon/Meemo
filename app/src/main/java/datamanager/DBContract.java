package datamanager;

import android.provider.BaseColumns;

/**
 * This class defines constants that help applications work with your DB.
 */

public class DBContract {

//    IV that holds the database name
    static final String DATABASE_NAME = "MeemoDatabase.db";
//    IV that holds the present database version and must be incremented every time a database scheme changes
    static final int DATABASE_VERSION = 1;

    public static final class MemoryTable implements BaseColumns {

//        inner final IV that holds the memory table name
        public static final String TABLE_NAME = "MEMORY_TABLE";
//        inner final IV that holds the unique memory ID inside this table
        public static final String COL_MEMORY_ID = "_ID";
//        inner final IV that holds the actual memory text
        public static final String COL_MEMORY_TEXT = "MEMORY_TEXT";
//        inner final IV that holds the file path of the memory file if it exists
        public static final String COL_MEMORY_FILE_PATH = "MEMORY_FILE_PATH";
//        inner final IV that holds the time that the memory got created
        public static final String COL_CREATION_TIME = "MEMORY_TIMESTAMP";
    }

    public static final class FamilyTable implements BaseColumns {

//        inner final IV that holds the family table name
        public static final String TABLE_NAME = "FAMILY_TABLE";
//        inner final IV that holds the unique memory ID inside this table
//        DON'T CONFUSE IT WITH THE ACTUAL MEMORY ID THAT IS STORED IN ANOTHER COLUMN
        public static final String COL_UNIQUE_ROW = "_ID";
//        inner final IV that holds the actual memory ID of the parent memory
        public static final String COL_MEMORY_ID = "PARENT_ID";
//        inner final IV that holds the actual memory ID of the child memory
        public static final String COL_CHILD_MEMORY_ID = "CHILD_ID";
    }
}
