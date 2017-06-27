package presenter;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;

import core.Memory;
import ui.MemoryList_Activity;

import static android.content.ContentValues.TAG;

/**
 * Presenter responsible for fetching and displaying data from a database using an adapter
 */

public class FetchPresenter implements LoaderManager.LoaderCallbacks<Memory[]> {

//    IV that will hold an instance of the underlying activity of this presenter
    private MemoryList_Activity activity;

//    IV that will hold the LayoutManager used to control the recycler view
    private LinearLayoutManager mLayoutManager;

//    IV that will hold the adapter instance for the recycler view
    private MemoryListAdapter mAdapter;

//    IV that holds the loader ID so to ensure that we are not creating a new Loader every time
//    if a loader already exists, the same loader will be used
    private final int FETCH_MEMORY_LOADER_ID = 3;

//    IV that will hold the key associated with the memory ID inside the Bundle to be sent to the loader
    static final String MEMORY_ID = "parentID";

    public FetchPresenter(MemoryList_Activity activity) {
//        setting the IV with the activity instance
        this.activity = activity;

//        creating and setting to the IV a LinearLayout for using in the RV
        this.mLayoutManager = new LinearLayoutManager(activity);

//        setting the IV of the adapter with a new instance of your adapter class
        this.mAdapter = new MemoryListAdapter();

//        calling the method to bind the necessary objects to the activity's RV
        this.bindObjectsToRecycler(mAdapter, mLayoutManager);

//        calling the private method to initiate the creation of the Loader
        this.setupLoader();
    }

    private void bindObjectsToRecycler(MemoryListAdapter adapter, LinearLayoutManager layoutManager) {

//        setting the layout manager to stack the memories from the bottom up
        layoutManager.setStackFromEnd(true);

//        setting the activity's RV with the proper layout manager
        activity.getRecyclerView().setLayoutManager(layoutManager);

//        setting the activity's RV with the proper adapter
        activity.getRecyclerView().setAdapter(adapter);
    }

    /**
     * method responsible for setting up the Loader that will fetch the data from the DB in another thread */
    private void setupLoader() {
//        getting an instance to the underlying activity's loader manager
//        this loader manager is responsible for operating with the loader's communication
        LoaderManager loaderManager = this.activity.getUILoaderManager();

//        starting a new Bundle object that will be used to send data to the loader (in our case the memoryID)
        Bundle bundle = new Bundle();
//      putting inside the Bundle the memoryID with a key that will be used for later retrieval
        bundle.putInt(MEMORY_ID, mAdapter.getParentMemory().getMemoryID());

//        initializing a new loader object (with the proper data type) using the loader manager and the loader ID we created
        Loader<Memory[]> loader = loaderManager.getLoader(this.FETCH_MEMORY_LOADER_ID);

        Log.d(TAG, "setupLoader: " + bundle.getInt(MEMORY_ID));

//        checking to see if the loader we got is a new one or it has already been created before
        if (loader == null) {
//            if the loader is new, we are initializing a new one with the loader ID and sending the Bundle with the memoryID along with it
//            we are also defining this class as the callback class that has implemented the proper callback methods
            loaderManager.initLoader(this.FETCH_MEMORY_LOADER_ID, bundle, this).forceLoad();
        } else {
//            if the loader is not new, we are using the same loader to restart it.
//            this will prevent memory leaks.
            loaderManager.restartLoader(this.FETCH_MEMORY_LOADER_ID, bundle, this).forceLoad();
        }
    }

    /**
     * This method is called whenever a new loader is asked to be created.*/
    @Override
    public Loader<Memory[]> onCreateLoader(int id, Bundle args) {

//        creating a new instance of our TaskLoader class that will perform the background work and set it to null
//        inside the switch statement we will populate it with the proper data
        TaskLoader loader = null;

        switch (id) {
//            checking to see if the loaderID sent to us is the same as the loader ID that we intend to create
            case 3:
//                since it is we will create a new instance of our TaskLoader class
//                we are sending the context of the activity, the bundle with the data, the loader id and the activity itself
                loader = new TaskLoader(activity.getUIContext(), args, id, activity);
//                make sure we break the statement otherwise this will continue to run
                break;
            default:
//                just a default case that so far, it is exactly the same as the case '3'
                loader = new TaskLoader(activity.getUIContext(), args, id, activity);
                break;
        }

//        returning the recently created loader
        return loader;
    }

    /**
     * This method is called after the loader has finished its load.
     * So we are expecting a Memory[] object properly populated.
     * */
    @Override
    public void onLoadFinished(Loader<Memory[]> loader, Memory[] memories) {

//        checking to see if the loader that finished is actually the loader that we started before
//        also checking to see if the memories array has content (if it's not null)
        if (loader.getId() == this.FETCH_MEMORY_LOADER_ID && memories != null) {
//            calling the adapter's method that will retrieve the data from the result array.
            this.mAdapter.updateMemories(memories);
        }

//        after setting the data into the adapter, we are retrieving the parent memory text to we can update the UI
        String parentMemoryText = mAdapter.getParentMemory().getMemoryText();
//        getting an instance for the Parent Textview and updating the text with the parent memory text
        activity.getParentTextView().setText(parentMemoryText);
    }

    @Override
    public void onLoaderReset(Loader<Memory[]> loader) {
//        doing nothing here since we do not have any link to the loader's data to release
    }
}
