package com.example.michael.discgolfapp.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.example.michael.discgolfapp.R;

import java.util.List;

/**
 * Created by Michael on 8/16/2016.
 */
public class FinishedScorecardsActivity extends Activity {

	ListView lvFinishedScoreCards;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.finished_scorecards_layout);

		lvFinishedScoreCards = (ListView) findViewById(R.id.lvFinishedScoreCards);

	}
}
