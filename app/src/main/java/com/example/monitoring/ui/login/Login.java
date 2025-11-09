package com.example.monitoring.ui.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.monitoring.MainActivity;
import com.example.monitoring.R;
import com.example.monitoring.api.RegisterAPI;
import com.example.monitoring.api.RetrofitClientInstance;
import com.example.monitoring.model.LoginResponse;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity {

    private TextInputEditText etUsername, etPassword;
    private MaterialButton btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin   = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(v -> doLogin());
    }

    private void doLogin() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Username dan password wajib diisi", Toast.LENGTH_SHORT).show();
            return;
        }

        RegisterAPI apiService = RetrofitClientInstance.getRetrofitInstance().create(RegisterAPI.class);
        Call<LoginResponse> call = apiService.login(username, password);

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse loginResponse = response.body();

                    if (loginResponse.isSuccess()) {
                        Toast.makeText(Login.this, "Login berhasil", Toast.LENGTH_SHORT).show();

                        // Pindah ke halaman dashboard (MainActivity)
                        Intent intent = new Intent(Login.this, MainActivity.class);
                        startActivity(intent);

                        // Pastikan activity dihancurkan setelah login berhasil
                        finishAffinity(); // Menutup semua activity dalam task
                        // atau bisa juga menggunakan:
                        // finish(); // Hanya menutup activity saat ini

                    } else {
                        Toast.makeText(Login.this, loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(Login.this, "Login gagal! Periksa koneksi atau server.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(Login.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Tambahkan log atau cleanup code di sini jika diperlukan
        System.out.println("Login Activity Destroyed");
    }
}