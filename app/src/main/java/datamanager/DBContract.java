package datamanager;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * This class defines constants that help applications work with your DB.
 */

public class DBContract {

//    IV that holds the database name
    static final String DATABASE_NAME = "MeemoDatabase.db";
//    IV that holds the present database version and must be incremented every time a database scheme changes
    static final int DATABASE_VERSION = 7;

//    The authority, which is how your code knows which Content Provider to access
//    this is defined inside the AndroidManifest file and probably is your package name
    static final String AUTHORITY = "seasonedblackolives.com.meemo";

//    this is the initial piece of every Uri that points to your Content Provider
    static final String INIT_CONTENT = "content://";

//    The base content URI = "content://" + <authority>
//    The base Uri that will be used to connect to the ContentProvider of this app
//    private static final Uri BASE_CONTENT_URI = Uri.parse(dummy.Database.INIT_CONTENT + dummy.Database.AUTHORITY);
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

//    Define the possible paths for accessing data in this contract
    static final String PATH_MEMORY = "memory";
    static final String PATH_GET = "get";
    static final String PATH_INSERT = "insert";
    static final String PATH_DELETE = "delete";
    static final String PATH_UPDATE = "update";


    public static final class MemoryTable implements BaseColumns {
//        variable that will store the URI used by the Content Provider to get memories from this table
        public static final Uri GET_MEMORY_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MEMORY).appendPath(PATH_GET).build();
//        variable that will store the URI used by the Content Provider to insert memories into this table
        public static final Uri INSERT_MEMORY_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MEMORY).appendPath(PATH_INSERT).build();
//        variable that will store the URI used by the Content Provider to delete memories from this table
        public static final Uri DELETE_MEMORY_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MEMORY).appendPath(PATH_DELETE).build();
//        variable that will store the URI used by the Content Provider to update memories from this table
        public static final Uri UPDATE_MEMORY_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MEMORY).appendPath(PATH_UPDATE).build();

//        inner final IV that holds the memory table name
        public static final String TABLE_NAME = "memory_table";
//        inner final IV that holds the unique memory ID inside this table
        public static final String COL_MEMORY_ID = "_ID";
//        inner final IV that holds the actual memory text
        public static final String COL_MEMORY_TEXT = "memory_text";
//        inner final IV that holds the file path of the memory file if it exists
        public static final String COL_MEMORY_FILE_PATH = "memory_file_path";
//        inner final IV that holds the time that the memory got created
        public static final String COL_CREATION_TIME = "memory_timestamp";
    }

    public static final class ConnectionTable implements BaseColumns {

//        inner final IV that states the code for a composition relation between two memories
        public static final int COMPOSITION_TYPE = 11;
//        inner final IV that states the code for a relational relation between two memories
        public static final int RELATIONAL_TYPE = 23;

//        inner final IV that holds the connection table name
        public static final String TABLE_NAME = "connection_table";
//        inner final IV that holds the actual memory ID of the first memory of this relation
        public static final String COL_MEMORY_A = "memory_a";
//        inner final IV that holds the actual memory ID of the second memory of this relation
        public static final String COL_MEMORY_B = "memory_b";
//        inner final IV that holds the type of connection that exists between the 2 memories represented in a row
        public static final String COL_CONNECTION_TYPE = "type";
    }

}
