package presenter;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import core.Memory;
import seasonedblackolives.com.meemo.R;


// ViewHolder that will hold the views of each of the RC list
// this class must extend RecyclerView.ViewHolder in order for it to properly function with recycler view
// in order for the Items of the recyclerview respond to clicks, this class must implement the View.OnClickListener interface

class MemoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    //    This IV is the textview that is inside the single memory layout file
    private TextView mTextView;
    // This IV is the TextView that holds the number of connections of this memory
    private TextView connTextView;
    //    IV that holds the memory object of this particular child inside the RV
    private Memory memory;
    //    IV that will hold the presenter instance that called this VieHolder
    private LoaderPresenter presenter;
    // this is the instance of the cardview that holds the current memory
    private CardView cardView;
//    private CardView cardview;

    /**
     * Constructor necessary override some functionalities for this ViewHolder to properly function
     *
     * @param itemView  this is the View (XML hole view) that was inflated in the Adapter class
     * @param presenter this is the presenter instance that created the adapter and consequently this VIewHOlder
     */
    MemoryViewHolder(View itemView, LoaderPresenter presenter) {
//        calling the super class constructor
        super(itemView);
//        setting the IV that holds the presenter instance
        this.presenter = presenter;
//        binding the textview of the given view to the IV that we have
        this.mTextView = (TextView) itemView.findViewById(R.id.single_memory_text);
        this.connTextView = (TextView) itemView.findViewById(R.id.mem_num_connections);
        this.cardView = (CardView) itemView.findViewById(R.id.cardview);
//        setting this same class to be a clickListener and respond to user clicks on it
        itemView.setOnClickListener(this);
    }

    /**
     * this is not an overriden method from the RecyclerView.ViewHolder superclass.
     * but you must design a method like this to bind an object that holds data for your adapter to the particular Views defined in this class
     *
     * @param memory this is the data type object that holds in it's IVs all the necessary data for the Views in this class
     */
    void bindMemoryToView(Memory memory) {
//        binding the Memory object that we received to the IV
        this.memory = memory;
//        setting the text of the textview with the underlying memory text
        this.mTextView.setText(memory.getMemoryText());
        this.connTextView.setText(Integer.toString(memory.getNumConnections()));
//        checking to see if this memory is a caller and if so, tint the background
        if (presenter.getLastHistoryId() == this.memory.getMemoryID()) {
            this.cardView.setCardBackgroundColor(ContextCompat.getColor(presenter.getActivityContext(), R.color.colorAccent));
        } else {
            this.cardView.setCardBackgroundColor(ContextCompat.getColor(presenter.getActivityContext(), R.color.white));
        }
        }

        //    this method is overriden from the View.OnClickListener interface for handling click functionality
//    don't forget to set the underlying view of this class to have this class as a clicklistener inside your constructor
        @Override
        public void onClick (View v){
//        calling the callback method of the presenter that holds the RV and sending the memory object that binds this viewholder
            presenter.handleRVClicks(this.memory);
        }
    }
