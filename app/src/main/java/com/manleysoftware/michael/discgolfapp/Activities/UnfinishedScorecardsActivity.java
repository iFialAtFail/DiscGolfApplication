package com.manleysoftware.michael.discgolfapp.Activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.manleysoftware.michael.discgolfapp.Adapters.ScoreCardDataAdapter;
import com.manleysoftware.michael.discgolfapp.BuildConfig;
import com.manleysoftware.michael.discgolfapp.Model.ScoreCard;
import com.manleysoftware.michael.discgolfapp.Model.ScoreCardStorage;
import com.manleysoftware.michael.discgolfapp.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

/**
 * Created by Michael on 8/16/2016.
 */
public class UnfinishedScorecardsActivity extends AppCompatActivity {

	Context context;
	ListView lvUnfinishedScoreCards;
	ScoreCardStorage scoreCardStorage;
	ScoreCardDataAdapter adapter;
	RelativeLayout unFinishedScoreCardRelativeLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.unfinished_scorecards_layout);
		context = this;

		if (BuildConfig.FLAVOR.equals("free")) {
			// add some ads or restrict functionallity
			AdView adView = (AdView) findViewById(R.id.adViewUFSC);
			AdRequest request = new AdRequest.Builder()
					.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
					.build();
			adView.loadAd(request);
		}


		lvUnfinishedScoreCards = (ListView) findViewById(R.id.lvUnfinishedScoreCards);
		unFinishedScoreCardRelativeLayout = (RelativeLayout) findViewById(R.id.unfinishedScoreCardRelativeLayout);

		initializeScoreCardStorage();

		if (!onSetupListViewAdapter()){ //If adapter never got made because list is empty...
			View messageLayout = getLayoutInflater().inflate(R.layout.listview_alternative_layout,null);

			ImageView backgroundImage = (ImageView) messageLayout.findViewById(R.id.ivImage);
			Bitmap bm5 = BitmapFactory
					.decodeResource(context.getResources(), R.drawable.nuke_500x500);
			backgroundImage.setImageBitmap(bm5);

			TextView tvNoListViewMessage = (TextView) messageLayout.findViewById(R.id.tvNoListViewMessage);
			tvNoListViewMessage.setText("Oops! No games to resume yet!");
			unFinishedScoreCardRelativeLayout.addView(messageLayout);
		}

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
								dialog.cancel();
							}

						})
						.setPositiveButton("Delete", new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {

								//Make the change
								scoreCardStorage.DeleteScoreCardFromStorage(position);

								//Commit the change to persistant memory
								scoreCardStorage.SaveUnFinishedCardListToFile(context);
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

	private boolean onSetupListViewAdapter() {
		if (scoreCardStorage.getCount() >= 1) {
			adapter = new ScoreCardDataAdapter(context, scoreCardStorage);
			lvUnfinishedScoreCards.setAdapter(adapter);
			return true;
		}
		return false;
	}

	private void initializeScoreCardStorage(){
		scoreCardStorage = ScoreCardStorage.LoadUnFinishedCardStorage(context);
		if (scoreCardStorage == null){
			scoreCardStorage = new ScoreCardStorage();
		}
	}
}
