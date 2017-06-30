package datamanager;

import android.content.ContentResolver;

import java.util.ArrayList;

import core.Memory;

/**
 * Interface used to enable communication with the Core classes
 */

public interface DataManagerInterface {

//    method responsible for telling the datamanager to load it's cursor using the memory ID sent as parameter
    ArrayList<Memory> getMemoriesWithID(ContentResolver resolver, int id);
}
