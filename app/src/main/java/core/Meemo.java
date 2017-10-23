package core;

import java.util.ArrayList;

import datamanager.DataManager;
import datamanager.DataManagerInterface;
import ui.UIInterface;

/**
 * Core class of this application.
 * Responsible for managing the fetch of data from the DB or future servers.
 * Responsible for making any data conversions necessary.
 * The logical unit of this app.
 * Does not communicate directly with any Android classes or frameworks.
 */

public class Meemo {

    //    IV that holds the instance for the activity that called this instance of Meemo
    private UIInterface userInterface;
    //    IV that holds the Data Manager interface instance for communication
    private DataManagerInterface dataManager;

    /**
     * The constructor of this class.
     *
     * @param userInterface the instance of the UI interface used to communicate with the Activity that called this class
     */
    public Meemo(UIInterface userInterface) {
//        populating the IV with the user interface so we can access specific values of android framework
        this.userInterface = userInterface;
//        creating a new instance of the datamanager class
        this.dataManager = new DataManager();
    }

    /**
     * Method responsible for making the bridge between the activity and the datamanager, asking for the later for the data.
     *
     * @param id the Memory ID that we want to fetch all connected memories
     * @return the ArrayList containing all connected memories to the Memory defined by the ID
     */
    public ArrayList<Memory> getMemoryListWithID(int id) {
//        returns the call to the method of the data manager that calls the DB and serve back the Memory array
        return dataManager.getMemoriesWithID(userInterface.getUIContentResolver(), id);
    }

    /**
     * Method responsible for making the bridge between the activity and the datamanager, asking for memories given a search query.
     * @param query the String that the user wrote to do the search for memories
     */
    public ArrayList<Memory> getMemoryListWithQuery(String query) {
        return dataManager.getMemoriesWithQuery(userInterface.getUIContentResolver(), query);
    }
}
