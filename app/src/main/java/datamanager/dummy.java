package datamanager;

/**
 * This class must reside inside the CORE package because it holds information that is business relevant data and is not Platform dependent
 * In here we'll have IVs organized into inner classes that define the Database, the ContentProvider and all the tables and columns of the database of the app.
 * Only Strings will reside inside this class. Any Uri is formed inside the DBContract class of the DATAMANAGER package using the Uri API
 */

public class dummy {

    /**
     * this inner class contains all the data that defines the database and data that allows to form the Uri to access the ContentProvider
     */
    public static final class Database {
        //    IV that holds the database name
        static final String DATABASE_NAME = "MeemoDatabase.db";
        //    IV that holds the present database version and must be incremented every time a database scheme changes
        static final int DATABASE_VERSION = 7;
        //    The authority, which is how your code knows which Content Provider to access
        //    this is defined inside the AndroidManifest file and probably is your package name
        static final String AUTHORITY = "seasonedblackolives.com.meemo";
        //    this is the initial piece of every Uri that points to your Content Provider
        static final String INIT_CONTENT = "content://";
        //    Define the possible paths for accessing data in the database
        static final String PATH_MEMORY = "memory";
        static final String PATH_GET = "get";
        static final String PATH_INSERT = "insert";
        static final String PATH_DELETE = "delete";
        static final String PATH_UPDATE = "update";
    }

    /**
     * this inner class holds all the columns names of the MemoryTable of the database
     */
    public static class MemoryTable {
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

    /**
     * this inner class holds all the columns names of the ConnectionTable of the database
     */
    public static class ConnectionTable {
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
