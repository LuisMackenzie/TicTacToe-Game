package com.mackenzie.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.mackenzie.tictactoe.databinding.ActivityRegistroBinding;

public class RegistroActivity extends AppCompatActivity {

    private ActivityRegistroBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegistroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.progressBarRegistro.setVisibility(View.GONE);
        eventos();
    }

    private void eventos() {
        binding.btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = binding.etNameReg.getText().toString();
                String email = binding.etEmailReg.getText().toString();
                String pass = binding.etPassReg.getText().toString();

                if (name.isEmpty() || name.length() < 3) {
                    binding.etNameReg.setError("El nombre es requerido");
                } else if (email.isEmpty() || email.length() < 8) {
                    binding.etEmailReg.setError("El Email es requerido");
                } else if (pass.isEmpty() || pass.length() < 4) {
                    binding.etPassReg.setError("Se requiere contraseña de al menos 4 digitos");
                } else {
                    // TODO realizar registro en firebase auth

                }

            }
        });
    }
}