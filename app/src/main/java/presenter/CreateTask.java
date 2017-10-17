package presenter;

import android.content.ContentValues;
import android.net.Uri;
import android.os.AsyncTask;

import datamanager.DBContract;
import ui.UIInterface;

/**
 * This class that extends AsyncTask will perform simple tasks in a worker thread.
 */

//class CreateTask extends AsyncTask<Uri, Void, Integer> {
class CreateTask extends AsyncTask<Integer, Void, Integer> {

    //    IV that will hold an instance of the underlying activity of this presenter
    private UIInterface userInterface;
    //    IV that will hold an instance of the presenter that called this task
    private TaskPresenter presenter;

    CreateTask(UIInterface userInterface, TaskPresenter presenter) {
//        defining the IV's of this class with the parameters given
        this.userInterface = userInterface;
        this.presenter = presenter;
    }

    // This method is the one that is actually run in the background
    @Override
    //protected Integer doInBackground(Uri... uris) {
    protected Integer doInBackground(Integer... integers) {
        if (integers.length == 0 || integers[0] == null) {
            return 0;
        }
//        retrieving the Uri sent as a parameter
        int uriType = integers[0];
        String textColumn;
        String textMemory;
        int numRows;
        // this is the proper Uri that has been set in the presenter class
        Uri uri = presenter.getUri();
//        initiating a new ContentValues object to store the data to be inserted in the db
        ContentValues cv = new ContentValues();
        switch (uriType) {
            case 3:
//        retrieving the column name of the memory table to be inserted
                textColumn = DBContract.MemoryTable.getColMemoryText();
//        getting the memory text from the Memory of the TaskPresenter
                textMemory = presenter.getMemoryText();
//        inserting the memory with the proper column name inside the ContentValues
                cv.put(textColumn, textMemory);
//        calling the insert method of the ContentResolver and receiving back the Uri of the inserted memory
//        this will be null if the insertion had failed
                Uri resultUri = userInterface.getUIContentResolver().insert(uri, cv);
//        checking to see if the insertion happened well
                if (resultUri != null) {
//            retrieving and returning the ID of the memory just created from the Uri sent to us from the ContentResolver
                    return Integer.parseInt(resultUri.getLastPathSegment());
//            something went wrong so we will return 0
                } else return 0;
            case 5:
//        retrieving the column name of the memory table to be inserted
                textColumn = DBContract.MemoryTable.getColMemoryText();
//        getting the memory text from the Memory of the TaskPresenter
                textMemory = presenter.getMemoryText();
//        inserting the memory with the proper column name inside the ContentValues
                cv.put(textColumn, textMemory);
                numRows = userInterface.getUIContentResolver().update(uri, cv, null, null);
                if (numRows == 1) {
                    return numRows;
//            something went wrong so we will return 0
                } else return 0;
            case 7:
                // calling the delete method of the content provider and having it execute
                numRows = userInterface.getUIContentResolver().delete(uri, null, null);
                if (numRows == 1) {
                    return numRows;
//            something went wrong so we will return 0
                } else return 0;
            case 11:
                Uri resConnUri = userInterface.getUIContentResolver().insert(uri, null);
                if (resConnUri != null) {
                    return 1;
                }
                return 0;
	    case 13:
                numRows = userInterface.getUIContentResolver().delete(uri, null, null);
                if (numRows == 1) {
                    return numRows;
//            something went wrong so we will return 0
                } else return 0;
            default:
                return 0;
        }
    }

    // This method is called after the doInBackground finishes and it receives the return data from doInBackground
    @Override
    protected void onPostExecute(Integer integer) {
//        calling the presenter callback method with the flag telling that the insertion happened well or not
        presenter.taskCallback(integer);
    }


}

