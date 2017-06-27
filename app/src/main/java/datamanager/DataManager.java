package datamanager;

import android.content.Context;
import android.database.Cursor;

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
//    IV that will flag is the Cursor variable is loaded or not with data
    private boolean isLoaded;
//    IV that will be populated with the Memory objects built with data from the database
    private Memory[] memoryList;

    public DataManager(Context context) {
        this.context = context;
    }

    @Override
    public void loadManagerWithID(int id) {

    }

    @Override
    public boolean isManagerLoaded() {
        return false;
    }

    @Override
    public Memory[] getMemoryList() {
        return new Memory[0];
    }

    private void resetCursor() {
//        resets the cursor for it to be used in another db query
        this.cursor = null;
    }

    private Memory[] populateMemoryList(Cursor mCursor) {
        return new Memory[0];
    }
}
