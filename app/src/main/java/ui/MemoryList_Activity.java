package ui;

import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import presenter.LoaderPresenter;
import presenter.TaskPresenter;
import seasonedblackolives.com.meemo.R;

public class MemoryList_Activity extends AppCompatActivity implements UIInterface {

//    IV that will hold the instance of the Loader presenter for this activity
    private LoaderPresenter loaderPresenter;

    //    IV that will hold the instance for the RecyclerView
    private RecyclerView childMemoryRV;

//    IV that holds the instance for the textview that holds the parent memory
    private TextView parentTextView;

//    IV that contains the String that will be the Key to store the RV's state during activity destruction
    private final String RV_STATE_KEY = "RV_STATE_KEY";
//    IV of Parcelable type that will actually store the RV's state
    private Parcelable mRVState = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory_list);

//        binds the views inside the layout to the IVs of this activity
        this.childMemoryRV = (RecyclerView) findViewById(R.id.memory_recycler_view);
        this.parentTextView = (TextView) findViewById(R.id.parent_memory_textview);

//        initiates the loader presenter of this activity sending this activity as a parameter
        this.loaderPresenter = new LoaderPresenter(this);

//        calling the method of the loader to initiate the creation of the Loader
        this.loaderPresenter.doInWorkerThread();

//        Checking to see if the bundle given to us has some data that needs to be initiated because it got saved
        if (savedInstanceState != null) {
//            checking to see if the existing bundle contains the Key assigned to the RV's state
            if (savedInstanceState.containsKey(RV_STATE_KEY)) {
//                retrieving the state from the bundle and assigning it to the proper IV
                this.mRVState = savedInstanceState.getParcelable(RV_STATE_KEY);
            }
        }
    }

    public RecyclerView getRecyclerView() {
        return childMemoryRV;
    }

    /**
     * returns the context of this activity*/
    @Override
    public Context getUIContext() {
        return this;
    }

    /**
     * returns the LoaderManager of this activity for usage inside the loader of the presenter*/
    @Override
    public android.support.v4.app.LoaderManager getUILoaderManager() {
        return getSupportLoaderManager();
    }

    /**
     * Method that returns the Parent Textview so the presenter can update it*/
    public TextView getParentTextView() {
//        returns the textview
        return this.parentTextView;
    }

    /**
     * Method that will return the content resolver for this activity when called.*/
    @Override
    public ContentResolver getUIContentResolver() {
        return getContentResolver();
    }

    /**
     * This method will be called everytime the FAB button gets clicked.
     * This method call is defined inside the XML layout file. */
    public void onFABClick(View view) {

//        initiates the task presenter of this activity sending this activity as a parameter
        new TaskPresenter(this).doInWorkerThread();
    }

    /**
     * returns the instance of the Loader Presenter of this activity.*/
    public LoaderPresenter getLoaderPresenter() {
        return this.loaderPresenter;
    }

    /**
     * This method will automatically be called every time the activity is about to be destroyed.
     * So in it we'll save all the information that we need to properly recreated it later. */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
//        calling the method of the super class of this activity
        super.onSaveInstanceState(outState);
//        saving the state of the RV into our IV. Notice that the state saved is of the type Parcelable
        this.mRVState = this.childMemoryRV.getLayoutManager().onSaveInstanceState();
//        putting the parcelable object (RV's state) into the bundle for saving
        outState.putParcelable(RV_STATE_KEY, mRVState);
    }

    /**
     * This method will be called by the proper presenter everytime the underlying RV updates its dataset.
     * This will get the newly recreated RV and update it with the proper state if is was saved before.*/
    public void updateRVState() {
//        checking to see if we have any state saved to be recreated
        if (mRVState != null) {
//            updating the RV with the saved state.
            this.childMemoryRV.getLayoutManager().onRestoreInstanceState(mRVState);
        }
    }
}
