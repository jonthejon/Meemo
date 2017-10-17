package presenter;

import android.content.ContentValues;
import android.net.Uri;
import android.os.AsyncTask;

import datamanager.DBContract;
import ui.UIInterface;

/**
 * This class that extends AsyncTask will perform simple tasks in a worker thread.
 */

class AddConnectionTask extends AsyncTask<Uri, Void, Integer> {

    //    IV that will hold an instance of the underlying activity of this presenter
    private UIInterface userInterface;
    //    IV that will hold an instance of the presenter that called this task
    private TaskPresenter presenter;

    AddConnectionTask(UIInterface userInterface, TaskPresenter presenter) {
//        defining the IV's of this class with the parameters given
        this.userInterface = userInterface;
        this.presenter = presenter;
    }

    // This method is the one that is actually run in the background
    @Override
    protected Integer doInBackground(Uri... uris) {
//        checking to see if the Uri given is valid. If invalid, returning 0 that corresponds to a failed insertion
        if (uris.length == 0 || uris[0] == null) {
            return 0;
        }
//        retrieving the Uri sent as a parameter
        Uri insertUri = uris[0];
//        retrieving the column name of the memory table to be inserted
        String textColumn = DBContract.MemoryTable.getColMemoryText();
//        getting the memory text from the Memory of the TaskPresenter
        String textMemory = presenter.getMemoryText();
//        initiating a new ContentValues object to store the data to be inserted in the db
        ContentValues cv = new ContentValues();
//        inserting the memory with the proper column name inside the ContentValues
        cv.put(textColumn, textMemory);
//        calling the insert method of the ContentResolver and receiving back the Uri of the inserted memory
//        this will be null if the insertion had failed
        Uri resultUri = userInterface.getUIContentResolver().insert(insertUri, cv);
//        checking to see if the insertion happened well
        if (resultUri != null) {
//            retrieving and returning the ID of the memory just created from the Uri sent to us from the ContentResolver
            return Integer.parseInt(resultUri.getLastPathSegment());
//            something went wrong so we will return 0
        } else return 0;
    }

    // This method is called after the doInBackground finishes and it receives the return data from doInBackground
    @Override
    protected void onPostExecute(Integer integer) {
//        calling the presenter callback method with the flag telling that the insertion happened well or not
        presenter.taskCallback(integer);
    }
}
