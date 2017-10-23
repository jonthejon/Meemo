package ui;

import android.app.SearchManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import presenter.LoaderPresenter;
import presenter.TaskPresenter;
import seasonedblackolives.com.meemo.R;

public class MemoryList_Activity extends AppCompatActivity implements UIInterface {

    //    IV that will hold the instance of the Loader presenter for this activity
    private LoaderPresenter loaderPresenter;

    //    IV that will hold the instance of the Task presenter for this activity
    private TaskPresenter taskPresenter;

    //    IV that will hold the instance for the RecyclerView
    private RecyclerView childMemoryRV;
    private FloatingActionButton fab;

    //    IV that contains the String that will be the Key to store the RV's state during activity destruction
    private final String RV_STATE_KEY = "RV_STATE_KEY";
    private final String HISTORY_KEY = "HISTORY_KEY";
    private final String MODE_KEY = "MODE_KEY";
    private final String MOVE_KEY = "MOVE_KEY";
    //    IV of Parcelable type that will actually store the RV's state
    private Parcelable mRVState = null;
    private SearchView searchView;
    private Toolbar myToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory_list);

//        binds the views inside the layout to the IVs of this activity
        this.childMemoryRV = (RecyclerView) findViewById(R.id.memory_recycler_view);
        this.fab = (FloatingActionButton) findViewById(R.id.main_fab);
        myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
//        This method sets the toolbar as the app bar for the activity
        setSupportActionBar(myToolbar);
//        myToolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white));
        getSupportActionBar().setTitle(R.string.main_title_appbar);

//        initiates the loader presenter of this activity sending this activity as a parameter
        this.loaderPresenter = new LoaderPresenter(this);

//        Checking to see if the bundle given to us has some data that needs to be initiated because it got saved
        if (savedInstanceState != null) {
//            checking to see if the existing bundle contains the Key assigned to the RV's state
            if (savedInstanceState.containsKey(RV_STATE_KEY)) {
//                retrieving the state from the bundle and assigning it to the proper IV
                this.mRVState = savedInstanceState.getParcelable(RV_STATE_KEY);
            }
            // checking to see if we have a history Key in the bundle
            if (savedInstanceState.containsKey(HISTORY_KEY)) {
                // putting the saved History into the Presenter
                loaderPresenter.setHistory(savedInstanceState.getIntegerArrayList(HISTORY_KEY));
            }
            if (savedInstanceState.containsKey(MODE_KEY)) {
                boolean conn_mode = savedInstanceState.getBoolean(MODE_KEY);
                // updating the presenter with the saved mode
                loaderPresenter.setConnectMode(conn_mode);
//                note that we need to invert the mode boolean value in order to maintain the current state from the saved activity
                loaderPresenter.changeFabState(!conn_mode);
            }
            if (savedInstanceState.containsKey(MOVE_KEY)) {
                boolean move_mode = savedInstanceState.getBoolean(MOVE_KEY);
                // updating the presenter with the saved mode
                loaderPresenter.setMoveMode(move_mode);
//                note that we need to invert the mode boolean value in order to maintain the current state from the saved activity
                loaderPresenter.changeFabState(!move_mode);
            }
        }
        //        checking to see if we have any state saved to be recreated
        if (mRVState != null) {
            //            updating the RV with the saved state.
            this.childMemoryRV.getLayoutManager().onRestoreInstanceState(mRVState);
        }

//        sending the intent that called this activity to the method that will handle how to operate on it
        this.handleIntent();
    }

    @Override
    protected void onNewIntent(Intent intent) {
//        since this activity has a launchmode:singleTop, only one instance of it will be created
//        this means that if we call it more than one time, only the intent will be new
//        the activity has not been restarted, so we need to set the new intent or the getIntent() method will return the same as before
        setIntent(intent);
        this.handleIntent();
    }

    private void handleIntent() {
        Intent intent = getIntent();
        // if the intent has 'ACTION_SEARCH' action, it means that a search triggered this activity
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            // setting the title to display 'search results'
            //getSupportActionBar().setTitle(R.string.search_title_appbar);
            // getting the query from the intent and calling the method that will handle it
            String query = intent.getStringExtra(SearchManager.QUERY);
            if (this.searchView != null) {
               myToolbar.collapseActionView();
            }
            executeSearch(query);
        } else {
            // if we got here, it means that this activity was not initiated from a search so we should proceed normally
            // setting the title to display 'memories'
            //getSupportActionBar().setTitle(R.string.main_title_appbar);
//        calling the method of the loader to initiate the creation of the Loader
            this.loaderPresenter.doInWorkerThread();
        }
    }

    /**
     * Private method that will receive a String query and call the presenter that will execute the search in the DB
     *
     * @param query the String query that was created by the user in the search_view
     */
    private void executeSearch(String query) {
        this.loaderPresenter.searchInWorkerThread(query);
        //Toast.makeText(this, query, Toast.LENGTH_SHORT).show();
    }

    /**
     * returns the RecyclerView instance that shows all memories in this activity
     *
     * @return the RecyclerView that holds all memories
     */
    public RecyclerView getRecyclerView() {
        return childMemoryRV;
    }

    @Override
    public Context getUIContext() {
        return this;
    }

    @Override
    public android.support.v4.app.LoaderManager getUILoaderManager() {
        return getSupportLoaderManager();
    }

    @Override
    public ContentResolver getUIContentResolver() {
        return getContentResolver();
    }

    /**
     * This method will be called everytime the FAB button gets clicked.
     * This method call is defined inside the XML layout file.
     *
     * @param view the view object, in this case the FAB button, that is calling this method.
     */
    public void onFABClick(View view) {
//        if the IV that holds the task presenter is null, initialize it
        if (this.taskPresenter == null) this.taskPresenter = new TaskPresenter(this);
        if (!loaderPresenter.isConnectMode() && !loaderPresenter.isMoveMode()) {
//        calling the presenter's method that will create a new Intent and initiate a new activity
            this.taskPresenter.startAddActivity();
            return;
        }
        if (loaderPresenter.isConnectMode()) {
            int id_a = loaderPresenter.getConnectId();
            int id_b = loaderPresenter.getAdapterCallerMemory().getMemoryID();
            if (id_a == id_b) {
                Toast.makeText(this, "Can't connect a memory to itself", Toast.LENGTH_SHORT).show();
                return;
            }
            if (loaderPresenter.checkIdIsOnDisplay(id_a)) {
                Toast.makeText(this, "Can't duplicate a connection", Toast.LENGTH_SHORT).show();
                return;
            }
            this.taskPresenter.createNewConnection(loaderPresenter.getConnectId(), loaderPresenter.getAdapterCallerMemory().getMemoryID());
            return;
        }
        if (loaderPresenter.isMoveMode()) {
            int toMoveId = loaderPresenter.getConnectId();
            int oldCallerId = loaderPresenter.getCallerMoveId();
            int newCallerId = loaderPresenter.getAdapterCallerMemory().getMemoryID();
            if (oldCallerId == newCallerId) {
                Toast.makeText(this, "Can't move a memory to the place it already is", Toast.LENGTH_SHORT).show();
                return;
            }
            if (toMoveId == newCallerId) {
                Toast.makeText(this, "Can't connect a memory to itself", Toast.LENGTH_SHORT).show();
                return;
            }
            if (loaderPresenter.checkIdIsOnDisplay(toMoveId)) {
                Toast.makeText(this, "The memory is already connected to the destination", Toast.LENGTH_SHORT).show();
                return;
            }
            this.taskPresenter.moveMemory(toMoveId, oldCallerId, newCallerId);
        }
    }

    /**
     * Overriden method of the Activity that gets called after the called activity returns with data and a result
     *
     * @param requestCode the request code that you created when calling the ActivityForResult
     * @param resultCode  a constant defined in the Activity superclass that tells us how is the result came back
     * @param data        the intent object that contains the data that the called activity sent us.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        checking to see if the request code is the same of the add activity and if the result code is OK
        if ((requestCode == taskPresenter.CREATE_MEMORY_REQUEST || requestCode == taskPresenter.UPDATE_MEMORY_REQUEST) && resultCode == RESULT_OK) {
//            checking to see if the intent sent by the activity is not null
            if (data != null) {
//                calling the presenter's method that will handle the result for this activity
                taskPresenter.handleActivityResult(data);
            }
        }
    }

    /**
     * returns the instance of the Loader Presenter of this activity.
     *
     * @return the instance of the LoaderPresenter class of this activity
     */
    public LoaderPresenter getLoaderPresenter() {
        return this.loaderPresenter;
    }


    //This method will automatically be called every time the activity is about to be destroyed.
    //So in it we'll save all the information that we need to properly recreated it later. */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
//        calling the method of the super class of this activity
        super.onSaveInstanceState(outState);
//        saving the state of the RV into our IV. Notice that the state saved is of the type Parcelable
        this.mRVState = this.childMemoryRV.getLayoutManager().onSaveInstanceState();
//        putting the parcelable object (RV's state) into the bundle for saving
        outState.putParcelable(RV_STATE_KEY, mRVState);
        // putting the ArrayList that contains the history into the Bundle
        outState.putIntegerArrayList(HISTORY_KEY, loaderPresenter.getHistory());
        // saving the mode of the current activity
        outState.putBoolean(MODE_KEY, loaderPresenter.isConnectMode());
        outState.putBoolean(MOVE_KEY, loaderPresenter.isMoveMode());
    }

    /**
     * This method will be called by the proper presenter everytime the underlying RV updates its dataset.
     * This will get the newly recreated RV and update it with the proper state if is was saved before.
     */
    public void updateRVState() {
//        checking to see if we have any state saved to be recreated
        if (mRVState != null) {
//            updating the RV with the saved state.
            this.childMemoryRV.getLayoutManager().onRestoreInstanceState(mRVState);
        }
    }

    //    this method is an override and it's automatically called every time the user presses the back button of the phone.
    @Override
    public void onBackPressed() {
//        getting the memory ID of the most recent memory in history
        int historyId = loaderPresenter.getLastHistoryId();
        if (historyId == 0) {
//            if ID = 0, the there is no history and we should just exit the app
            super.onBackPressed();
        } else {
//            we have a valid ID
//            deleting the memory from the history
            loaderPresenter.delMostRecentHistory();
//            calling for another DB fetch because we have a new caller memory
            loaderPresenter.doInWorkerThread();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        inflate the options menu from the XML
        getMenuInflater().inflate(R.menu.options_menu, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        return true;
    }

    //    this method is called every time the user clicks in one of the options in the Context Menu defined in the ViewHolder
//    don't forget that this method is supposed to be declared inside the Activity
    @Override
    public boolean onContextItemSelected(MenuItem item) {
//        this calls a method in the presenter class that will handle the clicks
        boolean handleResult = loaderPresenter.handleContextItemSelected(item);
//        if presenter returns false, then we call the super method
        return handleResult || super.onContextItemSelected(item);
    }

    //    is called every time the user clicks in one of the items in the menu of the app bar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:
                return super.onOptionsItemSelected(item);
            //Toast.makeText(this, "clicked the search button!", Toast.LENGTH_SHORT).show();
            //return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public TaskPresenter getTaskPresenter() {
        if (this.taskPresenter == null) this.taskPresenter = new TaskPresenter(this);
        return taskPresenter;
    }

    public FloatingActionButton getFab() {
        return fab;
    }
}
