package photobom.com.todolist.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import photobom.com.todolist.R;
import photobom.com.todolist.helpers.Constants;


/**
 * Created by snarielwala on 12/16/15.
 */
public class AddDueDateFragment extends DialogFragment implements View.OnClickListener {

    private Button addItemButton;
    private static Button selectDueDateButton;
    private String addItemName;

    private static SimpleDateFormat dueDate;
    private static String dueDateString;

    public AddDueDateFragment() {
    }

    /*
    Read method reads all the items from the database on startup
     */
    public interface AddDueDateDialogListener {
        void onFinishAddDueDateDialog(String itemName,String dueDate);
    }

    /*
    Additional constructor for the editItemFragment that accepts a string and an int
     */
    public static AddDueDateFragment newInstance(String itemName) {
        AddDueDateFragment frag = new AddDueDateFragment();
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_due_date, container);
    }

    /*
    sets up the view for the dialogue fragment
     */
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        addItemName = getArguments().getString(Constants.ADD_ITEM_NAME);

        addItemButton = (Button) view.findViewById(R.id.addItem);
        addItemButton.setOnClickListener(this);

        selectDueDateButton = (Button) view.findViewById(R.id.selectDueDate);
        selectDueDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getFragmentManager(), "DatePicker");
            }
        });

        getDialog().setTitle(Constants.ADD_DUE_DATE_TITLE);
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    /*
    Method called on clicking the save button the dialogue box
    This method eventually calls the onFinishedEdit dialogue method of the parent activity
     */
    @Override
    public void onClick(View view) {
        AddDueDateDialogListener listener = (AddDueDateDialogListener) getActivity();
        listener.onFinishAddDueDateDialog(addItemName, dueDateString);
        dismiss();
    }

    public static void updateDisplay(int year,int month, int day) {

        GregorianCalendar cal = new GregorianCalendar(year, month, day);
        cal.set(year,month,day);

        dueDate = new SimpleDateFormat("dd MMMM yyyy");

        dueDateString = dueDate.format(cal.getTime());
        selectDueDateButton.setText(dueDateString);

    }// updateDisplay



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
    }



}
