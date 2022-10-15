package com.manleysoftware.michael.discgolfapp.ui.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;


import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.manleysoftware.michael.discgolfapp.ui.Adapters.MainMenuDataAdapter;
import com.manleysoftware.michael.discgolfapp.BuildConfig;
import com.manleysoftware.michael.discgolfapp.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.manleysoftware.michael.discgolfapp.ui.course.CourseEditorMenuActivity;
import com.manleysoftware.michael.discgolfapp.ui.player.PlayerListActivity;
import com.manleysoftware.michael.discgolfapp.ui.scorecard.FinishedScorecardsActivity;
import com.manleysoftware.michael.discgolfapp.ui.scorecard.UnfinishedScorecardsActivity;

/**
 * Created by Michael on 6/22/2016.
 */
public class MainMenuActivity extends AppCompatActivity {

    private Button btnNewGame;
    private Button btnEditCourses;
    private Button btnEditPlayers;
    private Button btnScorecards;
    private Context context;

    private final String[] menuItems = {"New Game", "Resume Game", "Course Editor", "Player Editor", "Score Cards"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);
        context = this;

        if (BuildConfig.FLAVOR.equals("free")) {
            //String ADMOB_APP_ID = "ca-app-pub-8285085024937633~8121121504"; //Test admob app id
            MobileAds.initialize(context);

            AdView adView = (AdView) findViewById(R.id.adView);
            AdRequest request = new AdRequest.Builder().build();
//					.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
//					.build();
            if (adView != null)
                adView.loadAd(request);
        }


        ListView lvMainMenu = (ListView) findViewById(R.id.lvMainMenu);
        MainMenuDataAdapter adapter = new MainMenuDataAdapter(context, menuItems);
        lvMainMenu.setAdapter(adapter);
        lvMainMenu.setDivider(null); //Removes separating lines between the menu items.
        lvMainMenu.setDividerHeight(0);//^^^Same

        lvMainMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
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

    private void OnNewGameClick(View v) {
        Intent intent = new Intent(this, CoursePickerActivity.class);
        startActivity(intent);
    }

    private void OnEditCoursesClick(View v) {
        Intent intent = new Intent(context, CourseEditorMenuActivity.class);
        startActivity(intent);
    }

    private void OnEditPlayersClick(View v) {
        Intent intent = new Intent(context, PlayerListActivity.class);
        startActivity(intent);
    }

    private void OnScorecardsClick(View v) {
        Intent intent = new Intent(context, FinishedScorecardsActivity.class);
        startActivity(intent);
    }

    private void OnResumeGameClicked(View v) {
        Intent intent = new Intent(context, UnfinishedScorecardsActivity.class);
        startActivity(intent);
    }
}
