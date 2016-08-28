package com.example.michael.discgolfapp.Activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.michael.discgolfapp.Model.ScoreCard;
import com.example.michael.discgolfapp.R;

/**
 * Created by Michael on 8/22/2016.
 */
public class ScorecardOverview extends AppCompatActivity {
	Context context;
	TableLayout tblFinalScorecard;
	TextView tvFSCourseTitle;
	ScoreCard scoreCard;

	private final int FIRST_COLUMN_WIDTH = 80;
	private final int LAYOUT_WIDTH = 50;
	private final int TEXT_SIZE = 20;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.scorecard_finished_overview_layout);
		context = this;

		tblFinalScorecard = (TableLayout) findViewById(R.id.tblFinalScorecard);
		tvFSCourseTitle = (TextView) findViewById(R.id.tvFSCourseTitle);

		initScoreCardData();

		generateTable();

		tvFSCourseTitle.setText(scoreCard.getCourseName());


	}

	private void initScoreCardData(){
		Intent intent = getIntent();
		Bundle b = intent.getExtras();
		if (b != null){
			scoreCard = (ScoreCard) b.getSerializable("Finished Game");
		}
	}

	private void generateTable(){
		tblFinalScorecard.removeAllViews();

		//Generate dynamic table.
		for (int i = 0; i < scoreCard.getPlayersCount() + 2; i++){ //Plus 2 for Par And Hole # rows.
			// outer for loop
			TableRow row = new TableRow(context);
			row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
					TableRow.LayoutParams.WRAP_CONTENT));
			// inner for loop
			for (int j = 0; j < scoreCard.getCourse().getHoleCount() + 2; j++) { // Plus 2 for Name and Total Columns

				if (i == 0 && j == 0){ //First Cell is Hole
					TextView tv = setupTextViewInTable("Hole",applyLayoutWidth(FIRST_COLUMN_WIDTH), R.drawable.cell_shape_light_green);
					row.addView(tv);
				}

				else if (i == 0 && j == scoreCard.getCourse().getHoleCount() + 1){ //Last Cell is T
					TextView tv = setupTextViewInTable("T",applyLayoutWidth(LAYOUT_WIDTH), R.drawable.cell_shape_light_green);
					row.addView(tv);
				}

				else if (i == 0){
					TextView tv = setupTextViewInTable(String.valueOf(j),applyLayoutWidth(LAYOUT_WIDTH), R.drawable.cell_shape_light_green);
					row.addView(tv);
				}

				if (i == 1 && j == 0){ // First Cell in second row is "Par"
					TextView tv = setupTextViewInTable("Par",applyLayoutWidth(FIRST_COLUMN_WIDTH), R.drawable.cell_shape_light_green);
					row.addView(tv);
				}

				else if (i == 1 && j == scoreCard.getCourse().getHoleCount() + 1){ // Last Cell is Par Total
					TextView tv = setupTextViewInTable(String.valueOf(scoreCard.getCourse().getParTotal()),applyLayoutWidth(LAYOUT_WIDTH), R.drawable.cell_shape_light_green);
					row.addView(tv);
				}

				else if (i == 1 && j != scoreCard.getCourse().getHoleCount()+1){
					TextView tv = setupTextViewInTable(String.valueOf(scoreCard.getCourse().getParArray()[j-1]),applyLayoutWidth(LAYOUT_WIDTH), R.drawable.cell_shape_light_green);
					row.addView(tv);
				}

				if (i >= 2 && j == 0){ //Set Player Names
					TextView tv = setupTextViewInTable(scoreCard.getPlayerArray()[i-2].getName(),applyLayoutWidth(FIRST_COLUMN_WIDTH), R.drawable.cell_shape);
					row.addView(tv);
				}

				if (i >= 2 && j >0 && j != scoreCard.getCourse().getHoleCount()+1){ //handle player scores
					TextView tv = setupTextViewInTable(String.valueOf(scoreCard.getPlayerArray()[i-2].getScore()[j-1]),applyLayoutWidth(LAYOUT_WIDTH), R.drawable.cell_shape);
					row.addView(tv);
				}

				if (i >= 2 && j == scoreCard.getCourse().getHoleCount() + 1){
					TextView tv = setupTextViewInTable(String.valueOf(scoreCard.getPlayerArray()[i-2].getCurrentTotal()),applyLayoutWidth(LAYOUT_WIDTH), R.drawable.cell_shape);
					row.addView(tv);
				}


			}
			tblFinalScorecard.addView(row);
		}
	}

	private TextView setupTextViewInTable(String setText, int layoutWidthParam, int resourceID ){
		TextView tv = new TextView(context);
		tv.setLayoutParams(new TableRow.LayoutParams(layoutWidthParam,
				TableRow.LayoutParams.MATCH_PARENT, 1.0f));
		tv.setBackgroundResource(resourceID);
		tv.setTextColor(Color.BLACK);
		//tv.setTextAppearance(context,android.R.style.TextAppearance_Large);
		tv.setTextSize(TEXT_SIZE);
		tv.setGravity(Gravity.CENTER);
		tv.setPadding(15, 5, 15, 5);
		tv.setText(setText);
		tv.setSingleLine();
		return tv;
	}

	private int applyLayoutWidth(int width){
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, width, getResources().getDisplayMetrics());
	}
}
