package com.example.michael.discgolfapp.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.michael.discgolfapp.Adapters.ScoreCardDataAdapter;
import com.example.michael.discgolfapp.Model.ScoreCard;
import com.example.michael.discgolfapp.Model.ScoreCardStorage;
import com.example.michael.discgolfapp.R;

import java.util.List;

/**
 * Created by Michael on 8/16/2016.
 */
public class FinishedScorecardsActivity extends Activity {

	Context context;
	ListView lvFinishedScoreCards;
	ScoreCardDataAdapter adapter;
	ScoreCardStorage scoreCardStorage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.finished_scorecards_layout);
		context = this;

		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		lvFinishedScoreCards = (ListView) findViewById(R.id.lvFinishedScoreCards);

		initializeScoreCardStorage();

		setListViewAdapter();

		setupScoreCardListView();

	}

	private void setupScoreCardListView(){
		lvFinishedScoreCards.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Bundle b = new Bundle();
				b.putSerializable("Finished Game", (ScoreCard)adapter.getItem(position));
				Intent intent = new Intent(context, ScorecardOverview.class);
				intent.putExtras(b);
				startActivity(intent);
			}
		});

		lvFinishedScoreCards.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
				AlertDialog.Builder aat = new AlertDialog.Builder(context);
				aat.setTitle("Delete?")
						.setMessage("Are you sure you want to delete this unfinished game?")
						.setCancelable(true)
						.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){

							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								dialog.cancel();
							}

						})
						.setPositiveButton("Delete", new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								//Make the change
								scoreCardStorage.DeleteScoreCardFromStorage(position);

								//Commit the change to persistant memory
								scoreCardStorage.SaveFinishedCardsToFile(context);
								onCreate(null);
							}
						});
				AlertDialog art = aat.create();

				art.show();
				return true;
			}
		});
	}

	private void setListViewAdapter(){
		if (scoreCardStorage.getCount() >= 1){
			adapter = new ScoreCardDataAdapter(context, scoreCardStorage);
			lvFinishedScoreCards.setAdapter(adapter);
		}
	}

	private void initializeScoreCardStorage(){
		scoreCardStorage = ScoreCardStorage.LoadFinishedCardStorage(context);
		if (scoreCardStorage == null){
			scoreCardStorage = new ScoreCardStorage();
			Toast.makeText(context,"Failed to init Scorecards", Toast.LENGTH_SHORT).show();
		}
	}
}
