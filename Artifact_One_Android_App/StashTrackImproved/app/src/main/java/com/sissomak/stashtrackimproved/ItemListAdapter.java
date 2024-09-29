package com.sissomak.sqldemo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

public class ItemListAdapter extends ArrayAdapter<ItemModel> {

    private final Context mContext;
    private List<ItemModel> itemList;
    ItemDatabaseHelper itemDatabaseHelper;
    EditItem editItem;

    public ItemListAdapter(Context context, List<ItemModel> list) {
        super(context, 0, list);
        mContext = context;
        itemList = list;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        // Get the data item for this position
        ItemModel item = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_row_template, parent, false);
        }

        // Lookup view for data population
        ImageButton btnEditItem = convertView.findViewById(R.id.btn_editItem);
        TextView itemDescription = convertView.findViewById(R.id.itemDescription);
        TextView itemQuantity = convertView.findViewById(R.id.itemQuantity);
        TextView itemUnits = convertView.findViewById(R.id.itemUnits);
        ImageButton btnDeleteItem = convertView.findViewById(R.id.btn_deleteItem);

        // Populate the data into the template view using the data object
        assert item != null;
        itemDescription.setText(item.getDescription());
        itemQuantity.setText(String.valueOf(item.getQuantity()));
        itemUnits.setText(item.getUnits());

        // Handle edit and delete button actions
        btnEditItem.setOnClickListener(v -> {
            // Handle edit button click (navigate to edit screen or show dialog)
            editItem.editPopup(position);
        });

        btnDeleteItem.setOnClickListener(v -> {
            // Handle delete button click (delete the item from the database and update the list)
            itemDatabaseHelper.deleteOne(itemList.get(position));
            itemList = itemDatabaseHelper.getAll();
            notifyDataSetChanged();
        });

        // Return the completed view to render on screen
        return convertView;
    }
}

