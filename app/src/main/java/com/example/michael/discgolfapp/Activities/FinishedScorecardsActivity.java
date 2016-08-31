package com.example.michael.discgolfapp.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.michael.discgolfapp.Adapters.ScoreCardDataAdapter;
import com.example.michael.discgolfapp.Model.ScoreCard;
import com.example.michael.discgolfapp.Model.ScoreCardStorage;
import com.example.michael.discgolfapp.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Michael on 8/16/2016.
 */
public class FinishedScorecardsActivity extends AppCompatActivity {

	Context context;
	ListView lvFinishedScoreCards;
	RelativeLayout finishedScoreCardLinearLayout;
	ScoreCardDataAdapter adapter;
	ScoreCardStorage scoreCardStorage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.finished_scorecards_layout);
		context = this;

		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); //Set Portrait because coming back from scorecard overview comes back landscape.
		//todo consider changing orientation just before intent sends us back here from the overview of scorecard activity

		lvFinishedScoreCards = (ListView) findViewById(R.id.lvFinishedScoreCards);
		finishedScoreCardLinearLayout = (RelativeLayout) findViewById(R.id.finishedScoreCardLinearLayout);

		initializeScoreCardStorage();

		if (!setListViewAdapter()){ //If nothing to populate adapter and listview fails to be created
			View messageLayout = getLayoutInflater().inflate(R.layout.listview_alternative_layout,null);

			ImageView backgroundImage = (ImageView) messageLayout.findViewById(R.id.ivImage);
			Bitmap bm5 = BitmapFactory
					.decodeResource(context.getResources(), R.drawable.katana_500x500);
			backgroundImage.setImageBitmap(bm5);

			TextView tvNoListViewMessage = (TextView) messageLayout.findViewById(R.id.tvNoListViewMessage);
			tvNoListViewMessage.setText("Oops! No finished games here!");
			finishedScoreCardLinearLayout.addView(messageLayout);
		}

		setupScoreCardListView();

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()){
			case R.id.action_settings:
				showDeleteAllDialog();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main,menu);

		return true;
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
						.setMessage("Are you sure you want to delete this finished score card?")
						.setCancelable(true)
						.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){

							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.cancel();
							}

						})
						.setPositiveButton("Delete", new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								//Make the change
								scoreCardStorage.DeleteScoreCardFromStorage(position);

								//Commit the change to persistant memory
								scoreCardStorage.SaveFinishedCardsToFile(context);

								//Update View
								adapter.notifyDataSetChanged();
								if (adapter.getCount() == 0){
									recreate();
								}
							}
						});
				AlertDialog art = aat.create();

				art.show();
				return true;
			}
		});
	}

	private boolean setListViewAdapter(){
		if (scoreCardStorage.getCount() >= 1){
			adapter = new ScoreCardDataAdapter(context, scoreCardStorage);
			lvFinishedScoreCards.setAdapter(adapter);
			return true;
		}
		return false;
	}

	private void initializeScoreCardStorage(){
		scoreCardStorage = ScoreCardStorage.LoadFinishedCardStorage(context);
		if (scoreCardStorage == null){
			scoreCardStorage = new ScoreCardStorage();
			Toast.makeText(context,"Failed to init Scorecards", Toast.LENGTH_SHORT).show();
		}
	}

	private void showDeleteAllDialog(){
		AlertDialog.Builder aat = new AlertDialog.Builder(context);
		aat.setTitle("Delete?")
				.setMessage("Are you sure you want to delete ALL finished score cards?")
				.setCancelable(true)
				.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}

				})
				.setPositiveButton("Delete", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						//Make the change
						scoreCardStorage.DeleteAll();

						//Commit the change to persistant memory
						scoreCardStorage.SaveFinishedCardsToFile(context);

						//Refresh adapter view
						adapter.notifyDataSetChanged();
						if (adapter.getCount() == 0){
							recreate();
						}

					}
				});
		AlertDialog art = aat.create();

		art.show();
	}
}
