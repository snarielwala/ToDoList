package photobom.com.todolist.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Calendar;

import photobom.com.todolist.R;
import photobom.com.todolist.adapters.ItemAdapter;
import photobom.com.todolist.fragments.AddDueDateFragment;
import photobom.com.todolist.fragments.EditItemFragment;
import photobom.com.todolist.helpers.AlarmService;
import photobom.com.todolist.helpers.Constants;
import photobom.com.todolist.models.Item;
import photobom.com.todolist.fragments.EditItemFragment.EditNameDialogListener;
import photobom.com.todolist.fragments.AddDueDateFragment.AddDueDateDialogListener;
/**
 * Created by snarielwala on 12/13/15.
 */

public class AddItemActivity extends AppCompatActivity implements EditNameDialogListener, AddDueDateDialogListener {

    private static final String TAG = AddItemActivity.class.getSimpleName();

    private EditText etNewItem;
    private ArrayList<Item>  items;
    private ItemAdapter itemsAdapter;
    private ListView lvItems;


    private PendingIntent pendingIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.d(TAG, "onCreate Started");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        etNewItem= (EditText)findViewById(R.id.etNewItem);
        lvItems = (ListView) findViewById(R.id.lvItems);

        readItems();
        Log.d(TAG, "Items read from database");

        itemsAdapter = new ItemAdapter(getApplicationContext(), items);
        lvItems.setAdapter(itemsAdapter);
        setupListViewListener();
        Log.d(TAG, "onCreate End");

        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.MONTH, 1 );
        calendar.set(Calendar.YEAR, 2016);
        calendar.set(Calendar.DAY_OF_MONTH, 26);

        calendar.set(Calendar.HOUR_OF_DAY, 00);
        calendar.set(Calendar.MINUTE, 13);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.AM_PM,Calendar.PM);

        Intent myIntent = new Intent(AddItemActivity.this, AlarmService.class);
        pendingIntent = PendingIntent.getService(AddItemActivity.this, 0, myIntent, 0);

        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,System.currentTimeMillis(), 86400000, pendingIntent);
      //  alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(), pendingIntent);

     //end onCreate

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

        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupListViewListener() {
        /*
        new item click listener, called when an item is clicked for edit
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

                        showEditDialog(items.get(pos).getName(),pos, items.get(pos).getDueDate());
                        //startActivityForResult(intent, 1);

                    }
                });
        /*
         long click listener, called when an item is long-clicked for deletion
         */
        lvItems.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapter,
                                                   View item, int pos, long id) {
                        Item itemToBeDeleted = (Item)adapter.getItemAtPosition(pos);
                        Log.d(TAG, "Item to be deleted :" + itemToBeDeleted.getName());
                        itemToBeDeleted.delete();
                        items.remove(pos);
                        itemsAdapter.notifyDataSetChanged();

                        return true;
                    }

                });
    }

    /*
    onAddItem called when a new item is added, persists to db and updates view
     */
    public void onAddItem(View view) {
        Log.d(TAG, "AddItem Started");
        String itemText = etNewItem.getText().toString();
        showAddDueDateDialog(itemText);
        //itemsAdapter.add(Item.addItem(itemText));
        etNewItem.setText("");


    }

    /*
    Read method reads all the items from the database on startup
     */
    private void readItems() {
        Log.d(TAG,"readItems Started");
        Log.d(TAG, "readItems Started");
        items = (ArrayList<Item>)Item.getAllItems();
        Log.d(TAG,"readItems Ended With items:");
    }

    /*
    showEditDialogue box method called by the onClickListener, A dialogue fragment instance is created
     */
    private void showEditDialog(String selectedItemName,int position, String dueDate) {
        FragmentManager fm = getSupportFragmentManager();
        EditItemFragment editNameDialog = EditItemFragment.newInstance(selectedItemName, position, dueDate);
        editNameDialog.show(fm, "fragment_edit_name");
    }

    private void showAddDueDateDialog(String itemName){
        FragmentManager fm = getSupportFragmentManager();
        AddDueDateFragment dueDateFragment = AddDueDateFragment.newInstance(itemName);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.ADD_ITEM_NAME, itemName);
        dueDateFragment.setArguments(bundle);
        dueDateFragment.show(fm,"fragment_due_date");

    }

    /*
     onFinishDialog checkes if the updatedName and the oldName are different, updates only if there is a change
     */
    public void onFinishEditDialog(String inputText,int position, String dueDate) {
        Item itemToBeUpdated= (Item) items.get(position);
        Log.d(TAG, "Update item ID:" + itemToBeUpdated.getId() + "Item Name" + itemToBeUpdated.getName());

        //if(!itemToBeUpdated.getName().equals(inputText)){
        Item.updateItem(itemToBeUpdated.getId(), inputText, dueDate);
        items.get(position).setName(inputText);items.get(position).setDueDate(dueDate);
        itemsAdapter.notifyDataSetChanged();
        //}
    }

    public void onFinishAddDueDateDialog(String itemText, String dueDateString) {
        itemsAdapter.add(Item.addItem(itemText,dueDateString));

    }




    /*
      this method is used only when using another activity and not the dialogue fragment.
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

}
