package com.example.finalprojectppb;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText emailEditText = findViewById(R.id.emailEditText);
        EditText passwordEditText = findViewById(R.id.passwordEditText);
        Button loginButton = findViewById(R.id.loginButton);
        Button googleLoginButton = findViewById(R.id.googleLoginButton);
        TextView registerText = findViewById(R.id.registerText);

        loginButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (email.equals("email.com") && password.equals("123")) {
                Toast.makeText(this, "Login sukses!", Toast.LENGTH_SHORT).show();

                // Pindah ke MainActivity
                Intent intent = new Intent(LoginActivity.this, ScanActivity.class);
                startActivity(intent);
                finish(); // Optional: agar tidak bisa kembali ke login saat tekan back
            } else {
                Toast.makeText(this, "Email atau password salah", Toast.LENGTH_SHORT).show();
            }
        });

        googleLoginButton.setOnClickListener(v -> {
            Toast.makeText(this, "Fitur Google Sign-In belum diaktifkan", Toast.LENGTH_SHORT).show();
            // TODO: Tambahkan integrasi Google Sign-In nanti
        });

        registerText.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }
}
