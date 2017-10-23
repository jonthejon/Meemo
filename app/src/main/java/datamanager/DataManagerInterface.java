package datamanager;

import android.content.ContentResolver;

import java.util.ArrayList;

import core.Memory;

/**
 * Interface used to enable communication with the Core classes
 */

public interface DataManagerInterface {

    /**
     * Responsible for getting the data from the ContentProvider and returning the data in an ArrayList format back to Core class
     *
     * @param resolver the Content Resolver sent by the Core class
     * @param id       the Memory ID of the memory we want to get the connected memmories
     * @return the Arraylist containing all connected memories to the memory defined by the ID
     */
    ArrayList<Memory> getMemoriesWithID(ContentResolver resolver, int id);
    ArrayList<Memory> getMemoriesWithQuery(ContentResolver resolver, String query);
}
