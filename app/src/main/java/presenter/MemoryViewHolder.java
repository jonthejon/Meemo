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

class MemoryViewHolder extends RecyclerView.ViewHolder {

//    This IV is the textview that is inside the single memory layout file
    private TextView mTextView;
//    IV that holds the memory object of this particular child inside the RV
    private Memory memory;

    MemoryViewHolder(View itemView) {

//        calling the super class constructor
        super(itemView);

//        binding the textview of the given view to the IV that we have
        this.mTextView = (TextView) itemView.findViewById(R.id.single_memory_text);
    }

    void bindMemoryToView(Memory memory) {

//        binding the Memory object that we received to the IV
        this.memory = memory;
//        setting the text of the textview with the underlying memory text
        this.mTextView.setText(memory.getMemoryText());
    }
}
