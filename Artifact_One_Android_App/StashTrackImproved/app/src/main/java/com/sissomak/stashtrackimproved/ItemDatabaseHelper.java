package com.sissomak.sqldemo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * ItemDatabaseHelper Class
 * Functionality:
 *      - Manages CRUD operations to facilitate item manipulation by the user
 *
 * @author Aaron Sissom
 * @course CS-499 Computer Science Capstone
 * @school Southern New Hampshire University
 */

public class ItemDatabaseHelper extends SQLiteOpenHelper
{
    public static final String ITEM_TABLE = "ITEM_TABLE";
    public static final String COLUMN_ITEM_NAME = "ITEM_NAME";
    public static final String COLUMN_ITEM_DESCRIPTION = "ITEM_DESCRIPTION";
    public static final String COLUMN_ITEM_QUANTITY = "ITEM_QUANTITY";
    public static final String COLUMN_ITEM_UNITS = "ITEM_UNITS";
    public static final String COLUMN_ID = "ID";

    public ItemDatabaseHelper(@Nullable Context context) {
        super(context, "items.db", null, 3);
    }

    // called first time database is accessed. contains code to create new database
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String createTableStatement = "CREATE TABLE " + ITEM_TABLE +
                " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_ITEM_NAME + " TEXT, " +
                        COLUMN_ITEM_DESCRIPTION + " TEXT, " +
                        COLUMN_ITEM_QUANTITY + " TEXT, " +
                        COLUMN_ITEM_UNITS + " TEXT" + " )";

        db.execSQL(createTableStatement);
    }

    // called when DB version updates preventing previous users' apps from breaking when you update the database design
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        if (oldVersion < 3)
        {
            String alterQuery = "ALTER TABLE " + ITEM_TABLE + " ADD COLUMN " + COLUMN_ITEM_UNITS + " TEXT DEFAULT 'Unknown'";
            db.execSQL(alterQuery);
        }
    }


    /*
    ** Database CRUD operations
     */
    public boolean addOne(ItemModel item)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        
        cv.put(COLUMN_ITEM_NAME, item.getName());
        cv.put(COLUMN_ITEM_DESCRIPTION, item.getDescription());
        cv.put(COLUMN_ITEM_QUANTITY, item.getQuantity());
        cv.put(COLUMN_ITEM_UNITS, item.getUnits());

        long insert = db.insert(ITEM_TABLE, null, cv);
        return insert != -1;
    }

    public List<ItemModel> getAll()
    {
        List<ItemModel> returnList = new ArrayList<>();
        String query = "SELECT * FROM " + ITEM_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst())
        { // iterate through cursor (results) and create new customer objects. put new objects into the return list
            do {
                int itemID = cursor.getInt(0);
                String itemName = cursor.getString(1);
                String itemDescription = cursor.getString(2);
                String itemQuantity = cursor.getString(3);
                String itemUnits = cursor.isNull(4) ? "Unknown" : cursor.getString(4);

                ItemModel newItem = new ItemModel(itemID, itemName, itemDescription, itemQuantity, itemUnits);
                returnList.add(newItem);

            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return  returnList;
    }

    public int countItems()
    {
        String query = "SELECT * FROM " + ITEM_TABLE;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        int totalItems = cursor.getCount();
        cursor.close();

        return totalItems;
    }

    public int updateItem(ItemModel item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_ITEM_DESCRIPTION, item.getDescription());
        cv.put(COLUMN_ITEM_QUANTITY, item.getQuantity());
        cv.put(COLUMN_ITEM_UNITS, item.getUnits());

        return db.update(ITEM_TABLE, cv, COLUMN_ID + " = ?", new String[] {String.valueOf(item.getId())});
    }

    public void deleteOne(ItemModel item)
    {
        //find customer model in db, then delete it and return true
        //if not found return false

        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + ITEM_TABLE + " WHERE " + COLUMN_ID + " = " + item.getId();

        Cursor cursor = db.rawQuery(query, null);

        cursor.moveToFirst();
        cursor.close();
    }

    public void deleteAllItems()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(ITEM_TABLE, null, null);
        db.close();
    }
}
