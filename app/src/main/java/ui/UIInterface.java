package ui;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.app.LoaderManager;

/**
 * This interface will be used to enable communication between the UI classes and the inner classes of the app
 */

public interface UIInterface {

//    returns the context of the activity
    Context getUIContext();

//    returns the LoaderManager of the activity
    LoaderManager getUILoaderManager();

//    returns the resources for the given activity
    Resources getUIResources();
}
