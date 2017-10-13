package presenter;

import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import core.Memory;
import datamanager.DBContract;
import ui.AddMemory_Activity;
import ui.MemoryList_Activity;

/**
 * This is the presenter of the main activity that will perform simple tasks into the DB
 * For more complex tasks, see the LoaderPresenter class.
 */

public class TaskPresenter extends MemoryListPresenter {

    //    IV that will hold the Memory object to related to this particular task
    //private String memory_text;
    private Memory memory;

    //    IV that holds the request number for the add_activity to callback this activity
    public final int CREATE_MEMORY_REQUEST = 7;
    public final int UPDATE_MEMORY_REQUEST = 11;

    // this String will hold the key that we will use in the sending of the old memory text 
    public final String OLD_MEMORY = "OLD_MEMORY";

    //    this IV will sign to the doInWorkerThread() method what kind of uri it should create
    public int uriType = 3;

    public TaskPresenter(MemoryList_Activity activity) {
//        setting the super class IV with the instance of the underlying activity
        super(activity);
        this.memory = new Memory.MemoryBuilder(0, "dummy", 0).build();
    }

    // Implemented method of MemoryListPresenter.
    // This implementation will initiate a new AsyncTask to perform simple tasks.
    @Override
    public void doInWorkerThread() {
//        checking to see what kind of Uri should we create
        if (this.uriType == 3) {
//        initiating a new CreateTask class that will be responsible for the worker Thread
            Toast.makeText(activity, "Creating", Toast.LENGTH_SHORT).show();
            new CreateTask(super.activity, this).execute(this.createInsertUri());
        } else if (this.uriType == 5) {
            Toast.makeText(activity, "Updating", Toast.LENGTH_SHORT).show();
            new UpdateTask(super.activity, this).execute(this.createUpdateUri());
        } else if (this.uriType == 7) {
            Toast.makeText(activity, "Deleting", Toast.LENGTH_SHORT).show();
            new DeleteTask(super.activity, this).execute(this.createDeleteUri());
        }
    }

    /**
     * returns the memory String stored as an IV of this class
     *
     * @return the String that contains the memory
     */
    String getMemoryText() {
        return this.memory.getMemoryText();
        //return this.memory_text;
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
    private Uri createInsertUri() {
//        getting the memory ID of the memory that is trying to create a new memory
        int callerID = super.activity.getLoaderPresenter().getAdapter().getCallerMemory().getMemoryID();
//        building the Uri with the memory caller ID appended to the path
        return DBContract.MemoryTable.uriInsertMemory().buildUpon()
//        return DBContract.MemoryTable.INSERT_MEMORY_URI.buildUpon()
                .appendPath(Integer.toString(callerID)).build();
    }

    /**
     * creates the update Uri that will be used to update the text of a memory into the DB
     *
     * @return the update Uri
     */
    private Uri createUpdateUri() {
        int updateID = this.memory.getMemoryID();
        return DBContract.MemoryTable.uriUpdateMemory().buildUpon()
                .appendPath(Integer.toString(updateID)).build();
    }

    /**
     * creates the delete Uri that will be used to deletea memory from the DB
     *
     * @return the delete Uri
     */
    private Uri createDeleteUri() {
        int deleteID = this.memory.getMemoryID();
        return DBContract.MemoryTable.uriDeleteMemory().buildUpon()
                .appendPath(Integer.toString(deleteID)).build();
    }

    /**
     * This method will take a Memory object and delete it from the DB using a different Thread
     *
     * @param memory the memory object that will be deleted
     */
    public void deleteMemoryFromDB(Memory memory) {
        this.memory = memory;
        this.uriType = 7;
        this.doInWorkerThread();
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
     * This method will also call a new ActivityForResult, but this time with the intent of updating the current memory given as a parameter.
     *
     * @param memory the Memory object that must be updated
     */
    public void startUpdateActivity(Memory memory) {
        // saving the memory that will be updated into the proper IV of this presenter
        this.memory = memory;
        //this.memory.setMemoryID(memory.getMemoryID());
        Intent update_memo_intent = new Intent(super.activity, AddMemory_Activity.class);
        // putting into the Intent the Memory text that must be updated
        update_memo_intent.putExtra(OLD_MEMORY, memory.getMemoryText());
        // starting the new activity with the constructed intent
        super.activity.startActivityForResult(update_memo_intent, this.UPDATE_MEMORY_REQUEST);
    }

    /**
     * This method will be called after the Add Activity has finished. In here we'll need to grab the new memory data and start the insertion into the DB.
     *
     * @param data the Intent sent by the Add Activity containing all the data of the new memory to be inserted
     */
    public void handleActivityResult(Intent data) {
        // getting the memory information from the intent
        String memory_text = data.getStringExtra(DBContract.MemoryTable.getColMemoryText());
        // setting the memory holder of this presenter with the current value
        this.memory.setMemoryText(memory_text);
        // updating the uriType with the type given by the add_activity
        this.uriType = data.getIntExtra("RESULT_TYPE", 5);
        this.doInWorkerThread();
    }
}
