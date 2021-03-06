package presenter;

import ui.MemoryList_Activity;

/**
 * This is the superclass of the presenters for the Main Activity.
 * The presenters that will extend it this class will handle different use-cases for the activity
 */

public abstract class MemoryListPresenter {

    //    IV that will hold an instance of the underlying activity of this presenter
    MemoryList_Activity activity;

    MemoryListPresenter(MemoryList_Activity activity) {
        this.activity = activity;
    }

    /**
     * Responsible for setting up the Loader that will fetch the data from the DB in another thread
     */
    abstract void doInWorkerThread();
}
