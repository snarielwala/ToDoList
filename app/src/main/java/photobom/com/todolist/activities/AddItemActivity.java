package photobom.com.todolist.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import photobom.com.todolist.R;
import photobom.com.todolist.helpers.Constants;

public class AddItemActivity extends Activity {

    private static final String TAG = AddItemActivity.class.getSimpleName();
    private static final int RESULT_CODE_CHANGE=1;
    private static final int RESULT_CODE_NO_CHANGE=2;
    private static final String OLD_NAME = "oldName";
    private static final String NEW_NAME = "newName";
    private static final String POSITION = "position";


    private EditText etNewItem;
    private ArrayList<String> items;
    private ArrayAdapter<String> itemsAdapter;
    private ListView lvItems;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.d(TAG,"onCreate Started");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        etNewItem= (EditText)findViewById(R.id.etNewItem);
        lvItems = (ListView) findViewById(R.id.lvItems);

        readItems();
        Log.d(TAG, "Items read from file:" + items.toString());

        itemsAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, items);
        lvItems.setAdapter(itemsAdapter);
        setupListViewListener();
        Log.d(TAG, "onCreate End");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_simple_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupListViewListener() {
        /*
        new item click listener for the list items
         */
        lvItems.setOnItemClickListener(

                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapter,
                                            View item, int pos, long id) {
                        //edit activity called from here using intent
                        //position and name sent to the second activity
                        Intent intent = new Intent(getApplicationContext(), EditItemActivity.class);
                        String selectedFromList = (String) (adapter.getItemAtPosition(pos));
                        intent.putExtra(OLD_NAME, selectedFromList);
                        intent.putExtra(POSITION,pos);

                        startActivityForResult(intent, 1);

                    }
                });

        lvItems.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapter,
                                                   View item, int pos, long id) {
                        // Remove the item within array at position
                        items.remove(pos);
                        // Refresh the adapter
                        itemsAdapter.notifyDataSetChanged();
                        writeItems();
                        return true;
                    }

                });
    }

    /*
    Method called when new item is added.
    Updates the item adapter and writes to the file
     */
    public void onAddItem(View view) {
        Log.d(TAG,"AddItem Started");
        String itemText = etNewItem.getText().toString();
        itemsAdapter.add(itemText);
        etNewItem.setText("");
        writeItems();
        Log.d(TAG, "AddItem Ended, New Item Added:" + itemText);
    }

    /*
    Read method reads all the items from the file
    on startup
     */
    private void readItems() {
        Log.d(TAG,"readItems Started");
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        try {
            items = new ArrayList<String>(FileUtils.readLines(todoFile));

        } catch (IOException e) {
            items = new ArrayList<String>();
        }

        Log.d(TAG,"readItems Ended With items:"+items.toString());
    }

    /*
    Writes to the items to the file
     */
    private void writeItems() {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        try {
            FileUtils.writeLines(todoFile, items);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /*
    On finishing the edit Activity this method is called
    If the user changes the item name,
        the item adapter is updated and new item is written to the file
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==RESULT_CODE_CHANGE) {
            Log.d(TAG, "onActivityResult Called");

            String oldName = data.getStringExtra(Constants.OLD_NAME);
            String newName = data.getStringExtra(Constants.NEW_NAME);
            int position = data.getIntExtra(Constants.POSITION, 1);

            Log.d(TAG, OLD_NAME + ":" + oldName + " " + NEW_NAME + newName+"@" + POSITION +":"+position);

            itemsAdapter.remove(oldName);
            itemsAdapter.insert(newName, position);
            writeItems();
        }
    }
}
