package com.example.michael.discgolfapp.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.michael.discgolfapp.Adapters.MainMenuDataAdapter;
import com.example.michael.discgolfapp.R;

/**
 * Created by Michael on 6/22/2016.
 */
public class MainMenuActivity extends Activity {

    Button btnNewGame;
    Button btnEditCourses;
    Button btnEditPlayers;
    Button btnScorecards;
    Context context;
	ListView lvMainMenu;
	MainMenuDataAdapter adapter;

	String[] menuItems = {"New Game", "Resume Game", "Course Editor", "Player Editor", "Score Cards"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);

        context = this;

		lvMainMenu = (ListView) findViewById(R.id.lvMainMenu);
		adapter = new MainMenuDataAdapter(context,menuItems);
		lvMainMenu.setAdapter(adapter);
		lvMainMenu.setDivider(null);
		lvMainMenu.setDividerHeight(0);

		lvMainMenu.setOnItemClickListener(new AdapterView.OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				switch (position){
					case 0:
						OnNewGameClick(view);
						break;
					case 1:
						break;
					case 2:
						OnEditCoursesClick(view);
						break;
					case 3:
						OnEditPlayersClick(view);
						break;
					case 4:
						OnScorecardsClick(view);
						break;
				}
			}
		});



    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
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
        Intent intent = new Intent(context,PlayerEditorActivity.class);
        startActivity(intent);
    }
    public void OnScorecardsClick(View v){
        Toast toast = Toast.makeText(context,"You've clicked View Scorecards, still not implemented",Toast.LENGTH_LONG);
        toast.show();
        //Intent intent = new Intent(context, RuntimeGameActivity.class);
        //startActivity(intent);
    }
}
