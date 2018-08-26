package com.manleysoftware.michael.discgolfapp.Activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.manleysoftware.michael.discgolfapp.Fragments.CoursePickerFragment;
import com.manleysoftware.michael.discgolfapp.R;

import java.util.zip.Inflater;

public class NewGameActivity extends FragmentActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_game_wizard_layout);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        CoursePickerFragment fragment = new CoursePickerFragment();
        fragmentTransaction.add(R.id.newGameWizardContainter, fragment);
        fragmentTransaction.commit();
    }
}
