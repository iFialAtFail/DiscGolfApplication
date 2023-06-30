package com.manleysoftware.michael.discgolfapp.ui.main;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.manleysoftware.michael.discgolfapp.databinding.ActivityMainNavBinding;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainNavActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMainNavBinding binding = ActivityMainNavBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

}