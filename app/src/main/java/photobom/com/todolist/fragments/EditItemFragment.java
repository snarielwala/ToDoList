package photobom.com.todolist.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import photobom.com.todolist.R;
import photobom.com.todolist.activities.AddItemActivity;
import photobom.com.todolist.helpers.Constants;


/**
 * Created by snarielwala on 12/16/15.
 */
public class EditItemFragment extends DialogFragment implements View.OnClickListener {

    private static final String TAG = AddItemActivity.class.getSimpleName();


    private EditText fetEditItem;
    private int position;
    private Button saveButton;
    private String selectedItemName;
    private TextView editDueDateTextView;
    private static SimpleDateFormat dueDate;
    private static String dueDateString;
    private Button changeDueDateButton;




    private String editDueDate;


    public EditItemFragment() {
    }

    /*
    Read method reads all the items from the database on startup
     */
    public interface EditNameDialogListener {
        void onFinishEditDialog(String oldName, int position, String dueDate);
    }


    /*
    Additional constructor for the editItemFragment that accepts a string and an int
     */
    public static EditItemFragment newInstance(String selectedItemName, int position, String dueDate) {
        EditItemFragment frag = new EditItemFragment();
        Bundle args = new Bundle();
        args.putString(Constants.SELECTED_ITEM, selectedItemName);
        args.putInt(Constants.POSITION, position);
        args.putString(Constants.DUE_DATE, dueDate);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        editDueDate =  getArguments().getString(Constants.DUE_DATE);

        return inflater.inflate(R.layout.fragment_edit_name, container);
    }

    /*
    sets up the view for the dialogue fragment
     */
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        changeDueDateButton = (Button) view.findViewById(R.id.changeDueDate);
        changeDueDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getFragmentManager(), "DatePicker");
            }
        });

        editDueDateTextView = (TextView) view.findViewById(R.id.editDueDateText);
        editDueDateTextView.setText(getArguments().getString(Constants.DUE_DATE));

        fetEditItem = (EditText) view.findViewById(R.id.fetEditItem);
        selectedItemName = getArguments().getString(Constants.SELECTED_ITEM);
        position=getArguments().getInt(Constants.POSITION);
        fetEditItem.setText(selectedItemName);

        saveButton = (Button) view.findViewById(R.id.saveBtnFragment);
        saveButton.setOnClickListener(this);
        getDialog().setTitle(Constants.TITLE);
        fetEditItem.requestFocus();
        editDueDateTextView.setText(editDueDate);
        editDueDateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getFragmentManager(), "DatePicker");
            }
        });

        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    /*
    Method called on clicking the save button the dialogue box
    This method eventually calls the onFinishedEdit dialogue method of the parent activity
     */
    @Override
    public void onClick(View view) {
        EditNameDialogListener listener = (EditNameDialogListener) getActivity();
        listener.onFinishEditDialog(fetEditItem.getText().toString(),position,dueDateString);
        dismiss();
    }


    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            
            updateDisplay(year,month,day);
        }

        public static void updateDisplay(int year,int month, int day) {

            GregorianCalendar cal = new GregorianCalendar(year, month, day);
            cal.set(year,month,day);

            dueDate = new SimpleDateFormat("dd MMMM yyyy");

            dueDateString = dueDate.format(cal.getTime());

            Log.d(TAG,"Due Date: "+ dueDate);


        }// updateDisplay
    }




}
