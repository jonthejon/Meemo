package datamanager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import core.DBUtils;
import seasonedblackolives.com.meemo.R;

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
//        inserting the first memory into the DB. This will be the parent of all other memories.
        db.execSQL(DBUtils.sqlInsertMemory(context.getString(R.string.mind_name)));
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // For now simply drop the tables and create new ones. This means if you change the
        // DATABASE_VERSION the table will be dropped!!
        // In a production app, this method might be modified to ALTER the table
        // instead of dropping it, so that existing data is not deleted.
        db.execSQL("DROP TABLE IF EXISTS " + DBContract.MemoryTable.getTableName() + ";");
        db.execSQL("DROP TABLE IF EXISTS " + DBContract.ConnectionTable.getTableName() + ";");
        onCreate(db);
    }
}
