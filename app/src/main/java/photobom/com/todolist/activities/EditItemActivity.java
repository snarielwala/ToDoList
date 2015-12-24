package photobom.com.todolist.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;
import photobom.com.todolist.R;
import photobom.com.todolist.helpers.Constants;

/**
 * Created by snarielwala on 12/13/15.
 */

public class EditItemActivity extends AppCompatActivity {

    private static final String TAG = EditItemActivity.class.getSimpleName();

    private EditText etEditItem;
    private ArrayList<String> items;
    private String oldName;
    private String newName;
    private int position;

    /*
     onCreate called as soon as this activity is created. Initializer.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        etEditItem=(EditText)findViewById(R.id.etEditItem);
        oldName=(String) getIntent().getStringExtra(Constants.OLD_NAME);
        position= getIntent().getIntExtra(Constants.POSITION,1);
        Log.d(TAG, "onCreate: oldItemName=" + oldName +" position:"+position);
        etEditItem.setText(oldName);

    }

    /*
      On Save method is called when the save button is clicked
      fetches the old name, the new name entered and sends it
      to the previous activity if they are not the same
     */

    public void onSave(View view) {
        Log.d(TAG,"On Save Called");
        newName=etEditItem.getText().toString();
        Log.d(TAG,"New Name:"+ newName);

        if(newName.equals(oldName)) {
            setResult(Constants.RESULT_CODE_NO_CHANGE,null);
            finish();
        }
        else {
            Intent output = new Intent();
            output.putExtra(Constants.NEW_NAME, newName);
            output.putExtra(Constants.OLD_NAME,oldName);
            output.putExtra(Constants.POSITION,position);
            setResult(RESULT_OK, output);
            finish();
        }
    }

}



