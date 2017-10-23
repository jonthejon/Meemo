package datamanager;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import core.DBUtils;
import seasonedblackolives.com.meemo.R;

import static android.content.ContentValues.TAG;

/**
 * class that will implement the database based on the @DBContract.java class
 */

class DBHelper extends SQLiteOpenHelper {

    private Context context;

    /**
     * Constructor for this class that will create or update the DB.
     *
     * @param context the context of the ContentProvider that calls this class
     */
    DBHelper(Context context) {
//        calling the SQLiteOpenHelper constructor and sending him the parameters that it needs from the contract class
//        this call will ensure that the database will be instantiated or created depending on the version number
        super(context, DBContract.DATABASE_NAME, null, DBContract.DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
//        executing the creation of the memory table
        db.execSQL(DBUtils.sqlCreateMemoryTable());
//        executing the creation of the family table
        db.execSQL(DBUtils.sqlCreateConnectionTable());
        db.execSQL(DBUtils.sqlCreateVirtualSearchTable());
//        inserting the first memory into the DB. This will be the parent of all other memories.
        db.execSQL(DBUtils.sqlInsertMemory(context.getString(R.string.mind_name)));
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            for (int i = oldVersion; i < newVersion; ++i) {
                // creating the String with the sql filename
                String migrationName = String.format("from_%d_to_%d.sql", i, (i + 1));
                // method that will execute the sql file
                readAndExecuteSQLScript(db, context, migrationName);
            }
        } catch (Exception exception) {
            Log.e(TAG, "Exception running upgrade script:", exception);
        }
        onCreate(db);
    }

    /**
     * Private method that will try to open the SQL script saved stored inside the assets/ folder and read it using a buffered reader
     *
     * @param db       the database instance
     * @param ctx      the context of the creation of the database
     * @param fileName the filename in String format os the SQL script that is stored inside the assets/ folder
     */
    private void readAndExecuteSQLScript(SQLiteDatabase db, Context ctx, String fileName) {
        // getting the assetmanager of the application
        AssetManager assetManager = ctx.getAssets();
        // initiating a buffered reader
        BufferedReader reader = null;
        // like all I/O stuff, a lot of things can go wrong, so we're putting things into a try/catch block
        try {
            // opening the sql file with a InputStrem
            InputStream is = assetManager.open(fileName);
            // every input stream must have a reader attached to it
            InputStreamReader isr = new InputStreamReader(is);
            // using the Buffered reader so we can access the data more smoothly
            reader = new BufferedReader(isr);
            // calling the method that will actually execute the script
            executeSQLScript(db, reader);
        } catch (IOException e) {
            Log.e(TAG, "IOException:", e);
        } finally {
            // does not matter how things went, we must close our reader
            // checking to see if the reader is not null, in other words, it has been initalized
            if (reader != null) {
                try {
                    // trying to close the reader, since it's opened
                    reader.close();
                } catch (IOException e) {
                    Log.e(TAG, "IOException:", e);
                }
            }
        }
    }

    /**
     * Private method that will receive a BufferedReader with the SQL script and execute it
     *
     * @param db     the database instance
     * @param reader the BufferedReader that contains the SQL script
     */
    private void executeSQLScript(SQLiteDatabase db, BufferedReader reader) throws IOException {
        // string that will temporarily store the line that is read from the buffered reader
        String line;
        // with the StringBuilder we'll create the full statement of the SQL
        StringBuilder statement = new StringBuilder();
        // readLine() will read the next line of the buffered reader into a String or return null if it does not exist
        while ((line = reader.readLine()) != null) {
            // appending the line into the StringBuilder in case there are more line of this same sql statement inside the reader
            statement.append(line);
            // appending a carriage return inside the StringBuilder just like the sql file read by the reader
            statement.append("\n");
            // we want to end the statement creation in case we find a ';' because that is the end of this SQL statement
            if (line.endsWith(";")) {
                // raw executing the sql statement
                db.execSQL(statement.toString());
                // reseting the StringBuilder so we can continue to read other statements from the buffered reader
                statement = new StringBuilder();
            }
        }
        Log.d("INSIDE READING SQL", "sql asset file works.");
    }
}
