package core;

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

    public Meemo(UIInterface userInterface) {
//        populating the IV with the user interface so we can access specific values of android framework
        this.userInterface = userInterface;
//        creating a new instance of the datamanager class using the context retrieved from the UI
        this.dataManager = new DataManager(userInterface.getUIContext());
    }

    public Memory[] getMemoryListWithID(int id) {
/*        String[] fakeStringArr = userInterface.getUIResources().getStringArray(R.array.fake_string_array);
        Memory[] fakeMemoryArr = new Memory[fakeStringArr.length];
        for (int i = fakeStringArr.length - 1; i >= 0; i--) {
            fakeMemoryArr[fakeStringArr.length - 1 - i] = new Memory(1, fakeStringArr[i], 1, new int[0]);
        }
        return fakeMemoryArr;*/
        return dataManager.getMemoriesWithID(id);
    }

}
