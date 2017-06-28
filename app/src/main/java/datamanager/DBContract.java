package datamanager;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * This class defines constants that help applications work with your DB.
 */

class DBContract {

//    IV that holds the database name
    static final String DATABASE_NAME = "MeemoDatabase.db";
//    IV that holds the present database version and must be incremented every time a database scheme changes
    static final int DATABASE_VERSION = 1;

//    The authority, which is how your code knows which Content Provider to access
    static final String AUTHORITY = "seasonedblackolives.com.meemo";
//    The base content URI = "content://" + <authority>
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

//    Define the possible paths for accessing data in this contract
    static final String PATH_MEMORY = "memory";
    public static final String PATH_FAMILY = "family";
    static final String PATH_GET = "get";
    static final String PATH_INSERT = "insert";
    static final String PATH_DELETE = "delete";
    static final String PATH_UPDATE = "update";

    static final class MemoryTable implements BaseColumns {

//        variable that will store the URI used by the Content Provider to get memories from this table
        static final Uri GET_MEMORY_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MEMORY).appendPath(PATH_GET).build();
//        variable that will store the URI used by the Content Provider to insert memories into this table
        static final Uri INSERT_MEMORY_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MEMORY).appendPath(PATH_INSERT).build();
//        variable that will store the URI used by the Content Provider to delete memories from this table
        static final Uri DELETE_MEMORY_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MEMORY).appendPath(PATH_DELETE).build();
//        variable that will store the URI used by the Content Provider to update memories from this table
        static final Uri UPDATE_MEMORY_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MEMORY).appendPath(PATH_UPDATE).build();

//        inner final IV that holds the memory table name
        static final String TABLE_NAME = "MEMORY_TABLE";
//        inner final IV that holds the unique memory ID inside this table
        static final String COL_MEMORY_ID = "_ID";
//        inner final IV that holds the actual memory text
        static final String COL_MEMORY_TEXT = "MEMORY_TEXT";
//        inner final IV that holds the file path of the memory file if it exists
        static final String COL_MEMORY_FILE_PATH = "MEMORY_FILE_PATH";
//        inner final IV that holds the time that the memory got created
        static final String COL_CREATION_TIME = "MEMORY_TIMESTAMP";
    }

    static final class FamilyTable implements BaseColumns {

//        inner final IV that holds the family table name
        static final String TABLE_NAME = "FAMILY_TABLE";
//        inner final IV that holds the unique memory ID inside this table
//        DON'T CONFUSE IT WITH THE ACTUAL MEMORY ID THAT IS STORED IN ANOTHER COLUMN
        static final String COL_UNIQUE_ROW = "_ID";
//        inner final IV that holds the actual memory ID of the parent memory
        static final String COL_MEMORY_ID = "PARENT_ID";
//        inner final IV that holds the actual memory ID of the child memory
        static final String COL_CHILD_MEMORY_ID = "CHILD_ID";
    }
}
