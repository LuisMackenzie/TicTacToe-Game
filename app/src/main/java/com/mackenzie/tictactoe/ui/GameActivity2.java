package com.mackenzie.tictactoe.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.mackenzie.tictactoe.R;
import com.mackenzie.tictactoe.databinding.ActivityGame2Binding;

public class GameActivity2 extends AppCompatActivity {

    private ActivityGame2Binding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGame2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}