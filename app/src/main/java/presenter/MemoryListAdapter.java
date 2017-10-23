package presenter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import core.Memory;
import seasonedblackolives.com.meemo.R;

/**
 * Adapter class that will work to bind and recycle each individual memory inside the recycler view
 */

class MemoryListAdapter extends RecyclerView.Adapter<MemoryViewHolder> {

    //    IV that will store the fetched memories given the caller memory
    private ArrayList<Memory> memoriesArr;
    //    IV that will hold the Memory object that is the current caller memory of the memory array
    private Memory callerMemory;
    //    IV that will hold the presenter instance that called this adapter
    private LoaderPresenter presenter;

    MemoryListAdapter(LoaderPresenter presenter) {
//        setting the IV that holds the presenter instance
        this.presenter = presenter;
//        creating a DBUtils initial memory that has an ID of 1 (the same of the BRAIN memory)
//        the memory per se doesn't matter... all we need for the DB call to work is the proper ID of the caller memory set to 1
        this.callerMemory = new Memory.MemoryBuilder(1, presenter.activity.getString(R.string.dummy_init_memory), 0).build();
    }

    // This method is called everytime we need to create a new ViewHolder to hold memories inside the RV
    @Override
    public MemoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        getting the context of the ViewGroup to be able to inflate the layout
        Context context = parent.getContext();
//        get the int id of the layout you'll use in the ViewHolder
        int singleMemoryLayoutID = R.layout.single_memory;
//        get an instance of LayoutInflater using the context you got
        LayoutInflater inflater = LayoutInflater.from(context);
//        create a new View using the inflater with the layout id and the ViewGroup as inputs
        View view = inflater.inflate(singleMemoryLayoutID, parent, false);
//        create and return a new ViewHolder, giving to it's constructor the created View
        return new MemoryViewHolder(view, presenter);
    }

    // This method will bind an existing ViewHolder with a memory that is inside the memory ArrayList of this adapter
    @Override
    public void onBindViewHolder(MemoryViewHolder holder, int position) {
//        retrieving the memory object of the proper position given as a parameter
        Memory memory = this.memoriesArr.get(position);
//        calling the ViewHolder's method to bind and update the child UI with the proper memory
        holder.bindMemoryToView(memory);
    }

    // This method must return the number of memories that we have to display in this RV
    @Override
    public int getItemCount() {
//        first check to see if the ArrayList is null and if it, returns 0 as the size of the dataset for the RV
        if (this.memoriesArr == null) {
            return 0;
        }
//        returns the size of the arraylist (the dataset of the RV)
        return memoriesArr.size();
    }

    /**
     * method that returns the child memory array when called.
     *
     * @return the Arraylist that contains all the memories
     */
    private ArrayList<Memory> getMemoriesArray() {
        return this.memoriesArr;
    }

    /**
     * Returns the corresponding Memory given an ID or null if it does not exists
     *
     * @param id the ID of the memory object that you want to fetch
     * @return the Memory object that corresponds to the ID given
     */
    public Memory getMemoryById(int id) {
        for (Memory memory : memoriesArr) {
            if (memory.getMemoryID() == id) {
                return memory;
            }
        }
//        in case we didn't find the memory, we are just returning the null
        return null;
    }

    /**
     * Method that returns the child Memory given a specific position.
     *
     * @param position the position of the element that you want to read from the collection
     * @return returns the Memory that is in the position requested by the caller
     */
    public Memory getMemoryByPosition(int position) {
        if (position > this.memoriesArr.size() - 1) {
            return null;
        } else {
            return this.memoriesArr.get(position);
        }
    }

    /**
     * method that return the current parent memory when called.
     *
     * @return the instance of the parent memory (the caller memory)
     */
    Memory getCallerMemory() {
        if (this.callerMemory == null) {
            return new Memory.MemoryBuilder(1,"Dummy",0).build();
        }
        return this.callerMemory;
    }

    /**
     * Method that will receive the complete memory array from the DB.
     *
     * @param memories this is the collection that will be the new dataset for the Adapter
     */
    void updateMemories(ArrayList<Memory> memories) {
//        setting the IV arraylist (dataset for the RV) with the resulting memory array
        this.memoriesArr = memories;
//        this method is from the RecyclerView.Adapter class and let's the adapter knows that we have new data to show
        notifyDataSetChanged();
    }

    /**
     * Method that changes the current caller memory for the one sent as a paramenter
     *
     * @param callerMemory the new caller memory
     */
    void setCallerMemory(Memory callerMemory) {
//        setting the IV that holds the instance to the caller memory
        this.callerMemory = callerMemory;
//        updates the UI so the caller memory get's updated
//        presenter.updateCallerMemoryUI(callerMemory);
    }

    public ArrayList<Memory> getMemoriesArr() {
        return memoriesArr;
    }
}
