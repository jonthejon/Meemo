package ui;

import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import presenter.FetchPresenter;
import seasonedblackolives.com.meemo.R;

public class MemoryList_Activity extends AppCompatActivity implements UIInterface {

//    IV that will hold the instance of the presenter for this activity
    private FetchPresenter mPresenter;

//    IV that will hold the instance for the RecyclerView
    private RecyclerView childMemoryRV;

//    IV that holds the instance for the textview that holds the parent memory
    private TextView parentTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory_list);

//        binds the views inside the layout to the IVs of this activity
        this.childMemoryRV = (RecyclerView) findViewById(R.id.memory_recycler_view);
        this.parentTextView = (TextView) findViewById(R.id.parent_memory_textview);

//        initiates the presenter of this activity sending this activity as a parameter
        this.mPresenter = new FetchPresenter(this);
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
     * method that will be called whenever any other class needs an instance of the interface for communication*/
    public UIInterface getUIInterface() {
        return this;
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
}
