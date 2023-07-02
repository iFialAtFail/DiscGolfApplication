package com.manleysoftware.michael.discgolfapp.ui.scorecard;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.manleysoftware.michael.discgolfapp.BuildConfig;
import com.manleysoftware.michael.discgolfapp.R;
import com.manleysoftware.michael.discgolfapp.data.ScorecardRepository;
import com.manleysoftware.michael.discgolfapp.domain.Scorecard;
import com.manleysoftware.michael.discgolfapp.ui.adapters.ScoreCardDataAdapter;
import com.manleysoftware.michael.discgolfapp.ui.main.RuntimeGameActivity;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * Created by Michael on 8/16/2016.
 */
@AndroidEntryPoint
public class UnfinishedScorecardsActivity extends AppCompatActivity {

	private Context context;
	private ListView lvUnfinishedScoreCards;

	@Inject
	protected ScorecardRepository scorecardRepository;
	private ScoreCardDataAdapter adapter;
	private List<Scorecard> unfinishedScorecards;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.unfinished_scorecards_layout);
		context = this;

		if (BuildConfig.FLAVOR.equals("free")) {
			// add some ads or restrict functionallity
			AdView adView = (AdView) findViewById(R.id.adViewUFSC);
			AdRequest request = new AdRequest.Builder().build();
//					.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
//					.build();
			if (adView != null)
				adView.loadAd(request);
		}


		lvUnfinishedScoreCards = (ListView) findViewById(R.id.lvUnfinishedScoreCards);
		RelativeLayout unFinishedScoreCardRelativeLayout = (RelativeLayout) findViewById(R.id.unfinishedScoreCardRelativeLayout);

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
				Scorecard resumableScorecard = (Scorecard) adapter.getItem(position);
				b.putSerializable("Unfinished Game", resumableScorecard);
				b.putInt("From Resume Game Picker",2);
				Intent intent = new Intent(context, RuntimeGameActivity.class);
				intent.putExtras(b);
				//Remove scorecard from currently resumable scorecards
				scorecardRepository.delete(resumableScorecard, context);
				//TODO figure out a way to pass unfinished games to runtime
				//TODO and update the delete functionality to use "primary key" to make
				//certain we're deleting/finding the correct scorecard
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

								Scorecard scorecardToDelete = (Scorecard)adapter.getItem(position);
								//Make the change
								scorecardRepository.delete(scorecardToDelete,context);

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
		if (unfinishedScorecards.size() > 0) {
			adapter = new ScoreCardDataAdapter(context, unfinishedScorecards);
			lvUnfinishedScoreCards.setAdapter(adapter);
			return true;
		}
		return false;
	}

	private void initializeScoreCardStorage(){
		this.unfinishedScorecards = scorecardRepository.findAllUnfinishedScorecards();
	}
}
