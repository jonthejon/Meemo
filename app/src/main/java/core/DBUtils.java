package core;

import static core.DBUtils.ConnectionTable.COL_MEMORY_A;
import static core.DBUtils.ConnectionTable.COL_MEMORY_B;
import static core.DBUtils.MemoryTable.COL_MEMORY_ID;
import static core.DBUtils.MemoryTable.COL_MEMORY_NUM_CONN;
import static core.DBUtils.MemoryTable.COL_MEMORY_TEXT;

/**
 * This class must reside inside the CORE package because it holds information that is business relevant data and is not Platform dependent
 * In here we'll have IVs organized into inner classes that define the Database, the ContentProvider and all the tables and columns of the database of the app.
 * Only Strings will reside inside this class. Any Uri is formed inside the DBContract class of the DATAMANAGER package using the Uri API
 */

public class DBUtils {
    /**
     * this inner class contains all the data that defines the database and data that allows to form the Uri to access the ContentProvider
     */
    public static class Database {
        //    IV that holds the database name
        public static final String DATABASE_NAME = "MeemoDatabase.db";
        //    IV that holds the present database version and must be incremented every time a database scheme changes
        public static final int DATABASE_VERSION = 9;
        //    The authority, which is how your code knows which Content Provider to access
        //    this is defined inside the AndroidManifest file and probably is your package name
        public static final String AUTHORITY = "seasonedblackolives.com.meemo";
        //    this is the initial piece of every Uri that points to your Content Provider
        public static final String INIT_CONTENT = "content://";
        //    Define the possible paths for accessing data in the database
        public static final String PATH_MEMORY = "memory";
        public static final String PATH_CONNECTION = "connection";
        public static final String PATH_MEMORIES = "memories";
        public static final String PATH_GET = "get";
        public static final String PATH_INSERT = "insert";
        public static final String PATH_DELETE = "delete";
        public static final String PATH_UPDATE = "update";
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
        // inner final IV that holds the number of connections
        public static final String COL_MEMORY_NUM_CONN = "memory_num_conn";
        //        inner final IV that holds the time that the memory got created
        public static final String COL_CREATION_TIME = "memory_timestamp";
    }

    /**
     * this inner class holds all the columns names of the ConnectionTable of the database
     */
    public static class ConnectionTable {
        //        inner final IV that holds the connection table name
        public static final String TABLE_NAME = "connection_table";
        //        inner final IV that holds the actual memory ID of the first memory of this relation
        public static final String COL_MEMORY_A = "memory_a";
        //        inner final IV that holds the actual memory ID of the second memory of this relation
        public static final String COL_MEMORY_B = "memory_b";
    }

    /**
     * Creates and return the SQL statement necessary for the insertion of a new connection between 2 memories into the DB.
     * The insertion inside the Memory table is right now made using the ContentValues class and the methods of the SQLiteHelper class.
     *
     * @param id_a the id of the first memory. (usually this is the 'father' memory, but that is not mandatory)
     * @param id_b the id of the second memory.
     */
    public static String sqlInsertConnection(String id_a, String id_b) {
        // INSERT INTO connection_table (memory_a, memory_b) VALUES (id_a, id_b);
        return "INSERT INTO " +
                ConnectionTable.TABLE_NAME +
                " (" +
                COL_MEMORY_A +
                ", " +
                COL_MEMORY_B +
                ") VALUES (" +
                id_a +
                ", " +
                id_b +
                ");";
    }

    /**
     * returns the SQL statement that will increment the number of connections of the memory defined by the ID
     *
     * @param id the ID of the memory that will have its number of connections incremented
     * @return the SQL statement in a String format ready for execution
     */
    public static String sqlIncrementNumConnections(String id) {
        // UPDATE memory_table SET memory_num_conn = memory_num_conn + 1 WHERE _ID = id;
        return "UPDATE " +
                MemoryTable.TABLE_NAME +
                " SET " +
                COL_MEMORY_NUM_CONN +
                " = " +
                COL_MEMORY_NUM_CONN +
                " + 1 WHERE " +
                COL_MEMORY_ID +
                " = " +
                id +
                ";";
    }

    public static String sqlDecrementNumConnections(String id) {
        return "UPDATE " +
                MemoryTable.TABLE_NAME +
                " SET " +
                COL_MEMORY_NUM_CONN +
                " = " +
                COL_MEMORY_NUM_CONN +
                " - 1 WHERE " +
                COL_MEMORY_ID +
                " = " +
                id +
                ";";
    }


    /**
     * Creates and return the SQL statement necessary for retrieving from the DB all the memories that are connected with the memory defined by the sent ID.
     *
     * @param id the ID of the memory from which you want to get all the connected memories
     */
    public static String sqlConnectedMemories(String id) {
        // THIS IS THE NEW SQL, MADE FOR THE 1 ROW CONNECTION TABLE ARCHITECTURE
        // be aware of the static imports
        // select memory_table._ID, memory_table.memory from memory_table where (memory_table._ID in(select connection_table.memory_a from connection_table where connection_table.memory_b = 1)) or (memory_table._ID in(select connection_table.memory_b from connection_table where connection_table.memory_a = 1));
        // select memory_table._ID, memory_table.memory, memory_table.memory_num_conn from memory_table where (memory_table._ID in(select connection_table.memory_a from connection_table where connection_table.memory_b = 1)) or (memory_table._ID in(select connection_table.memory_b from connection_table where connection_table.memory_a = 1));
        return "SELECT " +
                MemoryTable.TABLE_NAME +
                "." +
                COL_MEMORY_ID +
                ", " +
                MemoryTable.TABLE_NAME +
                "." +
                COL_MEMORY_TEXT +
                ", " +
                MemoryTable.TABLE_NAME +
                "." +
                COL_MEMORY_NUM_CONN +
                " FROM " +
                MemoryTable.TABLE_NAME +
                " WHERE (" +
                MemoryTable.TABLE_NAME +
                "." +
                COL_MEMORY_ID +
                " IN (SELECT " +
                ConnectionTable.TABLE_NAME +
                "." +
                COL_MEMORY_A +
                " FROM " +
                ConnectionTable.TABLE_NAME +
                " WHERE " +
                ConnectionTable.TABLE_NAME +
                "." +
                COL_MEMORY_B +
                " = " +
                id +
                ")) OR (" +
                MemoryTable.TABLE_NAME +
                "." +
                COL_MEMORY_ID +
                " IN (SELECT " +
                ConnectionTable.TABLE_NAME +
                "." +
                COL_MEMORY_B +
                " FROM " +
                ConnectionTable.TABLE_NAME +
                " WHERE " +
                ConnectionTable.TABLE_NAME +
                "." +
                COL_MEMORY_A +
                " = " +
                id +
                "));";
    }

    /**
     * returns the raw SQL statement that gets all connections for a given ID (half-search)
     *
     * @param id the ID that we'll use to return all other connections id
     * @return the raw sql statement
     */
    public static String sqlConnections1 (String id) {
        return "SELECT " +
                ConnectionTable.TABLE_NAME +
                "." +
                COL_MEMORY_A +
                " FROM " +
                ConnectionTable.TABLE_NAME +
                " WHERE " +
                ConnectionTable.TABLE_NAME +
                "." +
                COL_MEMORY_B +
                " = " +
                id +
                ";";
    }

    public static String sqlConnections2 (String id) {
        return "SELECT " +
                ConnectionTable.TABLE_NAME +
                "." +
                COL_MEMORY_B +
                " FROM " +
                ConnectionTable.TABLE_NAME +
                " WHERE " +
                ConnectionTable.TABLE_NAME +
                "." +
                COL_MEMORY_A +
                " = " +
                id +
                ";";
    }

    /**
     * Returns the SQL statement that creates the Memory Table inside the DB
     *
     * @return the SQL statement
     */
    public static String sqlCreateMemoryTable() {
    /*
    this String holds the SQL statement for creating the memory table using the information from the contract class
    a) setting the ID as the primary key. Therefore it will autoincrement and must not be set by the user
    c) the file path of a memory can be true if it does not have a file attached to it. Setting the default value to 'void' in that case
    d) the user should also not set the memory creation time. This will be done automatically by the database
    */
        return "CREATE TABLE " +
                MemoryTable.TABLE_NAME +
                " (" +
                MemoryTable.COL_MEMORY_ID +
                " INTEGER PRIMARY KEY, " +
                MemoryTable.COL_MEMORY_TEXT +
                " TEXT NOT NULL, " +
                MemoryTable.COL_MEMORY_FILE_PATH +
                " VARCHAR(500) DEFAULT 'void', " +
                MemoryTable.COL_MEMORY_NUM_CONN +
                " INTEGER DEFAULT 0, " +
                MemoryTable.COL_CREATION_TIME +
                " TEXT DEFAULT CURRENT_TIMESTAMP);";
    }

    /**
     * Returns the SQL statement that creates the Connection Table inside the DB
     *
     * @return the SQL statement
     */
    public static String sqlCreateConnectionTable() {
    /*
    this String holds the SQL statement for creating the connection table using the information from the contract class
    a) this IDs must not be null and will match one specific primary key of the memory table
    */
        return "CREATE TABLE " +
                ConnectionTable.TABLE_NAME +
                " (" +
                ConnectionTable.COL_MEMORY_A +
                " INTEGER NOT NULL, " +
                ConnectionTable.COL_MEMORY_B +
                " INTEGER NOT NULL);";
    }

    /**
     * Method that takes a literal String and returns the SQL statement that can insert the String into the Memory Table
     *
     * @param memory the String literal that will be inserted
     * @return the SQL statement that if executed will insert the String into the Memory Table
     */
    public static String sqlInsertMemory(String memory) {
        return "INSERT INTO " +
                MemoryTable.TABLE_NAME +
                " (" +
                MemoryTable.COL_MEMORY_TEXT +
                ") VALUES('" +
                memory +
                "');";
    }
}
