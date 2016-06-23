package com.example.michael.discgolfapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by Michael on 6/22/2016.
 */
public class MainMenuActivity extends Activity {

    Button btnNewGame;
    Button btnEditCourses;
    Button btnEditPlayers;
    Button btnScorecards;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);

        context = this;
        btnNewGame = (Button) findViewById(R.id.btnNewGame);
        btnEditCourses = (Button) findViewById(R.id.btnCourses);
        btnEditPlayers = (Button) findViewById(R.id.btnPlayers);
        btnScorecards = (Button) findViewById(R.id.btnScorecards);
    }

    public void OnNewGameClick(View v){
        Intent intent = new Intent(this,CoursePickerActivity.class);
        startActivity(intent);
    }

    public void OnEditCoursesClick(View v){
        Intent intent = new Intent(context,CourseEditorMenuActivity.class);
        startActivity(intent);
    }

    public void OnEditPlayersClick(View v){
        Toast toast = Toast.makeText(context,"You've clicked Edit Players, still not Implemented...",Toast.LENGTH_LONG);
        toast.show();
    }
    public void OnScorecardsClick(View v){
        Toast toast = Toast.makeText(context,"You've clicked View Scorecards, still not implemented",Toast.LENGTH_LONG);
        toast.show();
    }
}
