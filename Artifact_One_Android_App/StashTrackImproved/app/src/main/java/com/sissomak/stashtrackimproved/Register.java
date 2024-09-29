package com.sissomak.sqldemo;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Register Class
 * Functionality:
 *      - Handles registration functions for new users
 *      - Populates new user data in Users database
 *
 * @author Aaron Sissom
 * @course CS-499 Computer Science Capstone
 * @school Southern New Hampshire University
 */

public class Register extends Activity
{
    EditText editTextUserName, editTextPhone, editTextEmail, editTextPassword;
    Button btn_cancelButton, btn_signupButton;
    UserDatabaseHelper userDatabaseHelper;
    SQLiteDatabase db;
    Boolean emptyField;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        editTextUserName = findViewById(R.id.editTextUserName);
        editTextPhone = findViewById(R.id.editTextPhone);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        btn_cancelButton = findViewById(R.id.btn_cancelButton);
        btn_signupButton = findViewById(R.id.btn_signupButton);

        userDatabaseHelper = new UserDatabaseHelper(this);


        /*
         Set on click listeners
         */
        btn_cancelButton.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
        });

        btn_signupButton.setOnClickListener(v -> {
            checkEditTextNotEmpty();
            String name = editTextUserName.getText().toString();
            String password = editTextPassword.getText().toString();
            String phone = editTextPhone.getText().toString();
            String email = editTextEmail.getText().toString();

            // Creates new user object and inserts data from registration screen
            UserModel user = new UserModel(name, password, phone, email);
            boolean insertUser = userDatabaseHelper.addUser(user);

            if (!insertUser) // Unsuccessful
            {
                Toast.makeText(Register.this, "Registration failed", Toast.LENGTH_SHORT).show();
            }
            else // Successful
            {
                Toast.makeText(Register.this, "Registration successful!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
            }
        });
    }

    // Checks for empty fields in signup page
    private String checkEditTextNotEmpty()
    {
        String message = "";
        String name = editTextUserName.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String phone = editTextPhone.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();


        if (name.isEmpty())
        {
            editTextUserName.requestFocus();
            emptyField = true;
            message = "Username field is empty";
        }
        else if (phone.isEmpty())
        {
            editTextPhone.requestFocus();
            emptyField = true;
            message = "Phone number field is empty";
        }
        else if (email.isEmpty())
        {
            editTextEmail.requestFocus();
            emptyField = true;
            message = "Email field is empty";
        }
        else if (password.isEmpty())
        {
            editTextPassword.requestFocus();
            emptyField = true;
            message = "Password field is empty";
        }
        else
        {
            emptyField = false;
        }
        return message;
    }
}
