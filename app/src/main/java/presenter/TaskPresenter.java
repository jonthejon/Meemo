package presenter;

import android.net.Uri;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Random;

import core.Memory;
import datamanager.DBContract;
import ui.MemoryList_Activity;

/**
 * This is the presenter of the main activity that will perform simple tasks into the DB
 * For more complex tasks, see the LoaderPresenter class.
 */

public class TaskPresenter extends MemoryListPresenter {

//    IV that will hold the Memory object to related to this particular task
    private Memory memory;

    public TaskPresenter(MemoryList_Activity activity) {
//        setting the super class IV with the instance of the underlying activity
        super(activity);
    }

    /**
     * Implemented method of MemoryListPresenter.
     * This implementation will initiate a new AsyncTask to perform simple tasks.*/
    @Override
    public void doInWorkerThread() {
//        retrieving the seconds in the current time for our dummy memory
        Calendar c = Calendar.getInstance();
        int second = c.get(Calendar.SECOND);
//        creating a random memory to be inserted inside the database
        Memory newMemory = new Memory.MemoryBuilder(new Random().nextInt(100),
                "New random memory " + second)
                .build();
//        setting the created memory to the IV of this presenter
        this.setMemory(newMemory);

//        initiating a new Task class that will be responsible for the worker Thread
//        we're sending the proper insert Uri to the method of the Task class
        new Task(super.activity, this).execute(this.createUri());
    }

    public void setMemory(Memory memory) {
//        setting the IV of this class with the memory sent as a parameter
        this.memory = memory;
    }

    public Memory getMemory() {
//        returning the memory object stored as an IV of this class
        return this.memory;
    }

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
    
    private Uri createUri() {
//        getting the memory ID of the memory that is trying to create a new memory
        int callerID = super.activity.getLoaderPresenter().getAdapter().getCallerMemory().getMemoryID();
//        building the Uri with the memory caller ID appended to the path
        return DBContract.MemoryTable.INSERT_MEMORY_URI.buildUpon()
                .appendPath(Integer.toString(callerID)).build();
    }
}
