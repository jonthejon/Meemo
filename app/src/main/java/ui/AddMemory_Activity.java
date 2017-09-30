package ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import datamanager.DBContract;
import seasonedblackolives.com.meemo.R;

// TODO: 06/07/17
public class AddMemory_Activity extends AppCompatActivity {

//    IV that holds the instance for the EditText with the memory to be inserted
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: 06/07/17
        setContentView(R.layout.activity_add_memory);
        editText = (EditText) findViewById(R.id.new_memory_edit_text);
    }

    // TODO: 06/07/17  
    public void onCancelClick(View view) {
        finish();
//        Toast.makeText(this, "Cancel button clicked.", Toast.LENGTH_SHORT).show();
    }

    // TODO: 06/07/17

    public void onSaveClick(View view) {
        String memory_text = editText.getText().toString();
        if (memory_text.length() == 0) {
            Toast.makeText(this, "Please, insert new memory.", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent resultIntent = new Intent();
        resultIntent.putExtra(DBContract.MemoryTable.COL_MEMORY_TEXT, memory_text);
        setResult(RESULT_OK, resultIntent);
        finish();
    }
}
