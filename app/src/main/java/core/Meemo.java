package core;

import seasonedblackolives.com.meemo.R;
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
    private UIInterface activity;

    public Meemo(UIInterface activity) {
        this.activity = activity;
    }

    public Memory[] getMemoryListWithID(int id) {
        String[] fakeStringArr = activity.getUIResources().getStringArray(R.array.fake_string_array);
        Memory[] fakeMemoryArr = new Memory[fakeStringArr.length];
        for (int i = fakeStringArr.length - 1; i >= 0; i--) {
            fakeMemoryArr[fakeStringArr.length - 1 - i] = new Memory(1, fakeStringArr[i], 1, new int[0]);
        }
        return fakeMemoryArr;
    }

}
