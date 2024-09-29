package com.sissomak.sqldemo;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

/**
 * Login Class
 * Functionality:
 *      - Startup screen wen app initializes
 *      - Enables login, signup, and forgot password functions
 *
 * @author Aaron Sissom
 * @course CS-499 Computer Science Capstone
 * @school Southern New Hampshire University
 */

public class Login extends AppCompatActivity
{
    EditText username, password;
    Button loginButton, forgotPasswordButton, signUpButton, btn_multifactor_auth;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginButton);
        signUpButton = findViewById(R.id.signupButton);
        forgotPasswordButton = findViewById(R.id.forgotPasswordButton);
        btn_multifactor_auth = findViewById(R.id.btn_multifactor_auth);

        /*
          Set on click listeners
         */
        loginButton.setOnClickListener(v -> {
            if (username.getText().toString().equals("user") && password.getText().toString().equals("password"))
            {
                Toast.makeText(Login.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), ItemList.class);
                startActivity(intent);
            }
            else
            {
                Toast.makeText(Login.this, "Login Failed!", Toast.LENGTH_SHORT).show();
            }
        });

        // Takes user to password recovery activity
        forgotPasswordButton.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), ForgotPassword.class);
            startActivity(intent);
        });

        // Takes user to registration screen
        signUpButton.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), Register.class);
            startActivity(intent);
        });

        btn_multifactor_auth.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), AuthFirstStep.class);
            startActivity(intent);
        });

    }

}
