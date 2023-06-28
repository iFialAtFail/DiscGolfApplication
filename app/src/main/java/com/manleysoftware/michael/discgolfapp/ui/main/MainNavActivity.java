package com.manleysoftware.michael.discgolfapp.ui.main;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.manleysoftware.michael.discgolfapp.R;
import com.manleysoftware.michael.discgolfapp.databinding.ActivityMainNavBinding;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainNavActivity extends AppCompatActivity {

    private ActivityMainNavBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainNavBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main_nav);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }

}