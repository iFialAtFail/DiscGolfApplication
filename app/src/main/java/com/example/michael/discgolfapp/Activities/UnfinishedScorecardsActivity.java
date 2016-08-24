package com.example.michael.discgolfapp.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.michael.discgolfapp.Adapters.ScoreCardDataAdapter;
import com.example.michael.discgolfapp.Model.ScoreCard;
import com.example.michael.discgolfapp.Model.ScoreCardStorage;
import com.example.michael.discgolfapp.R;

/**
 * Created by Michael on 8/16/2016.
 */
public class UnfinishedScorecardsActivity extends AppCompatActivity {

	Context context;
	ListView lvUnfinishedScoreCards;
	ScoreCardStorage scoreCardStorage;
	ScoreCardDataAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.unfinished_scorecards_layout);
		context = this;


		lvUnfinishedScoreCards = (ListView) findViewById(R.id.lvUnfinishedScoreCards);

		initializeScoreCardStorage();

		setListViewAdapter();

		setupScoreCardListView();

	}

	private void setupScoreCardListView() {
		lvUnfinishedScoreCards.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Bundle b = new Bundle();
				b.putSerializable("Unfinished Game", (ScoreCard)adapter.getItem(position));
				b.putInt("From Resume Game Picker",2);
				Intent intent = new Intent(context,RuntimeGameActivity.class);
				intent.putExtras(b);
				scoreCardStorage.DeleteScoreCardFromStorage(position);
				scoreCardStorage.SaveUnFinishedCardListToFile(context);
				startActivity(intent);
			}
		});

		lvUnfinishedScoreCards.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
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
								scoreCardStorage.SaveUnFinishedCardListToFile(context);
								onCreate(null);
							}
						});
				AlertDialog art = aat.create();

				art.show();
				return true;
			}
		});
	}

	private void setListViewAdapter() {
		if (scoreCardStorage.getCount() >= 1) {
			adapter = new ScoreCardDataAdapter(context, scoreCardStorage);
			lvUnfinishedScoreCards.setAdapter(adapter);
		}
	}

	private void initializeScoreCardStorage(){
		scoreCardStorage = ScoreCardStorage.LoadUnFinishedCardStorage(context);
		if (scoreCardStorage == null){
			scoreCardStorage = new ScoreCardStorage();
			Toast.makeText(context,"Failed to init scorecards",Toast.LENGTH_LONG).show();
		}
	}
}
