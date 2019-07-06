package com.manleysoftware.michael.discgolfapp.view;

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
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.manleysoftware.michael.discgolfapp.view.Adapters.ScoreCardDataAdapter;
import com.manleysoftware.michael.discgolfapp.BuildConfig;
import com.manleysoftware.michael.discgolfapp.Model.ScoreCard;
import com.manleysoftware.michael.discgolfapp.data.ScorecardRepository;
import com.manleysoftware.michael.discgolfapp.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

/**
 * Created by Michael on 8/16/2016.
 */
public class FinishedScorecardsActivity extends AppCompatActivity {

	private Context context;
	private ListView lvFinishedScoreCards;
	private ScoreCardDataAdapter adapter;
	private ScorecardRepository scorecardRepository;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.finished_scorecards_layout);
		context = this;

		if (BuildConfig.FLAVOR.equals("free")) {
			AdView adView = (AdView) findViewById(R.id.adViewFinishedSC);
			AdRequest request = new AdRequest.Builder().build();
//					.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
//					.build();
			if (adView != null)
				adView.loadAd(request);
		}


		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); //Set Portrait because coming back from scorecard overview comes back landscape.
		//todo consider changing orientation just before intent sends us back here from the overview of scorecard activity

		lvFinishedScoreCards = (ListView) findViewById(R.id.lvFinishedScoreCards);
		RelativeLayout finishedScoreCardLinearLayout = (RelativeLayout) findViewById(R.id.finishedScoreCardLinearLayout);

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
								scorecardRepository.DeleteScoreCardFromStorage(position);

								//Commit the change to persistant memory
								scorecardRepository.SaveFinishedCardsToFile(context);

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
		if (scorecardRepository.getCount() >= 1){
			adapter = new ScoreCardDataAdapter(context, scorecardRepository);
			lvFinishedScoreCards.setAdapter(adapter);
			return true;
		}
		return false;
	}

	private void initializeScoreCardStorage(){
		scorecardRepository = ScorecardRepository.LoadFinishedCardStorage(context);
		if (scorecardRepository == null){
			scorecardRepository = new ScorecardRepository();
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
						scorecardRepository.DeleteAll();

						//Commit the change to persistant memory
						scorecardRepository.SaveFinishedCardsToFile(context);

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
