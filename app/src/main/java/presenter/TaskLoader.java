package presenter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;

import java.util.ArrayList;

import core.Meemo;
import core.Memory;
import ui.UIInterface;

/**
 * this class will run in a background thread and deliver the result back into the main thread
 * the goal of this particular instance is to fetch data from the DB
 */

class TaskLoader extends AsyncTaskLoader<ArrayList<Memory>> {

    //    IV that holds the bundle with the data sent by the Presenter to make the proper call to the DB
    private Bundle bundle;
    //    IV that will hold an instance to the calling activity
    private UIInterface userInterface;

    TaskLoader(Context context, Bundle bundle, UIInterface userInterface) {
//        calling the super method constructor
        super(context);
//        setting the bundle IV with the given bundle parameter
        this.bundle = bundle;
//        setting the activity instance with the given parameter
        this.userInterface = userInterface;
    }

    // this method is called once BEFORE the loadInBackground method runs
    @Override
    protected void onStartLoading() {
//        before starting a new thread, check to see if the Bundle is not null. If it is, this does not make sense and must be returned
        if (bundle == null) {
//            here, we will cancel this loader since our bundle is null
            this.cancelLoad();
        }
    }

    // this is the working method that runs in the background
    @Override
    public ArrayList<Memory> loadInBackground() {
//        initiating an instance of the Meemo class which is our core class
        Meemo meemo = new Meemo(this.userInterface);
        //getting the bundle id so we can properly handle the request
        int bundleID = bundle.getInt(LoaderPresenter.BUNDLE_ID);
        switch (bundleID) {
            case 3:
//        retrieving the memoryID from the bundle we sent to this loader so we can send it to Meemo
                int memoryID = bundle.getInt(LoaderPresenter.MEMORY_ID);
//        calling the method of meemo that fetches the data and sending as a parameter the memoryID
                return meemo.getMemoryListWithID(memoryID);
            case 5:
                String query = bundle.getString(LoaderPresenter.QUERY);
                return meemo.getMemoryListWithQuery(query);
            default:
                return null;
        }
    }
}
