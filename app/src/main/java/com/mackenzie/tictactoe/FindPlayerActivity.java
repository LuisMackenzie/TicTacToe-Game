package com.mackenzie.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.mackenzie.tictactoe.databinding.ActivityFindPlayerBinding;

public class FindPlayerActivity extends AppCompatActivity {

    private ActivityFindPlayerBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFindPlayerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}