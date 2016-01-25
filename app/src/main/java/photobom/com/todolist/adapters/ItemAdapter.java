package photobom.com.todolist.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import photobom.com.todolist.R;
import photobom.com.todolist.models.Item;

/**
 * Created by snarielwala on 1/24/16.
 */
public class ItemAdapter extends ArrayAdapter<Item>{



    public ItemAdapter(Context context, List<Item> items){
        super(context,0,items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Item item = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.due_date_item, parent, false);
        }
        // Lookup view for data population
        TextView tvName = (TextView) convertView.findViewById(R.id.itemText);

        // Populate the data into the template view using the data object
        tvName.setText(item.getName());

        tvName.setTextColor(Color.BLACK);



        TextView dueDateTextView = (TextView) convertView.findViewById(R.id.itemDueDateTextView);
        if(item.getDueDate()!=null) {
            String date = item.getDueDate();
            dueDateTextView.setText(date);
        }

        // Return the completed view to render on screen
        return convertView;
    }

}
