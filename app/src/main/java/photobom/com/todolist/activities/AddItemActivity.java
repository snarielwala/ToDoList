package photobom.com.todolist.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import photobom.com.todolist.R;
import photobom.com.todolist.fragments.EditItemFragment;
import photobom.com.todolist.helpers.Constants;
import photobom.com.todolist.models.Item;
import photobom.com.todolist.fragments.EditItemFragment.EditNameDialogListener;

/**
 * Created by snarielwala on 12/13/15.
 */

public class AddItemActivity extends AppCompatActivity implements EditNameDialogListener {

    private static final String TAG = AddItemActivity.class.getSimpleName();

    private EditText etNewItem;
    private ArrayList<Item> items;
    private ArrayAdapter<Item> itemsAdapter;
    private ListView lvItems;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.d(TAG, "onCreate Started");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        etNewItem= (EditText)findViewById(R.id.etNewItem);
        lvItems = (ListView) findViewById(R.id.lvItems);

        readItems();
        Log.d(TAG, "Items read from database");

        itemsAdapter = new ArrayAdapter<Item>(this,
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
                        //Intent intent = new Intent(getApplicationContext(), EditItemActivity.class);
                        //intent.putExtra(Constants.OLD_NAME, items.get(pos).getName());
                        //intent.putExtra(Constants.POSITION,pos);
                        //Log.d(TAG, "SelectedItem for update Name:" + items.get(pos).getName());

                        showEditDialog(items.get(pos).getName(),pos);
                        //startActivityForResult(intent, 1);

                    }
                });

        lvItems.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapter,
                                                   View item, int pos, long id) {
                        Item itemToBeDeleted = (Item)adapter.getItemAtPosition(pos);
                        Log.d(TAG, "Item to be deleted :" + itemToBeDeleted.getName());
                        itemToBeDeleted.delete();
                        // Remove the item within array at position
                        items.remove(pos);
                        // Refresh the adapter
                        itemsAdapter.notifyDataSetChanged();

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
        Item newItem=Item.addItem(itemText);
        itemsAdapter.add(newItem);
        etNewItem.setText("");

        Log.d(TAG, "AddItem Ended, New Item Added:" + itemText);
    }

    /*
    Read method reads all the items from the file
    on startup
     */
    private void readItems() {
        Log.d(TAG,"readItems Started");
        Log.d(TAG, "readItems Started");
        items = (ArrayList<Item>)Item.getAllItems();
        Log.d(TAG,"readItems Ended With items:");
    }

    /*
    On finishing the edit Activity this method is called
    If the user changes the item name,
        the item adapter is updated and new item is written to the file
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==Constants.RESULT_CODE_CHANGE) {
            Log.d(TAG, "onActivityResult Called");

            String oldName = data.getStringExtra(Constants.OLD_NAME);
            String newName = data.getStringExtra(Constants.NEW_NAME);
            int position = data.getIntExtra(Constants.POSITION, 1);

            Log.d(TAG, Constants.OLD_NAME + ":" + oldName + " " + Constants.NEW_NAME + newName + "@" + Constants.POSITION + ":" + position);

            Item itemToBeUpdated= (Item) items.get(position);
            Log.d(TAG,"Update item ID:"+itemToBeUpdated.getId() + "Item Name"+itemToBeUpdated.getName());
            Item.updateItem(itemToBeUpdated.getId(), newName);
            items.get(position).setName(newName);
            itemsAdapter.notifyDataSetChanged();
        }
    }

    private void showEditDialog(String selectedItemName,int position) {
        FragmentManager fm = getSupportFragmentManager();
        EditItemFragment editNameDialog = EditItemFragment.newInstance(selectedItemName,position);
        editNameDialog.show(fm, "fragment_edit_name");
    }

    public void onFinishEditDialog(String inputText,int position) {
        Item itemToBeUpdated= (Item) items.get(position);
        Log.d(TAG,"Update item ID:"+itemToBeUpdated.getId() + "Item Name"+itemToBeUpdated.getName());
        Item.updateItem(itemToBeUpdated.getId(), inputText);
        items.get(position).setName(inputText);
        itemsAdapter.notifyDataSetChanged();
    }


}
