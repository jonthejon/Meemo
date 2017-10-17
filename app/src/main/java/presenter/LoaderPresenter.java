package presenter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;

import core.Memory;
import seasonedblackolives.com.meemo.R;
import ui.MemoryList_Activity;

/**
 * Presenter responsible for fetching and displaying data from a database using an adapter
 */
public class LoaderPresenter extends MemoryListPresenter implements LoaderManager.LoaderCallbacks<ArrayList<Memory>> {

    //    IV that will hold the adapter instance for the recycler view
    private MemoryListAdapter mAdapter;
    // creating the ArrayList IV that will hold the Caller history
    private ArrayList<Integer> history;
    // IV that tells if we are in connect mode or not
    private boolean connectMode;
    //    this IV will hold the ID of the memory we signed to connect
    private int connectId;

    //    IV that holds the loader ID so to ensure that we are not creating a new Loader every time
    //    if a loader already exists, the same loader will be used
    private final int FETCH_LOADER_ID = 3;

    //    IV that will hold the key associated with the memory ID inside the Bundle to be sent to the loader
    static final String MEMORY_ID = "callerID";

    public LoaderPresenter(MemoryList_Activity activity) {
//        setting the Superclass IV to hold the instance to the activity
        super(activity);
        // creating the Arraylist that will hold the history
        this.history = new ArrayList<>();
        // we don't want to start at connect mode
        this.connectMode = false;
//        creating and setting to the IV a LinearLayout for using in the RV
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(activity);
//        setting the IV of the adapter with a new instance of your adapter class
        this.mAdapter = new MemoryListAdapter(this);
//        calling the method to bind the necessary objects to the activity's RV
        this.bindObjectsToRecycler(mAdapter, mLayoutManager);
    }

    /**
     * Responsible for registering functionality concerning the swipe and drag of the RecyclerView items (memories)
     */
    private void registerAndHandleGestures() {
    /*
     * ItemTouchHelper enables touch behavior (like swipe and move) on each ViewHolder, and uses callbacks to signal when a user is performing these actions.
     * sending to the constructor of the ItemTouchHandler a new SimpleCallback class that will handle the proper gestures
     * ItemTouchHelper.SimpleCallback(int dragDirs, int swipeDirs)
     * dragDirs - Binary OR of direction flags in which the Views can be dragged. Must be composed of LEFT, RIGHT, START, END, UP and DOWN.
     * swipeDirs - Binary OR of direction flags in which the Views can be swiped. Must be composed of LEFT, RIGHT, START, END, UP and DOWN.
     */
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            //            on this method you can handle drag and drop
            //            in our case, we are not handling drag and drop, so the SimpleCallback sets the dragDirs to 0 and this method returns false
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            //            on this method you'll handle swipe functionality
//            on our case we are using the left swipe for deletion and the right swipe for other functionalities like archiving
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
//                getting the position of the item that is being swiped. With this you can access the Memory object later by asking for it to the adapter
                int viewPos = viewHolder.getAdapterPosition();
                if (direction == ItemTouchHelper.LEFT) {
//                    handle delete functionality
                } else if (direction == ItemTouchHelper.RIGHT) {
//                    handle other functionality besides deletion, like archiving
                }
            }
//            finally, attaching this ItemTouchHandler to the RecyclerView so it can use it whenever necessary
        }).attachToRecyclerView(super.activity.getRecyclerView());
    }

    /**
     * Responsible for wiring the activity's recycler view with the layout manager and corresponding adapter class
     *
     * @param adapter       the Adapter class that handles the items to be shown
     * @param layoutManager the LayoutManager that handles how the items show on the screen
     */
    private void bindObjectsToRecycler(MemoryListAdapter adapter, LinearLayoutManager layoutManager) {
//        setting the layout manager to stack the memories from the bottom up
        layoutManager.setStackFromEnd(true);
//        setting the activity's RV with the proper layout manager
        super.activity.getRecyclerView().setLayoutManager(layoutManager);
//        setting the activity's RV with the proper adapter
        super.activity.getRecyclerView().setAdapter(adapter);
    }

    /**
     * returns the current caller Memory defined inside the adapter
     *
     * @return the current caller Memory
     */
    public Memory getAdapterCallerMemory() {
        return mAdapter.getCallerMemory();
    }

    @Override
    public void doInWorkerThread() {
//        getting an instance to the underlying activity's loader manager
//        this loader manager is responsible for operating with the loader's communication
        LoaderManager loaderManager = super.activity.getUILoaderManager();
//        starting a new Bundle object that will be used to send data to the loader (in our case the memoryID)
        Bundle bundle = new Bundle();
//      putting inside the Bundle the memoryID with a key that will be used for later retrieval
        bundle.putInt(MEMORY_ID, mAdapter.getCallerMemory().getMemoryID());
//        initializing a new loader object (with the proper data type) using the loader manager and the loader ID we created
        Loader<ArrayList<Memory>> loader = loaderManager.getLoader(this.FETCH_LOADER_ID);
//        checking to see if the loader we got is a new one or it has already been created before
        if (loader == null) {
//            if the loader is new, we are initializing a new one with the loader ID and sending the Bundle with the memoryID along with it
//            we are also defining this class as the callback class that has implemented the proper callback methods
            loaderManager.initLoader(this.FETCH_LOADER_ID, bundle, this).forceLoad();
        } else {
//            if the loader is not new, we are using the same loader to restart it.
//            this will prevent memory leaks.
            loaderManager.restartLoader(this.FETCH_LOADER_ID, bundle, this).forceLoad();
        }
    }

    // This method is called whenever a new loader is asked to be created.
    @Override
    public Loader<ArrayList<Memory>> onCreateLoader(int id, Bundle args) {
//        creating a new instance of our TaskLoader class that will perform the background work and set it to null
//        inside the switch statement we will populate it with the proper data
        TaskLoader loader = null;
        switch (id) {
//            checking to see if the loaderID sent to us is the same as the loader ID that we intend to create
            case 3:
//                since it is we will create a new instance of our TaskLoader class
//                we are sending the context of the activity, the bundle with the data, the loader id and the activity itself
                loader = new TaskLoader(super.activity.getUIContext(), args, super.activity);
//                make sure we break the statement otherwise this will continue to run
                break;
            default:
//                just a default case that so far, it is exactly the same as the case '3'
                loader = new TaskLoader(super.activity.getUIContext(), args, super.activity);
                break;
        }
//        returning the recently created loader
        return loader;
    }

    /**
     * Method that will handle the clicks of the Context Menu of the Recycler View of each ViewHolder
     *
     * @param item the MenuItem sent by the overriden method onContextItemSelected() of the underlying activity
     * @return true if we properly handled the click. False if not.
     */
    public boolean handleContextItemSelected(MenuItem item) {
        int option = item.getItemId();
        Memory memory = mAdapter.getMemoryByPosition(item.getOrder());
        switch (option) {
            case 1:
                // this is the UPDATE case
                // sending the memory object to the TaskPresenter so the updating can be handled
                activity.getTaskPresenter().startUpdateActivity(memory);
                return true;
            case 2:
                // this is the DELETE case
                if (memory.getMemoryID() == 1 || memory.getMemoryID() == getLastHistoryId()) {
                    Toast.makeText(activity, "Can't delete caller Memory", Toast.LENGTH_SHORT).show();
                    return true;
                }
                activity.getTaskPresenter().deleteMemoryFromDB(memory);
                return true;
            case 3:
                // this is the NEW CONNECT case
                // changing the state of the FAB given the current mode of the underlying activity
                changeFabState(isConnectMode());
//                saving into the IV the id of the memory we want to create a new connection
                this.connectId = memory.getMemoryID();
//                reverting the current connect mode
                this.setConnectMode(!isConnectMode());
                return true;
            case 4:
                this.changeFabState(isConnectMode());
                this.setConnectMode(!isConnectMode());
                return true;
	    case 5:
                activity.getTaskPresenter().deleteConnection(memory);
		return true;
            default:
                return false;
        }
    }

    /**
     * Method that changes the state (color and drawable) of the FAB given the current mode (normal or connect)
     *
     * @param mode the current mode that the underlying activity is: normal mode or connection mode
     */
    public void changeFabState(boolean mode) {
//                checking to see if we are in normal mode
        if (!mode) {
//                    changing the background color of the FAB corresponding to the connect mode
            activity.getFab().setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(activity, R.color.fab_connect)));
//                    changing the drawable that the FAB uses corresponding to the connect mode
            activity.getFab().setImageDrawable(ContextCompat.getDrawable(getActivityContext(), R.drawable.ic_compare_arrows_black_24dp));
        } else {
            activity.getFab().setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(activity, R.color.colorAccent)));
            activity.getFab().setImageDrawable(ContextCompat.getDrawable(getActivityContext(), R.drawable.ic_add_24dp));
        }
    }

    // This method is called after the loader has finished its load.
    // So we are expecting a Memory[] object properly populated.
    // In it we are updating the Parent Textview and the Adapter
    @Override
    public void onLoadFinished(Loader<ArrayList<Memory>> loader, ArrayList<Memory> memories) {
//        checking to see if the loader that finished is actually the loader that we started before
//        also checking to see if the memories array has content (if it's not null)
        if (loader.getId() == this.FETCH_LOADER_ID && memories != null) {
//            calling the adapter's method that will retrieve the data from the result array.
            this.mAdapter.updateMemories(memories);
//            calls the activity method to update the state of the RV
            super.activity.updateRVState();
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Memory>> loader) {
//        doing nothing here since we do not have any link to the loader's data to release
    }

    /**
     * returns the adapter that underlies this presenter and the Main activity RV
     *
     * @return the Adapter class instance
     */
    MemoryListAdapter getAdapter() {
        return this.mAdapter;
    }

    /**
     * callback method that handles inside this presenter the clicks in the Recycler View's ViewHolders
     *
     * @param memory the Memory object that is represented by the viewholder that got clicked.
     */
    void handleRVClicks(Memory memory) {
        if (getLastHistoryId() == memory.getMemoryID()) {
            activity.onBackPressed();
            return;
        }
        // adding the current caller memory to the history Arraylist
        this.history.add(mAdapter.getCallerMemory().getMemoryID());
        // setting the new caller memory inside the adapter so their connected memories can be called
        mAdapter.setCallerMemory(memory);
        // initiate the fetching of the connected memories of the new caller memory
        this.doInWorkerThread();
    }

    public ArrayList<Integer> getHistory() {
        return history;
    }

    public void setHistory(ArrayList<Integer> history) {
        this.history = history;
    }

    /**
     * Returns the ID of the most recent memory in the History Array. If there is no memory in the history, it returns 0.
     *
     * @return Id of the most recent memory of the history Array or 0 if no memory found.
     */
    public int getLastHistoryId() {
        if (history.isEmpty()) {
            return 0;
        } else {
            return this.history.get(history.size() - 1);
        }
    }

    /**
     * pops the most recent memory of the History if exists and updates the caller memory of the adapter
     */
    public void delMostRecentHistory() {
        if (!history.isEmpty()) {
//            deletes the most recent ID from the history
            int history_id = history.remove(history.size() - 1);
//            setting the new caller Memory of the Adapter so we can fetch the connected memories
            mAdapter.setCallerMemory(mAdapter.getMemoryById(history_id));
        }
    }

    public boolean checkIdIsOnDisplay(int id) {
        ArrayList<Memory> memories = mAdapter.getMemoriesArr();
        for (Memory memory : memories) {
            if (memory.getMemoryID() == id) return true;
        }
        return false;
    }

    public Context getActivityContext() {
        return this.activity;
    }

    public boolean isConnectMode() {
        return connectMode;
    }

    public void setConnectMode(boolean connectMode) {
        this.connectMode = connectMode;
    }

    public int getConnectId() {
        return connectId;
    }
}
