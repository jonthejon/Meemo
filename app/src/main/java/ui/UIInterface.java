package ui;

import android.content.ContentResolver;
import android.content.Context;
import android.support.v4.app.LoaderManager;

/**
 * This interface will be used to enable communication between the UI classes and the inner classes of the app
 */

public interface UIInterface {

    /**
     * returns the context of this activity
     * @return the Context of this activity
     */
    Context getUIContext();

    /**
     * returns the LoaderManager of this activity for usage inside the loader of the presenter
     * @return the supportLoaderManager of this activity
     */
    LoaderManager getUILoaderManager();

    /**
     * Method that will return the content resolver for this activity when called.
     * @return returns the ContentResolver for the activity in which it was deployed.
     */
    ContentResolver getUIContentResolver();
}
