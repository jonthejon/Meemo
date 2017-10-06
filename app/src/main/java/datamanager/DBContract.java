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
    static final int DATABASE_VERSION = 8;

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
    static final String PATH_MEMORIES = "memories";
    static final String PATH_GET = "get";
    static final String PATH_INSERT = "insert";
    static final String PATH_DELETE = "delete";
    static final String PATH_UPDATE = "update";


    public static final class MemoryTable implements BaseColumns {
//        variable that will store the URI used by the Content Provider to get a single memory from this table
        public static final Uri GET_MEMORY_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MEMORY).appendPath(PATH_GET).build();
//        variable that will store the URI used by the Content Provider to get a set of memories from this table
        public static final Uri GET_MEMORIES_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MEMORIES).appendPath(PATH_GET).build();
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
        //public static final int COMPOSITION_TYPE = 11;
//        inner final IV that states the code for a relational relation between two memories
        //public static final int RELATIONAL_TYPE = 23;

//        inner final IV that holds the connection table name
        public static final String TABLE_NAME = "connection_table";
//        inner final IV that holds the actual memory ID of the first memory of this relation
        public static final String COL_MEMORY_A = "memory_a";
//        inner final IV that holds the actual memory ID of the second memory of this relation
        public static final String COL_MEMORY_B = "memory_b";
//        inner final IV that holds the type of connection that exists between the 2 memories represented in a row
        //public static final String COL_CONNECTION_TYPE = "type";
    }

    /** Creates and return the SQL statement necessary for the insertion of a new connection between 2 memories into the DB.
     * The insertion inside the Memory table is right now made using the ContentValues class and the methods of the SQLiteHelper class.
     * @param id_a the id of the first memory. (usually this is the 'father' memory, but that is not mandatory)
     * @param id_b the id of the second memory.
     */
    public static String getInsertSQL(String id_a, String id_b) {
	    return "INSERT INTO " +
                        DBContract.ConnectionTable.TABLE_NAME +
                        " (" +
                        DBContract.ConnectionTable.COL_MEMORY_A +
                        ", " +
                        DBContract.ConnectionTable.COL_MEMORY_B +
                        ") VALUES (" +
                        id_a +
                        ", " +
                        id_b +
                        ");";
    }

   /** Creates and return the SQL statement necessary for retrieving from the DB all the memories that are connected with the memory defined by the sent ID.
    * @param id the ID of the memory from which you want to get all the connected memories
    */ 
    public static String getConnMemoriesSQL(String id) {
	    // THIS IS THE OLD SQL.
		// SELECT memory_table._ID, connection_table.memory_a AS link_id, memory_table.memory_text, memory_table.memory_file_path, connection_table.type 
		// FROM (memory_table INNER JOIN connection_table ON memory_table._ID = connection_table.memory_b) 
		// WHERE memory_table._ID IN(SELECT connection_table.memory_b FROM connection_table WHERE connection_table.memory_a = id) 
		// AND link_id = id;
		// COMPLETED: check to see if we can delete this que String
                //String que = "connection_table.memory_a = 2) AND link_id = 2;";
//                executing the SQL statement to populate the cursor
/*	    return "SELECT " +
                        DBContract.MemoryTable.TABLE_NAME + "." + DBContract.MemoryTable.COL_MEMORY_ID +
                        ", " +
                        DBContract.ConnectionTable.TABLE_NAME + "." + DBContract.ConnectionTable.COL_MEMORY_A +
                        " AS link_id, " +
                        DBContract.MemoryTable.TABLE_NAME + "." + DBContract.MemoryTable.COL_MEMORY_TEXT +
                        ", " +
                        DBContract.MemoryTable.TABLE_NAME + "." + DBContract.MemoryTable.COL_MEMORY_FILE_PATH +
                        ", " +
                        DBContract.ConnectionTable.TABLE_NAME + "." + DBContract.ConnectionTable.COL_CONNECTION_TYPE +
                        " FROM (" +
                        DBContract.MemoryTable.TABLE_NAME +
                        " INNER JOIN " +
                        DBContract.ConnectionTable.TABLE_NAME +
                        " ON " +
                        DBContract.MemoryTable.TABLE_NAME + "." + DBContract.MemoryTable.COL_MEMORY_ID +
                        " = " +
                        DBContract.ConnectionTable.TABLE_NAME + "." + DBContract.ConnectionTable.COL_MEMORY_B +
                        ") WHERE " +
                        DBContract.MemoryTable.TABLE_NAME + "." + DBContract.MemoryTable.COL_MEMORY_ID +
                        " IN(SELECT " +
                        DBContract.ConnectionTable.TABLE_NAME + "." + DBContract.ConnectionTable.COL_MEMORY_B +
                        " FROM " +
                        DBContract.ConnectionTable.TABLE_NAME +
                        " WHERE " +
                        DBContract.ConnectionTable.TABLE_NAME + "." + DBContract.ConnectionTable.COL_MEMORY_A +
                        " = " +
                        id +
                        ") AND link_id = " +
                        id +
                        ";";
			*/
//                // THIS IS THE NEW SQL, MADE FOR THE 1 ROW CONNECTION TABLE ARCHITECTURE
	// COMPLETED: this is the new query that you'll use for when the connection table has only one row of data.
	// select memory_table._ID, memory_table.memory from memory_table where (memory_table._ID in(select connection_table.memory_a from connection_table where connection_table.memory_b = 1)) or (memory_table._ID in(select connection_table.memory_b from connection_table where connection_table.memory_a = 1));
	return "SELECT " +
		DBContract.MemoryTable.TABLE_NAME +
		"." +
		DBContract.MemoryTable.COL_MEMORY_ID +
		", " +
		DBContract.MemoryTable.TABLE_NAME +
		"." +
		DBContract.MemoryTable.COL_MEMORY_TEXT +
		" FROM " +
		DBContract.MemoryTable.TABLE_NAME +
		" WHERE (" +
		DBContract.MemoryTable.TABLE_NAME +
		"." +
		DBContract.MemoryTable.COL_MEMORY_ID +
		" IN (SELECT " +
		DBContract.ConnectionTable.TABLE_NAME +
		"." +
		DBContract.ConnectionTable.COL_MEMORY_A +
		" FROM " +
		DBContract.ConnectionTable.TABLE_NAME +
		" WHERE " +
		DBContract.ConnectionTable.TABLE_NAME +
		"." +
		DBContract.ConnectionTable.COL_MEMORY_B +
		" = " +
		id +
		")) OR (" +
		DBContract.MemoryTable.TABLE_NAME +
		"." +
		DBContract.MemoryTable.COL_MEMORY_ID +
		" IN (SELECT " +
		DBContract.ConnectionTable.TABLE_NAME +
		"." +
		DBContract.ConnectionTable.COL_MEMORY_B +
		" FROM " +
		DBContract.ConnectionTable.TABLE_NAME +
		" WHERE " +
		DBContract.ConnectionTable.TABLE_NAME +
		"." +
		DBContract.ConnectionTable.COL_MEMORY_A +
		" = " +
		id +
		"));";
    }
}
