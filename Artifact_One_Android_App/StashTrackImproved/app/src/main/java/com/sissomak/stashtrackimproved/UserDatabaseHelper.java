package com.sissomak.sqldemo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.Arrays;

/**
 * UserDatabaseHelper Class
 * Functionality:
 *      - Provides SQLite functions for Users database
 *      - Manages CRUD operations for Users database
 *
 * @author Aaron Sissom
 * @course CS-499 Computer Science Capstone
 * @school Southern New Hampshire University
 */

public class UserDatabaseHelper extends SQLiteOpenHelper
{
    public static final String USER_TABLE = "USER_TABLE";
    public static final String COLUMN_USER_NAME = "USER_NAME";
    public static final String COLUMN_USER_PASSWORD = "USER_PASSWORD";
    public static final String COLUMN_USER_PHONE = "USER_PHONE";
    public static final String COLUMN_USER_EMAIL = "USER_EMAIL";
    public static final String COLUMN_ID = "ID";

    public UserDatabaseHelper(@Nullable Context context) {
        super(context, "users.db", null, 1);
    }

    // called first time database is accessed. contains code to create new database
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String createUserTableStatement = "CREATE TABLE " + USER_TABLE +
                " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_USER_NAME + " TEXT, " + COLUMN_USER_PASSWORD + " TEXT, " +
                        COLUMN_USER_PHONE + " TEXT, " + COLUMN_USER_EMAIL + " TEXT" +")";

        db.execSQL(createUserTableStatement);
    }

    // called when DB version updates preventing previous users apps from breaking when you update the database design
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {

    }

    public boolean addUser(UserModel aUser)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_USER_NAME, aUser.getName());
        cv.put(COLUMN_USER_PHONE, aUser.getPhone());
        cv.put(COLUMN_USER_PASSWORD, aUser.getPassword());
        cv.put(COLUMN_USER_EMAIL, aUser.getEmail());

        long insert = db.insert(USER_TABLE, null, cv);

        if (insert == -1)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    public void deleteOne(UserModel aUser)
    {
        //find customer model in db, then delete it and return true
        //if not found return false

        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + USER_TABLE + " WHERE " + COLUMN_ID + " = " + aUser.getId();

        Cursor cursor = db.rawQuery(query, null);

        cursor.moveToFirst();

    }

    public boolean checkUserEmail(String email)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + USER_TABLE + " WHERE " + COLUMN_USER_EMAIL + " = ?" + Arrays.toString(new String[]{email});

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.getCount() > 0)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public boolean checkUserPassword(String email, String password)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + USER_TABLE + " WHERE " + COLUMN_USER_EMAIL + " = ?" + " AND " + COLUMN_USER_PASSWORD + " = ?" + Arrays.toString(new String[]{email, password});

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.getCount() > 0)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
}
