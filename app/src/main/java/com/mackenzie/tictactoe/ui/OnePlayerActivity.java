package com.mackenzie.tictactoe.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.mackenzie.tictactoe.R;

public class OnePlayerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_player);
    }
}