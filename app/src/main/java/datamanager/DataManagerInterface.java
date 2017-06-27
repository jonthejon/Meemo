package datamanager;

import core.Memory;

/**
 * Interface used to enable communication with the Core classes
 */

public interface DataManagerInterface {

//    method responsible for telling the datamanager to load it's cursor using the memory ID sent as parameter
    void loadManagerWithID(int id);
//    method that will flag if the cursor of the data manager is loaded or not
    boolean isManagerLoaded();
//    method that will return the list filled with memory objects built with the data from the cursor
    Memory[] getMemoryList();
}
