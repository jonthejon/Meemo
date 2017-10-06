package ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import datamanager.DBContract;
import seasonedblackolives.com.meemo.R;

public class AddMemory_Activity extends AppCompatActivity {

//    IV that holds the instance for the EditText with the memory to be inserted
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_memory);
        editText = (EditText) findViewById(R.id.new_memory_edit_text);
    }

    /**
     *  this method is defined inside the XML layout file and is triggered if the user clicks the cancel button
     *  @param view the view object that called this function 
     */
    public void onCancelClick(View view) {
	    // the user clicked the cancel button, so we'll just finish this activity and call back the caller activity
        finish();
    }

    /**
     *  this method is defined inside the XML layout file and is triggered if the user clicks the cancel button
     *  @param view the view object that called this function 
     */
    public void onSaveClick(View view) {
        String memory_text = editText.getText().toString();
        if (memory_text.length() == 0) {
            Toast.makeText(this, "Please, insert new memory.", Toast.LENGTH_SHORT).show();
            return;
        }
	// creating the intent that will send back to the activity the information that must be inserted into the DB
        Intent resultIntent = new Intent();
	// putting into the intent the name of the column of the DB that we'll insert and the actual value of the memory to be inserted
        resultIntent.putExtra(DBContract.MemoryTable.COL_MEMORY_TEXT, memory_text);
	// this method creates the Result of this activity that can be accessed by the caller activity
        setResult(RESULT_OK, resultIntent);
	// ending the activity. the result will be accessed later by the caller activity
        finish();
    }
}
