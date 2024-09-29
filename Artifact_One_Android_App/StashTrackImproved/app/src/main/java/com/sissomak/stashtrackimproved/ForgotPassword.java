package com.sissomak.sqldemo;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

/**
 * ForgotPassword Class
 * Functionality:
 *      - Retrieves user password based on entered email
 *
 * @author Aaron Sissom
 * @course CS-499 CS Capstone
 * @school Southern New Hampshire University
 */

public class ForgotPassword extends AppCompatActivity
{
    Button btnResetPass, btnBack;
    EditText editEmail;
    ProgressBar progressBar;
    String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password);

        btnBack = findViewById(R.id.btn_ForgotPasswordBack);
        btnResetPass = findViewById(R.id.btn_ResetPassword);
        editEmail = findViewById(R.id.edtForgotPasswordEmail);
        progressBar = findViewById(R.id.forgetPasswordProgressbar);

        /*
         Set on click listeners
         */

        //Reset Button Listener
        btnResetPass.setOnClickListener(v -> {
            userEmail = editEmail.getText().toString().trim();

            if (!TextUtils.isEmpty(userEmail))
            {
                ResetPassword();
            }
            else
            {
                editEmail.setError("Email field can't be empty");
            }
        });


        // Takes user back to login on canceling operation
        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
        });

    }

    private void ResetPassword()
    {
        progressBar.setVisibility(View.VISIBLE);
        btnResetPass.setVisibility(View.INVISIBLE);
    }
}