package com.mackenzie.tictactoe.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mackenzie.tictactoe.databinding.ActivityFindPlayerBinding;

public class FindPlayerActivity extends AppCompatActivity {

    private ActivityFindPlayerBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseUser user;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFindPlayerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initProgressBar();
        firebaseInit();
        events();

    }

    private void firebaseInit() {
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        user = mAuth.getCurrentUser();
        uid = mAuth.getUid();
    }

    private void events() {
        binding.buttonJugar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        binding.buttonRanking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void initProgressBar() {
        binding.progressBarJugadas.setIndeterminate(true);
        binding.textViewLoading.setText("Cargando...");
        changeVisibility(true);

    }

    private void changeVisibility(boolean showMenu) {
        binding.progressBarJugadas.setVisibility(showMenu ? View.GONE : View.VISIBLE);
        binding.menuJuego.setVisibility(showMenu ? View.VISIBLE : View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        changeVisibility(true);
    }
}