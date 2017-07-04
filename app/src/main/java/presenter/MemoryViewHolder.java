package presenter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import core.Memory;
import seasonedblackolives.com.meemo.R;

/**
 * ViewHolder that will hold the views of each of the RC list
 * Each ViewHolder will hold one memory
 */
class MemoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

//    This IV is the textview that is inside the single memory layout file
    private TextView mTextView;
//    IV that holds the memory object of this particular child inside the RV
    private Memory memory;
//    IV that will hold the presenter instance that called this VieHolder
    private LoaderPresenter presenter;

    MemoryViewHolder(View itemView, LoaderPresenter presenter) {

//        calling the super class constructor
        super(itemView);

//        setting the IV that holds the presenter instance
        this.presenter = presenter;

//        binding the textview of the given view to the IV that we have
        this.mTextView = (TextView) itemView.findViewById(R.id.single_memory_text);

//        setting this same class to be a clickListener and respond to user clicks on it
        itemView.setOnClickListener(this);
    }

    void bindMemoryToView(Memory memory) {

//        binding the Memory object that we received to the IV
        this.memory = memory;
//        setting the text of the textview with the underlying memory text
        this.mTextView.setText(memory.getMemoryText());
    }

    @Override
    public void onClick(View v) {
//        calling the callback method of the presenter that holds the RV and sending the memory object that binds this viewholder
        presenter.handleRVClicks(this.memory);
    }
}
