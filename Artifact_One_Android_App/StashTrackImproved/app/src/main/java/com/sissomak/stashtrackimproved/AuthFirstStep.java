package com.sissomak.sqldemo;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

/**
 * AuthFirstStep Class
 * Functionality:
 *      - Creates an authenticated user with database access using
 *          the Firebase API.
 *
 * @author Aaron Sissom
 * @course CS-499 Computer Science Capstone
 * @school Southern New Hampshire University
 */

public class AuthFirstStep extends AppCompatActivity
{
    EditText edit_email, edit_password, edit_confirmPassword;
    ProgressBar loadingBar;
    Button register_button;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auth_first_step);

        mAuth = FirebaseAuth.getInstance();

        edit_email = findViewById(R.id.edit_email);
        edit_password = findViewById(R.id.edit_password);
        edit_confirmPassword = findViewById(R.id.edit_confirmPassword);

        loadingBar = new ProgressBar(AuthFirstStep.this);
        register_button = findViewById(R.id.register_button);

        register_button.setOnClickListener(v -> CreateAuthorizedAccount());
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null)
        {
            Intent intent = new Intent(getApplicationContext(), ItemList.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
    }

    private void CreateAuthorizedAccount()
    {
        String email = edit_email.getText().toString();
        String password = edit_password.getText().toString();
        String confirmPassword = edit_confirmPassword.getText().toString();

        if (TextUtils.isEmpty(email))
        {
            Toast.makeText(this, "Enter your email", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "Enter your password", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(confirmPassword))
        {
            Toast.makeText(this, "Confirm your password", Toast.LENGTH_SHORT).show();
        }
        else if (!password.equals(confirmPassword))
        {
            Toast.makeText(this, "Passwords do not match!", Toast.LENGTH_SHORT).show();
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful())
                    {
                        Toast.makeText(this, "2FA registration successful", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        String message = Objects.requireNonNull(task.getException()).getMessage();
                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
