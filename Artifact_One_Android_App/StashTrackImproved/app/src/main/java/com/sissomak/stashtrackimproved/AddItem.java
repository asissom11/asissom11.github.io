package com.sissomak.sqldemo;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.atomic.AtomicReference;

/**
 * AddItem Class
 * Functionality:
 *      - Enables user to add an item to the list
 *      - Called from ItemList activity
 *
 * @author Aaron Sissom
 * @course CS-499 Computer Science Capstone
 * @school Southern New Hampshire University
 */

public class AddItem extends AppCompatActivity {

    String emailString, descriptionString, quantityString, unitsString;
    TextView email;
    ImageButton qtyUp, qtyDown;
    EditText itemDescription, itemQuantity, itemUnits;
    Button btnCancel, btnAddItem;
    Boolean emptyField;
    ItemDatabaseHelper itemDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_item);

        // Initiate buttons, textViews, and editText variables
        email = findViewById(R.id.textViewCurrentUser);
        itemDescription = findViewById(R.id.editTextItemDescription);
        itemUnits = findViewById(R.id.editTextItemUnit);
        qtyUp = findViewById(R.id.itemQtyIncrease);
        qtyDown = findViewById(R.id.itemQtyDecrease);
        itemQuantity = findViewById(R.id.editTextItemQuantity);
        btnCancel = findViewById(R.id.addCancelButton);
        btnAddItem = findViewById(R.id.addItemButton);

        itemDatabaseHelper = new ItemDatabaseHelper(AddItem.this);

        AtomicReference<Intent> intent = new AtomicReference<>(getIntent());

        // Setting current user email to display
        email.setText(getString(R.string.current_user, emailString));

        // Adding click listener to increase Button
        qtyUp.setOnClickListener(view -> {
            int input = 0, total;

            String value = itemQuantity.getText().toString().trim();

            if (!value.isEmpty()) {
                input = Integer.parseInt(value);
            }

            total = input + 1;
            itemQuantity.setText(String.valueOf(total));
        });

        // Adding click listener to decrease Button
        qtyDown.setOnClickListener(view -> {
            int input, total;

            String qty = itemQuantity.getText().toString().trim();

            if (qty.equals("0")) {
                Toast.makeText(this, "Item Quantity is Zero", Toast.LENGTH_LONG).show();
            } else {
                input = Integer.parseInt(qty);
                total = input - 1;
                itemQuantity.setText(String.valueOf(total));
            }
        });

        // Adding click listener to cancel Button
        btnCancel.setOnClickListener(view -> {
            // Goes back to ItemList after cancel adding item
            Intent add = new Intent();
            setResult(0, add);
            this.finish();
        });

        // Adding click listener to addItem Button
        btnAddItem.setOnClickListener(view -> InsertItemIntoDatabase());
    }

    // Insert item data into database and send data to ItemList
    public void InsertItemIntoDatabase() {
        String message = CheckEditTextNotEmpty();

        if (!emptyField) {
            String description = descriptionString;
            String quantity = quantityString;
            String unit = unitsString;

            ItemModel item = new ItemModel(description, quantity, unit);
            itemDatabaseHelper.addOne(item);

            // Display toast message after insert in table
            Toast.makeText(this,"Item Added Successfully", Toast.LENGTH_LONG).show();

            // Close AddItem
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            this.finish();
        } else {
            // Display message if item description is empty and focus the field
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        }
    }

    // Checking item description is not empty
    public String CheckEditTextNotEmpty() {
        String message = "";
        descriptionString = itemDescription.getText().toString().trim();
        unitsString = itemUnits.getText().toString().trim();
        quantityString = itemQuantity.getText().toString().trim();

        if (descriptionString.isEmpty()) {
            itemDescription.requestFocus();
            emptyField = true;
            message = "Item Description is Empty";
        } else if (unitsString.isEmpty()){
            itemUnits.requestFocus();
            emptyField = true;
            message = "Item Unit is Empty";
        } else {
            emptyField = false;
        }
        return message;
    }

}
