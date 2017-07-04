package presenter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;

import core.Memory;
import seasonedblackolives.com.meemo.R;

/**
 * Adapter class that will work to bind and recycle each individual memory inside the recycler view
 */

class MemoryListAdapter extends RecyclerView.Adapter<MemoryViewHolder> {

//    IV that will store the children memories of the parent memory
    private ArrayList<Memory> childMemories;
//    IV that will hold the Memory object that is the current parent array
    private Memory parentMemory;
//    IV that will hold the presenter instance that called this adapter
    private LoaderPresenter presenter;

    public MemoryListAdapter(LoaderPresenter presenter) {
//        setting the IV that holds the presenter instance
        this.presenter = presenter;
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

    @Override
    public void onBindViewHolder(MemoryViewHolder holder, int position) {

//        retrieving the memory object of the proper position given as a parameter
//        Memory memory = this.childMemories[position];
        Memory memory = this.childMemories.get(position);
//        calling the ViewHolder's method to bind and update the child UI with the proper memory
        holder.bindMemoryToView(memory);
    }

    @Override
    public int getItemCount() {
//        first check to see if the ArrayList is null and if it, returns 0 as the size of the dataset for the RV
        if (this.childMemories == null) {
            return 0;
        }
//        returns the size of the arraylist (the dataset of the RV)
        return childMemories.size();
    }

    /**
     * method that returns the child memory array when called.*/
    public ArrayList<Memory> getChildsArray() {
//        returns the child memories array
        return this.childMemories;
    }

    /** method that return the current parent memory when called.*/
    public Memory getParentMemory() {

        if (this.parentMemory != null) {
//            returns the current parent memory
            return parentMemory;
        } else {
//            this is a dummy command to give a dummy memory in case the parent memory has not yet been created.
//            this code will actually force the db to fetch the 1st memory and it's children
            return new Memory.MemoryBuilder(-11, "GETTING_MOTHER_MEMORY", 0).build();
        }
    }

    /**
     * Method that will receive the complete memory array from the DB.
     * It then will separate the first memory (the parent memory) from the children.
     * */
    public void updateMemories(ArrayList<Memory> memories) {

//        stores the parent memory into the proper IV and remove it from the arraylist
//          (remember that it is the first memory)
        this.parentMemory = memories.remove(0);

//        reversing the order of the children arraylist. Remember that this arrayList can still have a size 0
        Collections.reverse(memories);
//        setting the IV arraylist (dataset for the RV) with the resulting memory array
        this.childMemories = memories;
//        this method is from the RecyclerView.Adapter class and let's the adapter knows that we have new data to show
        notifyDataSetChanged();
    }

    public void addChild(Memory memory) {
//        adding the memory to the first position. Remember that the order of the children is reversed
        childMemories.add(0, memory);
//        notifying the adapter that the dataset has changed
        notifyDataSetChanged();
    }

    /**
     * Method that returns the child Memory given a specific position.
     * */
    public Memory getChildByPosition(int position) {
//        returns the child memory in the given position
        return this.childMemories.get(position);
    }
}
