package datamanager;

import core.Memory;

/**
 * Interface used to enable communication with the Core classes
 */

public interface DataManagerInterface {

//    method responsible for telling the datamanager to load it's cursor using the memory ID sent as parameter
    Memory[] getMemoriesWithID(int id);
}
