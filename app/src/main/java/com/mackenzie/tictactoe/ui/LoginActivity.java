package com.mackenzie.tictactoe.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mackenzie.tictactoe.FindPlayerActivity;
import com.mackenzie.tictactoe.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private FirebaseAuth mAuth;
    private String email, pass;

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
                email = binding.etEmail.getText().toString();
                pass = binding.etPass.getText().toString();

                if (email.isEmpty() || email.length() < 8) {
                    binding.etEmail.setError("El Email es requerido");
                } else if (pass.isEmpty() || pass.length() < 4) {
                    binding.etPass.setError("Se requiere contraseña de al menos 4 digitos");
                } else {
                    // TODO realizar LOGIN en firebase auth

                    loginUser();
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

    private void loginUser() {
        mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = mAuth.getCurrentUser();
                    updateUI(user);
                } else {
                    Log.w("TAG", "Signin ERROR" + task.getException());
                    Toast.makeText(LoginActivity.this, "Algo a salido mal", Toast.LENGTH_SHORT).show();
                    updateUI(null);
                }
            }
        });
    }

    private void changeVisibility(boolean showForm) {
        binding.progressBarLogin.setVisibility(showForm ? View.GONE : View.VISIBLE);
        binding.formLogin.setVisibility(showForm ? View.VISIBLE : View.GONE);
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            // almacenar la informacion del usuario en firestore
            // TODO comprobar si existe el usuario

            // y navegar hacia la siguentye pantalla
            Intent in = new Intent(LoginActivity.this, FindPlayerActivity.class);
            startActivity(in);
        } else {
            binding.etPass.setError("Email o contraseña incorrectos");
            binding.etPass.requestFocus();
        }
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