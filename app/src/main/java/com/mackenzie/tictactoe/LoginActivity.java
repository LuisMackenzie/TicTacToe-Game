package com.mackenzie.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mackenzie.tictactoe.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        changeVisibility(true);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        eventos();
    }

    private void eventos() {
        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = binding.etEmail.getText().toString();
                String pass = binding.etPass.getText().toString();

                if (email.isEmpty() || email.length() < 8) {
                    binding.etEmail.setError("El Email es requerido");
                } else if (pass.isEmpty() || pass.length() < 4) {
                    binding.etPass.setError("Se requiere contraseña de al menos 4 digitos");
                } else {
                    // TODO realizar LOGIN en firebase auth

                    changeVisibility(false);
                }

            }
        });
        binding.btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(LoginActivity.this, RegistroActivity.class);
                startActivity(in);
                // finish();
            }
        });
    }

    private void changeVisibility(boolean showForm) {
        binding.progressBarLogin.setVisibility(showForm ? View.GONE : View.VISIBLE);
        binding.formLogin.setVisibility(showForm ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            // reload();
        }
    }

}