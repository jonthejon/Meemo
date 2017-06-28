package datamanager;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

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
    public static final int GET_SINGLE_MEMORY = 101;
    public static final int INSERT_SINGLE_MEMORY = 102;
    public static final int DELETE_SINGLE_MEMORY = 103;
    public static final int UPDATE_SINGLE_MEMORY = 104;
    public static final int FAMILY_TABLE = 200;
    public static final int GET_MEMORY_CHILDREN = 201;

    /**
     * Method that returns a new UriMatcher all the time.
     * This object is used to bind a particular Uri pattern to a specific number.
     * This helps us identify the type of Uri and act properly upon it.*/
    public static UriMatcher getUriMatcher() {

//        Initialize a UriMatcher with no matches by passing in NO_MATCH to the constructor
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

//        adds to the UriMatcher the URI that we want to register
//        Uri for getting all memories from memory table
        uriMatcher.addURI(DBContract.AUTHORITY, DBContract.PATH_MEMORY + "/" + DBContract.PATH_GET, MEMORY_TABLE);
//        Uri for getting a single memory from memory table
        uriMatcher.addURI(DBContract.AUTHORITY, DBContract.PATH_MEMORY + "/" + DBContract.PATH_GET + "/#", GET_SINGLE_MEMORY);
//        Uri for inserting a new memory inside the memory table
        uriMatcher.addURI(DBContract.AUTHORITY, DBContract.PATH_MEMORY + "/" + DBContract.PATH_INSERT, INSERT_SINGLE_MEMORY);
//        Uri for deleting a memory from the memory table
        uriMatcher.addURI(DBContract.AUTHORITY, DBContract.PATH_MEMORY + "/" + DBContract.PATH_DELETE + "/#", DELETE_SINGLE_MEMORY);
//        Uri for updating a memory from the memory table
        uriMatcher.addURI(DBContract.AUTHORITY, DBContract.PATH_MEMORY + "/" + DBContract.PATH_UPDATE + "/#", UPDATE_SINGLE_MEMORY);

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

    /**
     * Method used by any client that wants to query this Content Provider.
     * It receives at least an Uri as a parameter.
     */
    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

//        getting a final readable instance for the database using our dbhelper class
        final SQLiteDatabase db = dbHelper.getReadableDatabase();
//        SQLiteQueryBuilder is a helper class that helps us create SQL queries
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
//        initializing a new Cursor object that will be returned after populated
        Cursor cursor;
//        using the UriMatcher object to tell us which Uri the client sent us
        switch (getUriMatcher().match(uri)) {
//            we'll use the query entire memory table as a default case
//            probably not a good idea if this is a public Content Provider
            default:
//                setting the table to be queried to be the memory table
                queryBuilder.setTables(DBContract.MemoryTable.TABLE_NAME);
//                performing the actual query and populating the cursor
                cursor = queryBuilder.query(db, null, null, null, null, null, "'_ID' ASC");
                break;
        }
//        Register to watch a content URI for changes
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
//        return the created and populated cursor object
        return cursor;
    }

    /**
     * This method, if correctly implemented must return the type of the data for each URI that this CP receives.
     * In our case, we are returning data from a table.
     * See the proper documentation for more information on MIME-TYPES.
     */
    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
//        checks to see if we are receiving an URI for getting memories from the memory table
        switch (getUriMatcher().match(uri)) {
            case MEMORY_TABLE:
//            returning the proper data type for this type of request
//            'vnd.android.cursor.dir' says that we are returning more then one line of data
//            'vnd.com.meemo.provider.memory_table' uniquely identifies this data type
                return "vnd.android.cursor.dir/vnd.com.meemo.provider.memory_table";
        }
        return null;
    }

    /**
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
//                  inserts the content values inside the database
                long id = db.insert(DBContract.MemoryTable.TABLE_NAME, null, values);
//                  returns the new Uri that points to the specific memory inside the memory table
                return uri.buildUpon().appendPath(Long.toString(id)).build();
        }
        return null;
    }

    /**
     * This method is called when any client wants to delete a particular memory from the database.
     * note that we are checking if the used Uri is in the correct format before doing any operations.*/
    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
//        creates a new writable instance of the database for data insertion
        final SQLiteDatabase db = dbHelper.getWritableDatabase();

//        checks to see what type of Uri was sent to us by the user
        switch (getUriMatcher().match(uri)) {
            case DELETE_SINGLE_MEMORY:
//                get the last path segment that in the case of this Uri is the memory ID to be deleted
                String id = uri.getLastPathSegment();
//                deletes the memory from the DB and returns the number of rows deleted (hopefully 1)
                return db.delete(DBContract.MemoryTable.TABLE_NAME, "_ID = ?", new String[]{id});
        }
        return 0;
    }

    /**
     * This method is called when any client wants to update a particular memory from the database.
     * note that we are checking if the used Uri is in the correct format before doing any operations.*/
    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
//        creates a new writable instance of the database for data insertion
        final SQLiteDatabase db = dbHelper.getWritableDatabase();

//        checks to see what type of Uri was sent to us by the user
        switch (getUriMatcher().match(uri)) {
            case UPDATE_SINGLE_MEMORY:
//                get the last path segment that in the case of this Uri is the memory ID to be updated
                String id = uri.getLastPathSegment();
//                updates the memory in the DB and returns the number of rows updated (hopefully 1, since we are giving a specific ID)
                return db.update(DBContract.MemoryTable.TABLE_NAME, values, "_ID = ?", new String[]{id});
        }
        return 0;
    }
}
