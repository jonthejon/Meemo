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

    public MemoryListAdapter(LoaderPresenter presenter) {
//        setting the IV that holds the presenter instance
        this.presenter = presenter;
//        creating a dummy initial memory that has an ID of 1 (the same of the BRAIN memory)
//        the memory per se doesn't matter... all we need for the DB call to work is the proper ID of the caller memory set to 1
        this.callerMemory = new Memory.MemoryBuilder(1, presenter.activity.getString(R.string.dummy_init_memory)).build();
    }

    @Override
    public MemoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        getting the context of the ViewGroup to be able to inflate the layout
        Context context = parent.getContext();
//        get the int id of the layout you'll use in the ViewHolder
        int singleMemoryLayoutID = R.layout.single_memory;
//        get an instance of LayoutInflater using the context you got
        LayoutInflater inflater = LayoutInflater.from(context);
//        create a new View using the inflater with the layout id and the ViewGroup as inputs
        View view = inflater.inflate(singleMemoryLayoutID,parent,false);
//        create and return a new ViewHolder, giving to it's constructor the created View
        return new MemoryViewHolder(view, presenter);
    }

// If you need the position of an item later on (e.g. in a click listener), use RecyclerView.ViewHolder.getAdapterPosition() which will have the updated adapter position.
    @Override
    public void onBindViewHolder(MemoryViewHolder holder, int position) {

//        retrieving the memory object of the proper position given as a parameter
//        Memory memory = this.memoriesArr[position];
        Memory memory = this.memoriesArr.get(position);
//        calling the ViewHolder's method to bind and update the child UI with the proper memory
        holder.bindMemoryToView(memory);
    }

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
     * method that returns the child memory array when called.*/
    public ArrayList<Memory> getMemoriesArray() {
//        returns the child memories array
        return this.memoriesArr;
    }

    /** method that return the current parent memory when called.*/
    public Memory getCallerMemory() {
        return this.callerMemory;
    }

    /**
     * Method that will receive the complete memory array from the DB.
     * @param memories this is the collection that will be the new dataset for the Adapter
     * */
    public void updateMemories(ArrayList<Memory> memories) {

//        setting the IV arraylist (dataset for the RV) with the resulting memory array
        this.memoriesArr = memories;
//        this method is from the RecyclerView.Adapter class and let's the adapter knows that we have new data to show
        notifyDataSetChanged();
    }

    /**
     * Method that will add one single Memory into the Collection of this adapter and notify it of the change
     * @param memory the Memory object that will be inserted inside the collection
     */
    public void addChild(Memory memory) {
//        adding the memory to the first position. Remember that the order of the children is reversed
        memoriesArr.add(0, memory);
//        notifying the adapter that the dataset has changed
        notifyDataSetChanged();
    }

    /**
     * Method that returns the child Memory given a specific position.
     * @param position the position of the element that you want to read from the collection
     * @return returns the Memory that is in the position requested by the caller
     * */
    public Memory getMemoryByPosition(int position) {
//        returns the child memory in the given position
        return this.memoriesArr.get(position);
    }

    public void setCallerMemory(Memory callerMemory) {
//        setting the IV that holds the instance to the caller memory
        this.callerMemory = callerMemory;
//        updates the UI so the caller memory get's updated
        presenter.updateCallerMemoryUI(callerMemory);
    }
}
