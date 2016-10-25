package com.manleysoftware.michael.discgolfapp.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;


import com.manleysoftware.michael.discgolfapp.Adapters.MainMenuDataAdapter;
import com.manleysoftware.michael.discgolfapp.BuildConfig;
import com.manleysoftware.michael.discgolfapp.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

/**
 * Created by Michael on 6/22/2016.
 */
public class MainMenuActivity extends AppCompatActivity {

    Button btnNewGame;
    Button btnEditCourses;
    Button btnEditPlayers;
    Button btnScorecards;
    Context context;
	ListView lvMainMenu;
	MainMenuDataAdapter adapter;
	final String ADMOB_APP_ID = "ca-app-pub-8285085024937633~8121121504";

	String[] menuItems = {"New Game", "Resume Game", "Course Editor", "Player Editor", "Score Cards"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);
		context = this;

		if (BuildConfig.FLAVOR == "free") {
			MobileAds.initialize(context,ADMOB_APP_ID);

			AdView adView = (AdView) findViewById(R.id.adView);
			AdRequest request = new AdRequest.Builder()
					.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
					.build();
			adView.loadAd(request);
		}




		lvMainMenu = (ListView) findViewById(R.id.lvMainMenu);
		adapter = new MainMenuDataAdapter(context,menuItems);
		lvMainMenu.setAdapter(adapter);
		lvMainMenu.setDivider(null); //Removes separating lines between the menu items.
		lvMainMenu.setDividerHeight(0);//^^^Same

		lvMainMenu.setOnItemClickListener(new AdapterView.OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				switch (position){
					case 0:
						OnNewGameClick(view);
						break;
					case 1:
						OnResumeGameClicked(view);
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
        Intent intent = new Intent(context, FinishedScorecardsActivity.class);
        startActivity(intent);
    }

	public void OnResumeGameClicked(View v){
		Intent intent = new Intent(context, UnfinishedScorecardsActivity.class);
		startActivity(intent);
	}
}
