package presenter;

import ui.MemoryList_Activity;

/**
 * This is the presenter of the main activity that will perform simple tasks into the DB
 * For more complex tasks, see the LoaderPresenter class.
 */

public class TaskPresenter extends MemoryListPresenter {

    public TaskPresenter(MemoryList_Activity activity) {
//        setting the super class IV with the instance of the underlying activity
        super(activity);
    }

    /**
     * Implemented method of MemoryListPresenter.
     * This implementation will initiate a new AsyncTask to perform simple tasks.*/
    @Override
    void doInWorkerThread() {

    }
}
