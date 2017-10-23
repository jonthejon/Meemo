package datamanager;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import core.DBUtils;

/**
 * This will be the Content Provider of Meemo.
 * All database queries should now be processed by this class.
 * All of these methods except onCreate() can be called by multiple threads at once, so they must be thread-safe.
 */

public class MeemoContentProvider extends ContentProvider {

    //    IV that holds the instance to the dbHelper class for database calls
    private DBHelper dbHelper;

    //    These are integer values that will uniquely identify each possible Uri used by my CP
    public static final int MEMORY_TABLE = 100;
    public static final int GET_MEMORIES_BY_PARENT_ID = 101;
    public static final int INSERT_SINGLE_MEMORY = 102;
    public static final int DELETE_SINGLE_MEMORY = 103;
    public static final int UPDATE_SINGLE_MEMORY = 104;
    public static final int GET_SINGLE_MEMORY_BY_ID = 105;
    public static final int GET_MEMORIES_BY_SEARCH = 106;
    public static final int CONNECTION_TABLE = 200;
    public static final int INSERT_NEW_CONNECTION = 201;
    public static final int DELETE_CONNECTION = 202;

    /**
     * Method that returns a new UriMatcher all the time.
     * This object is used to bind a particular Uri pattern to a specific number.
     * This helps us identify the type of Uri and act properly upon it.
     */
    public static UriMatcher getUriMatcher() {

//        Initialize a UriMatcher with no matches by passing in NO_MATCH to the constructor
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

//        adds to the UriMatcher the URI that we want to register
//        Uri for getting a single memory from memory table
        uriMatcher.addURI(DBContract.AUTHORITY, DBContract.PATH_MEMORIES + "/" + DBContract.PATH_GET + "/#", GET_MEMORIES_BY_PARENT_ID);
//        Uri for inserting a new memory inside the memory table
        uriMatcher.addURI(DBContract.AUTHORITY, DBContract.PATH_MEMORY + "/" + DBContract.PATH_INSERT + "/#", INSERT_SINGLE_MEMORY);
//        Uri for deleting a memory from the memory table
        uriMatcher.addURI(DBContract.AUTHORITY, DBContract.PATH_MEMORY + "/" + DBContract.PATH_DELETE + "/#", DELETE_SINGLE_MEMORY);
//        Uri for updating a memory from the memory table
        uriMatcher.addURI(DBContract.AUTHORITY, DBContract.PATH_MEMORY + "/" + DBContract.PATH_UPDATE + "/#", UPDATE_SINGLE_MEMORY);
//        Uri for getting  a single memory from the memory table
        uriMatcher.addURI(DBContract.AUTHORITY, DBContract.PATH_MEMORY + "/" + DBContract.PATH_GET + "/#", GET_SINGLE_MEMORY_BY_ID);
        uriMatcher.addURI(DBContract.AUTHORITY, DBContract.PATH_CONNECTION + "/" + DBContract.PATH_INSERT + "/#" + "/#", INSERT_NEW_CONNECTION);
        uriMatcher.addURI(DBContract.AUTHORITY, DBContract.PATH_CONNECTION + "/" + DBContract.PATH_DELETE + "/#" + "/#", DELETE_CONNECTION);
        uriMatcher.addURI(DBContract.AUTHORITY, DBContract.PATH_MEMORIES + "/" + DBContract.PATH_SEARCH, GET_MEMORIES_BY_SEARCH);

//        returns the created urimatcher
        return uriMatcher;
    }

    /**
     * Initialize your provider. The Android system calls this method immediately after it creates your provider.
     * Notice that your provider is not created until a ContentResolver object tries to access it.
     * Return true from onCreate to signify success performing setup.
     */
    @Override
    public boolean onCreate() {
//        initializing the IV that holds the instance to the dbHelper class
        this.dbHelper = new DBHelper(getContext());
        return true;
    }

    /*
     * Method used by any client that wants to query this Content Provider.
     * It receives at least an Uri as a parameter.
     */
    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
//        getting a final readable instance for the database using our dbhelper class
        final SQLiteDatabase db = dbHelper.getReadableDatabase();
//        initializing a new Cursor object that will be returned after populated
        Cursor cursor;
//        using the UriMatcher object to tell us which Uri the client sent us
        switch (getUriMatcher().match(uri)) {
//            checking to see if the uri is the case of fetching memories using the parent ID
            case GET_MEMORIES_BY_PARENT_ID:
//                retrieving the ID of the caller memory from the Uri
                String id = uri.getLastPathSegment();
                cursor = db.rawQuery(DBUtils.sqlConnectedMemories(id), null);
                break;
            case GET_MEMORIES_BY_SEARCH:
                String sql = DBUtils.sqlSearchMemories(selection);
                Log.d("SQL: ", sql);
                cursor = db.rawQuery(DBUtils.sqlSearchMemories(selection), null);
                break;
            default:
//                the uri is not a valid one, so we'll return a null cursor
                return null;
        }
        if (cursor != null) {
//        Register to watch a content URI for changes. This is cool in case your dataset changed and you have a cursor pointing at it.
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
        }
//        return the created and populated cursor object
        return cursor;
    }

    /*
     * This method, if correctly implemented must return the type of the data for each URI that this CP receives.
     * In our case, we are returning data from a table.
     * See the proper documentation for more information on MIME-TYPES.
     */
    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
//        checks to see if we are receiving an URI for getting memories from the memory table
        switch (getUriMatcher().match(uri)) {
            case GET_MEMORIES_BY_PARENT_ID:
//            returning the proper data type for this type of request
//            'vnd.android.cursor.dir' says that we are returning more then one line of data
//            'vnd.com.meemo.provider.memory_table' uniquely identifies this data type
                return "vnd.android.cursor.dir/vnd.com.meemo.provider.memory_table";
        }
        return null;
    }

    /*
     * This method is used by clients to insert given values into the database.
     * note that you need to insert data in more than one table since every memory is a new child of an older memory.
     */
    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
//        creates a new writable instance of the database for data insertion
        final SQLiteDatabase db = dbHelper.getWritableDatabase();

//        checks to see what type of Uri was sent to us by the user
        switch (getUriMatcher().match(uri)) {
            case INSERT_SINGLE_MEMORY:
//                getting the memory text so we can insert it into the virtual search table
                String memory = values.getAsString(DBContract.MemoryTable.getColMemoryText());
//                  inserts the content values inside the memory_table
                long id_b = db.insert(DBContract.MemoryTable.getTableName(), null, values);
//                retrieves the father memory ID from the Uri
                String id_a = uri.getLastPathSegment();
                // getting the SQL from DBContract and executing the command into the connection_table
                db.execSQL(DBUtils.sqlInsertConnection(id_a, Long.toString(id_b)));
                // getting the SQL from DBUtils that will update the number of connections of each memory and executing it.
                db.execSQL(DBUtils.sqlIncrementNumConnections(id_a));
                db.execSQL(DBUtils.sqlIncrementNumConnections(Long.toString(id_b)));
//                syncing the virtual table with the new inserted memory
                String sql = DBUtils.sqlInsertMemoryVirtualTable(Long.toString(id_b), memory);
                Log.d("SQL: ", sql);
                db.execSQL(sql);
//                db.execSQL(DBUtils.sqlInsertMemoryVirtualTable(Long.toString(id_b), memory));
//                  returns the new Uri that points to the specific memory inside the memory table
                return DBContract.MemoryTable.uriGetMemory().buildUpon().appendPath(Long.toString(id_b)).build();
            case INSERT_NEW_CONNECTION:
                List<String> paths = uri.getPathSegments();
                String id_conn_a = paths.get(paths.size() - 1);
                String id_conn_b = paths.get(paths.size() - 2);
                db.execSQL(DBUtils.sqlInsertConnection(id_conn_a, id_conn_b));
                db.execSQL(DBUtils.sqlIncrementNumConnections(id_conn_a));
                db.execSQL(DBUtils.sqlIncrementNumConnections(id_conn_b));
//                returning the Uri of the first memory that got chosen to be connected
                return DBContract.MemoryTable.uriGetMemory().buildUpon().appendPath(id_conn_a).build();
        }
        // if we got here, then the Uri sent to us did not match the insertion Uri so we'll return null
        return null;
    }

    /*
     * This method is called when any client wants to delete a particular memory from the database.
     * note that we are checking if the used Uri is in the correct format before doing any operations.
     */
    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
//        creates a new writable instance of the database for data insertion
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        final SQLiteDatabase db_r = dbHelper.getReadableDatabase();

//        checks to see what type of Uri was sent to us by the user
        switch (getUriMatcher().match(uri)) {
            case DELETE_SINGLE_MEMORY:
//                get the last path segment that in the case of this Uri is the memory ID to be deleted
                String id = uri.getLastPathSegment();
//                deletes the memory from the DB and returns the number of rows deleted (hopefully 1)
                int numRows = db.delete(DBContract.MemoryTable.getTableName(), "_ID = ?", new String[]{id});
                if (numRows != 1) return 0;
                Cursor connSearch1 = db_r.rawQuery(DBUtils.sqlConnections1(id), null);
                Cursor connSearch2 = db_r.rawQuery(DBUtils.sqlConnections2(id), null);
                ArrayList<Integer> connIDs = new ArrayList<>();
                if (connSearch1 != null && connSearch1.getCount() > 0) {
                    int mem_a_col = connSearch1.getColumnIndex(DBContract.ConnectionTable.getColMemoryA());
                    while (connSearch1.moveToNext()) {
                        connIDs.add(connSearch1.getInt(mem_a_col));
                    }
                    connSearch1.close();
                }
                if (connSearch2 != null && connSearch2.getCount() > 0) {
                    int mem_b_col = connSearch2.getColumnIndex(DBContract.ConnectionTable.getColMemoryB());
                    while (connSearch2.moveToNext()) {
                        connIDs.add(connSearch2.getInt(mem_b_col));
                    }
                    connSearch2.close();
                }
                for (Integer it : connIDs) {
                    db.execSQL(DBUtils.sqlDecrementNumConnections(Integer.toString(it)));
                }
                db.delete(DBContract.ConnectionTable.getTableName(), DBUtils.ConnectionTable.COL_MEMORY_A + " = ?", new String[]{id});
                db.delete(DBContract.ConnectionTable.getTableName(), DBUtils.ConnectionTable.COL_MEMORY_B + " = ?", new String[]{id});
//                updating the virtual table given that we just deleted a memory
                db.execSQL(DBUtils.sqlDeleteMemoryVirtualTable(id));
                return numRows;
            case DELETE_CONNECTION:
                List<String> paths = uri.getPathSegments();
                String id_conn_a = paths.get(paths.size() - 1);
                String id_conn_b = paths.get(paths.size() - 2);
                db.execSQL(DBUtils.sqlDeleteConnection(id_conn_a, id_conn_b));
                db.execSQL(DBUtils.sqlDeleteConnection(id_conn_b, id_conn_a));
                db.execSQL(DBUtils.sqlDecrementNumConnections(id_conn_a));
                db.execSQL(DBUtils.sqlDecrementNumConnections(id_conn_b));
                return 1;
        }
        return 0;
    }

    /*
     * This method is called when any client wants to update a particular memory from the database.
     * note that we are checking if the used Uri is in the correct format before doing any operations.
     */
    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
//        creates a new writable instance of the database for data insertion
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
//        checks to see what type of Uri was sent to us by the user
        switch (getUriMatcher().match(uri)) {
            case UPDATE_SINGLE_MEMORY:
                //getting the memory text so we can insert it into the virtual search table
                String memory = values.getAsString(DBContract.MemoryTable.getColMemoryText());
//                get the last path segment that in the case of this Uri is the memory ID to be updated
                String id = uri.getLastPathSegment();
//                updates the memory in the DB and returns the number of rows updated (hopefully 1, since we are giving a specific ID)
                int numRows = db.update(DBContract.MemoryTable.getTableName(), values, "_ID = ?", new String[]{id});
//                deleting the corresponding memory from the virtual table
                db.execSQL(DBUtils.sqlDeleteMemoryVirtualTable(id));
//                inserting the new memory again inside the virtual table
                db.execSQL(DBUtils.sqlInsertMemoryVirtualTable(id, memory));
                return numRows;
        }
        return 0;
    }
}
