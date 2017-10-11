package datamanager;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

import java.util.ArrayList;
import java.util.Collections;

import core.Memory;

/**
 * This is not a core class!
 * It uses android frameworks to load data into a Cursor and converts that data into Memory Objects
 * For communication with the core classes, it implements an interface.
 */

public class DataManager implements DataManagerInterface {

    //    IV that will hold the cursor with the data retrieved from the database
//    It can be null, in this case there is no data retrieved from the database
    private Cursor cursor;

    @Override
    public ArrayList<Memory> getMemoriesWithID(ContentResolver resolver, int id) {
//        initiating an Uri object so we can create the proper Uri
        Uri uri;

//        creating a get memory Uri
        uri = DBContract.MemoryTable.uriGetMemories();
//        appending the Uri with the memory ID
        uri = uri.buildUpon().appendPath(Integer.toString(id)).build();

//        making sure that we have a null cursor so we can populate it
        resetCursor();

//        calling the query method of the Content Resolver which will call the query method of the Content Provider
        this.cursor = resolver.query(uri, null, null, null, null);

//        calling the method that will extract the data from the cursor
        return getMemoryListFromCursor(this.cursor);
    }

    /**
     * resets the cursor for it to be used in another db query
     */
    private void resetCursor() {
        this.cursor = null;
    }

    /**
     * Converts a Cursor datatype full of Memory objects into an ArrayList<Memory>
     *
     * @param data the cursor containing all the Memory objects retrieved from the DB
     * @return ArrayList<Memory> containing all the memories that were inside the cursor
     */
    private ArrayList<Memory> getMemoryListFromCursor(Cursor data) {
//        checking to see if we have a valid Cursor and if not, returning an empty memory list
        if (data == null || data.getCount() == 0) {
            return new ArrayList<>();
        }
//        creating an Arraylist of Memory type so we can dinamically update it with the memories from the cursor
        ArrayList<Memory> memoryArrayList = new ArrayList<>();

//        getting the indexes of the columns of the memory table in the database
        int idColIndex = data.getColumnIndex(DBContract.MemoryTable.getColId());
        int textColIndex = data.getColumnIndex(DBContract.MemoryTable.getColMemoryText());
        int connColIndex = data.getColumnIndex(DBContract.MemoryTable.getColConnNum());

//        we will loop inside the cursor
//        Cursor always starts at -1, se we can start the first loop already calling moveToNext()
        while (data.moveToNext()) {
//            create a new Memory object with data from the cursor
            Memory memory = new Memory.MemoryBuilder(data.getInt(idColIndex), data.getString(textColIndex), data.getInt(connColIndex))
                    .build();
            memoryArrayList.add(memory);
        }

//        reversing the array before sending it back to the caller
        Collections.reverse(memoryArrayList);
//        returning the reversed Arraylist containing the memories extracted from the db
        return memoryArrayList;
    }
}
