package photobom.com.todolist.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;

import photobom.com.todolist.R;

public class EditItemActivity extends Activity {

    private static final String TAG = EditItemActivity.class.getSimpleName();
    private static final int RESULT_CODE_CHANGE=1;
    private static final int RESULT_CODE_NO_CHANGE=2;
    private static final String OLD_NAME = "oldName";
    private static final String NEW_NAME = "newName";
    private static final String POSITION = "position";

    private EditText etEditItem;
    private ArrayList<String> items;
    private String oldName;
    private String newName;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        etEditItem=(EditText)findViewById(R.id.etEditItem);
        oldName=getIntent().getStringExtra(OLD_NAME);
        position=getIntent().getIntExtra(POSITION,1);
        Log.d(TAG, "onCreate: oldName=" + oldName +" Position:"+position);
        etEditItem.setText(oldName);

    }

    /*
      On Save method is called when the save button is clicked
      fetches the old name, the new name entered and sends it
      to the previous activity if they are not the same
     */

    public void onSave(View view) {
        newName=etEditItem.getText().toString();
        if(newName.equals(oldName)) {
            setResult(RESULT_CODE_NO_CHANGE, null);
            finish();
        }

        else {
            Intent output = new Intent();
            output.putExtra(OLD_NAME, oldName);
            output.putExtra(NEW_NAME, newName);
            output.putExtra(POSITION,position);

            setResult(RESULT_CODE_CHANGE, output);
            finish();
        }
    }

}
