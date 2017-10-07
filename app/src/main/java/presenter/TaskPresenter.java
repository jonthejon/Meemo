package presenter;

import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import datamanager.DBContract;
import ui.AddMemory_Activity;
import ui.MemoryList_Activity;

/**
 * This is the presenter of the main activity that will perform simple tasks into the DB
 * For more complex tasks, see the LoaderPresenter class.
 */

public class TaskPresenter extends MemoryListPresenter {

    //    IV that will hold the Memory object to related to this particular task
    private String memory_text;

    //    IV that holds the request number for the add_activity to callback this activity
    public final int CREATE_MEMORY_REQUEST = 7;

    public TaskPresenter(MemoryList_Activity activity) {
//        setting the super class IV with the instance of the underlying activity
        super(activity);
    }

    // Implemented method of MemoryListPresenter.
    // This implementation will initiate a new AsyncTask to perform simple tasks.
    @Override
    public void doInWorkerThread() {
//        initiating a new Task class that will be responsible for the worker Thread
//        we're sending the proper insert Uri to the method of the Task class
        new Task(super.activity, this).execute(this.createUri());
    }

    /**
     * sets the IV of this class with the memory sent as a parameter
     *
     * @param memory the String with the memory that will be updated into the TextView
     */
    void setMemoryText(String memory) {
        this.memory_text = memory;
    }

    /**
     * returns the memory String stored as an IV of this class
     *
     * @return the String that contains the memory
     */
    String getMemoryText() {
        return this.memory_text;
    }

    /**
     * This method will be called back by the AsyncTask when the insertion is over
     *
     * @param result the int code of the insertion made by the AsyncTask class
     */
    void taskCallback(int result) {
//        checking to see if the result is bigger than 0. If not, the insertion went wrong.
        if (result > 0) {
//            getting a new instance to the Loader Presenter that underlies the activity
//            this is because is an child class of the loader that has access to the RV dataset
//            and calling the method to call the DB again since we inserted new information inside it
            super.activity.getLoaderPresenter().doInWorkerThread();
        } else {
//            something went wrong... let the user know
            Toast.makeText(super.activity, "Something went wrong...", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Creates the insertion Uri that will be used to insert the new memory into the DB
     *
     * @return the insertion Uri
     */
    private Uri createUri() {
//        getting the memory ID of the memory that is trying to create a new memory
        int callerID = super.activity.getLoaderPresenter().getAdapter().getCallerMemory().getMemoryID();
//        building the Uri with the memory caller ID appended to the path
        return DBContract.MemoryTable.uriInsertMemory().buildUpon()
//        return DBContract.MemoryTable.INSERT_MEMORY_URI.buildUpon()
                .appendPath(Integer.toString(callerID)).build();
    }

    /**
     * This method will call a new ActivityForResult. This means that the called activity will perform a task and return a value as a result for the calling activity.
     */
    public void startAddActivity() {
//        creating a new intent for calling the add_activity
        Intent new_memo_intent = new Intent(super.activity, AddMemory_Activity.class);
//        calling the activity for result. This will ensure that the activity will return with data
        super.activity.startActivityForResult(new_memo_intent, this.CREATE_MEMORY_REQUEST);
    }

    /**
     * This method will be called after the Add Activity has finished. In here we'll need to grab the new memory data and start the insertion into the DB.
     *
     * @param data the Intent sent by the Add Activity containing all the data of the new memory to be inserted
     */
    public void handleActivityResult(Intent data) {
        // getting the memory information from the intent
        String memory_text = data.getStringExtra(DBContract.MemoryTable.getColMemoryText());
        this.setMemoryText(memory_text);
        this.doInWorkerThread();
    }
}
