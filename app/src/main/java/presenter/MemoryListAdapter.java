package presenter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import core.Memory;
import seasonedblackolives.com.meemo.R;

/**
 * Adapter class that will work to bind and recycle each individual memory inside the recycler view
 */

class MemoryListAdapter extends RecyclerView.Adapter<MemoryViewHolder> {

//    fake IV that will hold an array to store the memories
    private Memory[] childMemories;

//    IV that will hold the Memory object that is the current parent array
    private Memory parentMemory;

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
        return new MemoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MemoryViewHolder holder, int position) {

//        retrieving the memory object of the proper position given as a parameter
        Memory memory = this.childMemories[position];
//        calling the ViewHolder's method to bind and update the child UI with the proper memory
        holder.bindMemoryToView(memory);
    }

    @Override
    public int getItemCount() {
        if (this.childMemories == null) {
            return 0;
        }
        return childMemories.length;
    }

    /**
     * method that returns the child memory array when called.*/
    public Memory[] getChildsArray() {
//        returns the child memories array
        return this.childMemories;
    }

    /** method that return the curent parent memory when called.*/
    public Memory getParentMemory() {

        if (this.parentMemory != null) {
//            returns the current parent memory
            return parentMemory;
        } else {
//            this is a dummy command to give a dummy memory in case the parent memory has not yet been created.
//            later this will change so we can call the mother of all memories from the DB
            return new Memory(1, "BRAIN", 0, new int[0], "");
        }
    }

    /**
     * Method that will receive the complete memory array from the DB.
     * It then will separate the first memory (the parent memory) from the children.
     * */
    public void updateMemories(Memory[] memories) {

//        stores the parent memory into the proper IV (remember that it is the first memory)
        this.parentMemory = memories[0];

//        calculates the number of children that this memory has
        int numChildren = memories.length - 1;

//        checks to see if the memory has any children
        if (numChildren > 0) {
//            creating a new children array with the proper size
            this.childMemories = new Memory[numChildren];
//            since it has, populates the child array with the memories of the children
            System.arraycopy(memories, 1, this.childMemories, 0, numChildren);
        } else {
//            since it does not, make sure that we are setting the child memory array to null
//            notice that we are defining a memory with no children as a memory with a null array
            this.childMemories = null;
        }

//        this method is from the RecyclerView.Adapter class and let's the adapter knows that we have new data to show
        notifyDataSetChanged();
    }

    /**
     * Method that returns the child Memory given a specific position.
     * */
    public Memory getChildByPosition(int position) {
//        returns the child memory in the given position
        return this.childMemories[position];
    }
}
