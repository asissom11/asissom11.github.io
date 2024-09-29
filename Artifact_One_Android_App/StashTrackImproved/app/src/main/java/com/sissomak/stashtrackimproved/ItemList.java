package com.sissomak.sqldemo;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * ItemList Class
 * Functionality:
 *      - Enables interface items (buttons, SMS notifiations, and alert dialogs)
 *      - Populates total number of items in list
 *
 * @author Aaron Sissom
 * @course CS-499 CS Capstone
 * @school Southern New Hampshire University
 */

public class ItemList extends AppCompatActivity
{
    // refs to buttons and controls on layout
    ImageButton btn_add, btn_deleteAll, btn_smsAlerts;
    TextView totalItems, currentUserLabel;
    ListView lv_itemList;
    ItemListAdapter itemArrayAdapter;
    ArrayList<ItemModel> items;
    ItemDatabaseHelper itemDatabaseHelper;
    AlertDialog alertDialog;
    EditItem editItem;
    int itemCount;
    static String phoneNumber;

    public static final String userEmail = "";
    private static boolean deleteItems = false;
    private static final int SMS_PERMISSION_CODE = 0;
    private static boolean messagesAuthorized = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_list);

        // Initialize activity items
        btn_add = findViewById(R.id.btn_add);
        btn_deleteAll = findViewById(R.id.btn_deleteAll);
        btn_smsAlerts = findViewById(R.id.btn_smsAlerts);
        totalItems = findViewById(R.id.totalItems);
        currentUserLabel = findViewById(R.id.currentUserLabel);
        lv_itemList = findViewById(R.id.lv_itemList);

        // Create a new instance of item database helper and show items in the list
        itemDatabaseHelper = new ItemDatabaseHelper(ItemList.this);

        showItemsOnListView(itemDatabaseHelper);

        /*
         Set click listeners
         */
        btn_add.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), AddItem.class);
            startActivity(intent);
        });

        btn_deleteAll.setOnClickListener(v -> {
            itemCount = itemDatabaseHelper.countItems();

            if (itemCount > 0)
            {
                alertDialog = DeleteItemsAlert.doubleButton(ItemList.this);
                alertDialog.show();
                alertDialog.setCancelable(true);
                alertDialog.setOnCancelListener(dialog -> DeleteAll());
            }
            else
            {
                Toast.makeText(ItemList.this, "All items deleted - the database is empty", Toast.LENGTH_LONG).show();
            }
        });

        btn_smsAlerts.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(ItemList.this, Manifest.permission.SEND_SMS)
                    != PackageManager.PERMISSION_GRANTED)
            {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.SEND_SMS))
                {
                    Toast.makeText(ItemList.this, "Permission needed for text alerts", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    ActivityCompat.requestPermissions(ItemList.this, new String[]{Manifest.permission.SEND_SMS}, SMS_PERMISSION_CODE);
                }
            }
            else
            {
                Toast.makeText(ItemList.this, "Text alerts are permitted", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showItemsOnListView(ItemDatabaseHelper itemDatabaseHelper)
    {
        List<ItemModel> items = itemDatabaseHelper.getAll();

        itemArrayAdapter = new ItemListAdapter(ItemList.this, items);
        lv_itemList.setAdapter(itemArrayAdapter);
    }

    // Setting signout menu item in AppBar
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.appbar_items_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId() == R.id.btn_signOut)
        {
            // End ItemList on menu item click.
            itemDatabaseHelper.close();
            super.finish();
            Toast.makeText(this,"Signout successful", Toast.LENGTH_LONG).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1)
        {
            if (resultCode == RESULT_OK)
            {
                itemCount = itemDatabaseHelper.countItems();
                totalItems.setText(String.valueOf(itemCount));

                if(editItem == null)
                {
                    editItem = new EditItem(this, items, itemDatabaseHelper);
                    lv_itemList.setAdapter(editItem);
                }

                editItem.items = (ArrayList<ItemModel>) itemDatabaseHelper.getAll();
                ((BaseAdapter)lv_itemList.getAdapter()).notifyDataSetChanged();
            }
            else
            {
                Toast.makeText(this, "Action Canceled", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static void AffirmativeDelete()
    {
        deleteItems = true;
    }

    public static void NegativeDelete()
    {
        deleteItems = false;
    }

    public void DeleteAll()
    {
        if (deleteItems)
        {
            itemDatabaseHelper.deleteAllItems();
            Toast.makeText(ItemList.this, "All items deleted - the database is empty", Toast.LENGTH_SHORT).show();
        }
    }

    /*
     Allow and Block message scripts for SMS inventory alerts
     to be used with AlertDialog API
     */

    public static void AllowAlerts()
    {
        messagesAuthorized = true;
    }

    public static void  BlockAlerts()
    {
        messagesAuthorized = false;
    }

    public static void SendSMSMessage(Context context)
    {
        String phone = phoneNumber;
        String message = "You have one or more items with a zero value in your inventory.";

        if (messagesAuthorized)
        {
            try
            {
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(phone, null, message, null, null);
                Toast.makeText(context, "SMS Message Sent", Toast.LENGTH_SHORT).show();
            }
            catch (Exception e)
            {
                Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
        else
        {
            Toast.makeText(context, "Enable device permissions to send SMS alerts", Toast.LENGTH_LONG).show();
        }
    }
}
