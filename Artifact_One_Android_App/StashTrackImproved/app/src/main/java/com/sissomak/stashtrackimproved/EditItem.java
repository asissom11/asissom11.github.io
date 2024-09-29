package com.sissomak.sqldemo;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * EditItems Class
 * Functionality:
 *      - Populates items into ItemList activity
 *      - Allows user to select an item to edit via popup window
 *
 * @author Aaron Sissom
 * @course CS-499 Computer Science Capstone
 * @school Southern New Hampshire University
 */

public class EditItem extends BaseAdapter
{
    private final Activity context;
    private PopupWindow window;
    ArrayList<ItemModel> items;
    ItemDatabaseHelper itemDatabaseHelper;

    public EditItem(Activity context, ArrayList<ItemModel> items, ItemDatabaseHelper itemDatabaseHelper)
    {
        this.context = context;
        this.items = items;
        this.itemDatabaseHelper = itemDatabaseHelper;
    }

    public static class ViewHolder
    {
        TextView itemId, userEmail, itemDescription, itemQuantity, itemUnits;
        ImageButton btn_editItem, btn_deleteItem;
    }

    /*
     Default functions in the BaseAdapter class
     */

    @Override
    public int getCount()
    {
        return items.size();
    }

    @Override
    public Object getItem(int position)
    {
        return position;
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View row = convertView;
        LayoutInflater inflater = context.getLayoutInflater();
        ViewHolder v;

        if (convertView == null)
        {
            v = new ViewHolder();
            row = inflater.inflate(R.layout.item_row_template, null, true);

            v.btn_editItem = row.findViewById(R.id.btn_editItem);
            v.itemId = row.findViewById(R.id.itemId);
            v.userEmail = row.findViewById(R.id.userEmail);
            v.itemDescription = row.findViewById(R.id.itemDescription);
            v.itemQuantity = row.findViewById(R.id.itemQuantity);
            v.itemUnits = row.findViewById(R.id.itemUnits);
            v.btn_deleteItem = row.findViewById(R.id.btn_deleteItem);

            row.setTag(v);
        }
        else
        {
            v = (ViewHolder)convertView.getTag();
        }

        v.itemId.setText("" + items.get(position).getId());
        v.itemDescription.setText(items.get(position).getDescription());
        v.itemQuantity.setText(items.get(position).getQuantity());
        v.itemUnits.setText(items.get(position).getUnits());

        // Check is the cell value is zero to change color and send SMS
        String value = v.itemQuantity.getText().toString().trim();
        if (value.equals("0")) {
            // Change background color and text color of item qty cell if value is zero
            v.itemQuantity.setBackgroundColor(Color.RED);
            v.itemQuantity.setTextColor(Color.WHITE);
            ItemList.SendSMSMessage(context.getApplicationContext());
        } else {
            // Change background color and text color of item qty cell to default
            v.itemQuantity.setBackgroundColor(Color.parseColor("#E6E6E6"));
            v.itemQuantity.setTextColor(Color.BLACK);
        }

        final int positionPopup = position;

        v.btn_editItem.setOnClickListener(view -> editPopup(positionPopup));

        v.btn_deleteItem.setOnClickListener(view -> {

            itemDatabaseHelper.deleteOne(items.get(positionPopup));

            items = (ArrayList<ItemModel>) itemDatabaseHelper.getAll();
            notifyDataSetChanged();

            Toast.makeText(context, "Item Deleted", Toast.LENGTH_SHORT).show();

            int itemsCount = itemDatabaseHelper.countItems();
            TextView totalItems = context.findViewById(R.id.totalItems);
            totalItems.setText(String.valueOf(itemsCount));
        });

        return  row;
    }

    public void editPopup(final int positionPopup) {
        LayoutInflater inflater = context.getLayoutInflater();
        View layout = inflater.inflate(R.layout.edit_item_window, context.findViewById(R.id.popup_element));

        window = new PopupWindow(layout, 800, 1000, true);
        window.showAtLocation(layout, Gravity.CENTER, 0, 0);

        final EditText editItemDesc = layout.findViewById(R.id.editTextItemDescriptionPopup);
        final EditText editItemQty = layout.findViewById(R.id.editTextItemQtyPopup);
        final EditText editItemUnit = layout.findViewById(R.id.editTextItemUnitPopup);

        editItemDesc.setText(items.get(positionPopup).getDescription());
        editItemQty.setText(items.get(positionPopup).getQuantity());
        editItemUnit.setText(items.get(positionPopup).getUnits());

        Button save = layout.findViewById(R.id.editSaveButton);
        Button cancel = layout.findViewById(R.id.editCancelButton);

        save.setOnClickListener(view -> {
            String itemDesc = editItemDesc.getText().toString();
            String itemQty = editItemQty.getText().toString();
            String itemUnit = editItemUnit.getText().toString();

            ItemModel item = items.get(positionPopup);
            item.setDescription(itemDesc);
            item.setQuantity(itemQty);
            item.setUnits(itemUnit);

            itemDatabaseHelper.updateItem(item);
            items = (ArrayList<ItemModel>) itemDatabaseHelper.getAll();
            notifyDataSetChanged();

            Toast.makeText(context, "Item Updated", Toast.LENGTH_SHORT).show();

            window.dismiss();
        });

        cancel.setOnClickListener(view -> {
            Toast.makeText(context, "Action Canceled", Toast.LENGTH_SHORT).show();
            window.dismiss();
        });
    }
}
