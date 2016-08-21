package com.example.michael.discgolfapp.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
public class UnfinishedScorecardsActivity extends Activity {

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
				Intent intent = new Intent(context,RuntimeGameActivity.class);
				intent.putExtras(b);
				startActivity(intent);
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
