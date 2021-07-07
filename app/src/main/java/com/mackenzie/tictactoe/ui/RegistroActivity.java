package com.mackenzie.tictactoe.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mackenzie.tictactoe.FindPlayerActivity;
import com.mackenzie.tictactoe.databinding.ActivityRegistroBinding;

public class RegistroActivity extends AppCompatActivity {

    private ActivityRegistroBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private String name, email, pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegistroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.progressBarRegistro.setVisibility(View.GONE);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        // Mostrar algo aqui
        changeVisibility(true);
        eventos();
    }

    private void eventos() {
        binding.btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = binding.etNameReg.getText().toString();
                email = binding.etEmailReg.getText().toString();
                pass = binding.etPassReg.getText().toString();

                if (name.isEmpty() || name.length() < 3) {
                    binding.etNameReg.setError("El nombre es requerido");
                } else if (email.isEmpty() || email.length() < 8) {
                    binding.etEmailReg.setError("El Email es requerido");
                } else if (pass.isEmpty() || pass.length() < 4) {
                    binding.etPassReg.setError("Se requiere contraseña de al menos 4 digitos");
                } else {
                    createUser();
                }

            }
        });
    }

    private void createUser() {

        changeVisibility(false);
        mAuth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull  Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = mAuth.getCurrentUser();
                    updateUI(user);
                } else {
                    changeVisibility(true);
                    Toast.makeText(RegistroActivity.this, "Error en el registro", Toast.LENGTH_SHORT).show();
                    updateUI(null);
                }
            }
        });
    }

    private void changeVisibility(boolean showForm) {
        binding.progressBarRegistro.setVisibility(showForm ? View.GONE : View.VISIBLE);
        binding.formRegistro.setVisibility(showForm ? View.VISIBLE : View.GONE);
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            // almacenar la informacion del usuario en firestore

            // y navegar hacia la siguentye pantalla
            Intent in = new Intent(RegistroActivity.this, FindPlayerActivity.class);
            startActivity(in);
        } else {
            binding.etPassReg.setError("Email o contraseña incorrectos");
            binding.etPassReg.requestFocus();
        }
    }
}