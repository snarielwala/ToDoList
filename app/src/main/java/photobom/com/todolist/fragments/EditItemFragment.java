package photobom.com.todolist.fragments;

import android.content.Intent;
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

import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

/**
 * Created by snarielwala on 12/16/15.
 */
public class EditItemFragment extends DialogFragment implements View.OnClickListener {
    private EditText fetEditItem;
    private int position;
    private Button saveButton;

    public EditItemFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public interface EditNameDialogListener {
        void onFinishEditDialog(String oldName, int position);
    }


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

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get field from view
        fetEditItem = (EditText) view.findViewById(R.id.fetEditItem);
        String selectedItemName = getArguments().getString(Constants.SELECTED_ITEM);
        position=getArguments().getInt(Constants.POSITION);
        fetEditItem.setText(selectedItemName);
        saveButton = (Button) view.findViewById(R.id.saveBtnFragment);
        saveButton.setOnClickListener(this);
        // Fetch arguments from bundle and set title
        getDialog().setTitle(Constants.TITLE);
        // Show soft keyboard automatically and request focus to field
        fetEditItem.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    @Override
    public void onClick(View view) {
        // Return input text to activity
        EditNameDialogListener listener = (EditNameDialogListener) getActivity();
        listener.onFinishEditDialog(fetEditItem.getText().toString(),position);
        dismiss();
    }



}
