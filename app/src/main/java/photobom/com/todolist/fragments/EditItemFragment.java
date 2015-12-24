package photobom.com.todolist.fragments;

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
import android.widget.EditText;

import photobom.com.todolist.R;
import photobom.com.todolist.helpers.Constants;


/**
 * Created by snarielwala on 12/16/15.
 */
public class EditItemFragment extends DialogFragment implements View.OnClickListener {
    private EditText fetEditItem;
    private int position;
    private Button saveButton;
    private String selectedItemName;

    public EditItemFragment() {
    }

    /*
    Read method reads all the items from the database on startup
     */
    public interface EditNameDialogListener {
        void onFinishEditDialog(String oldName, int position);
    }


    /*
    Additional constructor for the editItemFragment that accepts a string and an int
     */
    public static EditItemFragment newInstance(String selectedItemName, int position) {
        EditItemFragment frag = new EditItemFragment();
        Bundle args = new Bundle();
        args.putString(Constants.SELECTED_ITEM, selectedItemName);
        args.putInt(Constants.POSITION,position);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_name, container);
    }

    /*
    sets up the view for the dialogue fragment
     */
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fetEditItem = (EditText) view.findViewById(R.id.fetEditItem);

        selectedItemName = getArguments().getString(Constants.SELECTED_ITEM);
        position=getArguments().getInt(Constants.POSITION);
        fetEditItem.setText(selectedItemName);

        saveButton = (Button) view.findViewById(R.id.saveBtnFragment);
        saveButton.setOnClickListener(this);
        getDialog().setTitle(Constants.TITLE);
        fetEditItem.requestFocus();
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
        listener.onFinishEditDialog(fetEditItem.getText().toString(),position);
        dismiss();
    }



}
