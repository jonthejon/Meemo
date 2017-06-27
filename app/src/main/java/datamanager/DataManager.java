package datamanager;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.util.ArrayList;

import core.Memory;

/**
 * This is not a core class!
 * It uses android frameworks to load data into a Cursor and converts that data into Memory Objects
 * For communication with the core classes, it implements an interface.
 */

public class DataManager implements DataManagerInterface {

//    IV that holds the context of the activity that initiated this use-case
    private Context context;
//    IV that will hold the cursor with the data retrieved from the database
//    It can be null, in this case there is no data retrieved from the database
    private Cursor cursor;
//    IV that will hold the instance for the class that extends the database
    private DBHelper dbHelper;

    public DataManager(Context context) {
//        updating the proper IV with the given context
        this.context = context;
//        creating a new instance of the db helper class so we can use it to query the db
        dbHelper = new DBHelper(context);
    }

    @Override
    public Memory[] getMemoriesWithID(ContentResolver resolver, int id) {

//        calling and retrieving the Uri that is corresponds to getting all data from memory table
        Uri uri = DBContract.MemoryTable.GET_MEMORY_URI;

//        making sure that we have a null cursor so we can populate it
        resetCursor();

//        calling the query method of the Content Resolver which will call the query method of the Content Provider
        this.cursor = resolver.query(uri,null,null,null,null);

//        calling the method that will extract the data from the cursor
        return getMemoryListFromCursor(this.cursor);
    }

    private void resetCursor() {
//        resets the cursor for it to be used in another db query
        this.cursor = null;
    }

    private Memory[] getMemoryListFromCursor(Cursor data) {

//        checking to see if we have a valid Cursor and if not, returning an empty memory list
        if (data == null || data.getCount() == 0) {
            return new Memory[0];
        }

//        creating an Arraylist of Memory type so we can dinamically update it with the memories from the cursor
        ArrayList<Memory> memoryArrayList = new ArrayList<>();

//        getting the indexes of the columns of the memory table in the database
        int idColIndex = data.getColumnIndex(DBContract.MemoryTable.COL_MEMORY_ID);
        int textColIndex = data.getColumnIndex(DBContract.MemoryTable.COL_MEMORY_TEXT);
        int fileColIndex = data.getColumnIndex(DBContract.MemoryTable.COL_MEMORY_FILE_PATH);

//        we will loop inside the cursor
//        Cursor always starts at -1, se we can start the first loop already calling moveToNext()
        while(data.moveToNext()) {
//            create a new Memory object with data from the cursor
            Memory memory = new Memory(data.getInt(idColIndex),
                    data.getString(textColIndex),
                    0,
                    new int[0],
                    data.getString(fileColIndex));
//            adding the created memory object inside the arraylist
            memoryArrayList.add(memory);
        }

//        creating a new Memory array with the size of the arraylist that just got populated
        Memory[] memoryList = new Memory[memoryArrayList.size()];
//        converting all the data from inside the arraylist into the array
        memoryList = memoryArrayList.toArray(memoryList);

//        returning the array
        return memoryList;
    }
}
