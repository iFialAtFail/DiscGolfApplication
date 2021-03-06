package com.manleysoftware.michael.discgolfapp.Activities;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.manleysoftware.michael.discgolfapp.CustomViews.ObservableHorizontalScrollView;
import com.manleysoftware.michael.discgolfapp.CustomViews.ObservableScrollView;
import com.manleysoftware.michael.discgolfapp.Interfaces.IHorizontalScrollViewListener;
import com.manleysoftware.michael.discgolfapp.Interfaces.IScrollViewListener;
import com.manleysoftware.michael.discgolfapp.Model.Course;
import com.manleysoftware.michael.discgolfapp.Model.Player;
import com.manleysoftware.michael.discgolfapp.Model.ScoreCard;
import com.manleysoftware.michael.discgolfapp.Model.ScoreCardStorage;
import com.manleysoftware.michael.discgolfapp.R;

import java.util.ArrayList;
import java.util.List;

public class RuntimeGameActivity extends AppCompatActivity implements IScrollViewListener, IHorizontalScrollViewListener {

    //region Private Model Variables

    private Course course;
    private Player[] players;
    private ScoreCard scoreCard;
	private ScoreCardStorage finishedCards;
	private ScoreCardStorage unFinishedCards;
    private Context context;
	private Boolean gameStarted = false;

    //Variable to store the last red selected cell
    private TextView lastTVBackgroundChanged;

    //Cache the scoreTotal TextViews
    private TextView[] scoreTotalTextViews;

    //endregion

    //region Constants



    private final int LAYOUT_WIDTH = 50;
    private final int TOTAL_COLUMN_WIDTH = 60;

	//endregion

    //region Control/View references

	//Custom Scrollviews
    private ObservableHorizontalScrollView parHorizontalScrollView;
    private ObservableHorizontalScrollView scoreHorizontalScrollView;
    private ObservableScrollView nameScrollView;
    private ObservableScrollView scoreScrollView;
    private ObservableScrollView currentScoreSV;

    //Static in name implies a stationary view.
    //Dynimic in name implies a view wrapped in a custom scrollview.
    private TableLayout scoreTable;
    private TableLayout nameTable;
    private TableLayout dynamicHeaderTable;
    private TableLayout staticHeaderRowsTable;
    private TableLayout currentScoreTable;
    private TableLayout staticParCountTable;

	//endregion

    //region Android Product Lifecycle

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //this.setTheme(R.style.NoActionBarTheme);
        setContentView(R.layout.runtime_game_layout);
        context = this;

        //region Setup View References

		TextView titleCourseTextView = (TextView) findViewById(R.id.titleCourseTextView);

        scoreTable = (TableLayout) findViewById(R.id.scoreTable);
        nameTable = (TableLayout) findViewById(R.id.nameTable);
        dynamicHeaderTable = (TableLayout) findViewById(R.id.parTable);
        staticHeaderRowsTable = (TableLayout) findViewById(R.id.parCellTable);
        staticParCountTable = (TableLayout) findViewById(R.id.staticParCountTable);
        currentScoreTable = (TableLayout) findViewById(R.id.currentScoreTable);

        parHorizontalScrollView = (ObservableHorizontalScrollView) findViewById(R.id.parHorizontalScrollView);
        parHorizontalScrollView.setHorizontalScrollViewListener(this);
        scoreHorizontalScrollView = (ObservableHorizontalScrollView) findViewById(R.id.scoreHorizontalScrollView);
        scoreHorizontalScrollView.setHorizontalScrollViewListener(this);

        nameScrollView = (ObservableScrollView) findViewById(R.id.nameScrollView);
        nameScrollView.setScrollViewListener(this);
        scoreScrollView = (ObservableScrollView) findViewById(R.id.scoreScrollView);
        scoreScrollView.setScrollViewListener(this);
        currentScoreSV = (ObservableScrollView) findViewById(R.id.currentScoreSV);
        currentScoreSV.setScrollViewListener(this);


        //endregion

		//retrieve Player/Course data
        //and setup references
        retrieveGameData();



        //If first time go, create new stuff
        if (savedInstanceState == null) {
            initPlayers(players);
            if (scoreCard == null) //If coming from new game path...
				scoreCard = new ScoreCard(players, course);
        }

        //If on recreation, pull out the saved state information and reset the object
        //to the previous state.
        if (savedInstanceState != null) {
            players = (Player[])savedInstanceState.getSerializable("RestorePlayers");
            scoreCard = (ScoreCard) savedInstanceState.getSerializable("RestoreScoreCard");
        }
        scoreTotalTextViews = new TextView[players.length];

        titleCourseTextView.setText(course.getName());
        generateTables(players, course.getHoleCount());
        updateSelectedCell();


    }

    //endregion

    private void generateTables(Player[] players, int courseHoleCount){

        //Setup Static cells (Par, Hole#)
        setupStaticHeaderRows();

        //Setup Static Header Par and Holes cells
        String[] input = {"T", course.getParTotal()+ "" };
        setupStaticParCountTable(input);

        //Setup Par/Hole# Rows
        setupDynamicHeaderTable();

        //Setup Final Current Score Column. Needs to be called on score changes.
        setupCurrentScoreColumn(players);


        //Setup Name Column
        setupNameColumn(players);

        //Generate the score table. Used to refresh the view as well.
        generateScoreTable(players, courseHoleCount);


    }

    //region Button Handler Methods

    public void OnIncrementScoreClick(View v){
		gameStarted = true;
        players[scoreCard.getCurrentPlayerSelected()].IncrementCurrentScore(scoreCard.getCurrentHole());
//        generateScoreTable(players, course.getHoleCount());
//        setupCurrentScoreColumn(players);

        updateScoreTable();
        updateSelectedCell();
    }

    public void OnDecrementScoreClick(View v){
		gameStarted = true;
        scoreCard.getPlayerArray()[scoreCard.getCurrentPlayerSelected()].DecrementCurrentScore(scoreCard.getCurrentHole());
//        generateScoreTable(players, course.getHoleCount());
//        setupCurrentScoreColumn(players);
        updateScoreTable();
        updateSelectedCell();

    }

    public void OnNextHoleClick(View v){
        scoreCard.NextHole();
        updateSelectedCell();
    }

    public void OnPreviousHoleClick(View v){
        scoreCard.PreviousHole();
        updateSelectedCell();
    }

    public void OnLastPlayerClick(View view) {
        if (scoreCard.getCurrentPlayerSelected() > 0 ){
            scoreCard.LastPlayer();
            updateSelectedCell();
        }
    }

    public void OnNxtPlayerClick(View view) {
        if (scoreCard.getCurrentPlayerSelected() < players.length -1){
            scoreCard.NextPlayer();
            updateSelectedCell();
        }
    }

	public void OnSaveFinnishGameClick(View view) {
		AlertDialog.Builder aat = new AlertDialog.Builder(context);
		aat.setTitle("End Game?")
				.setMessage("If you are finished, select \"Finished\". If you want to save your game to resume later, select \"Save\"")
				.setCancelable(true)
				.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				})
				.setPositiveButton("Finish", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						finishedCards.AddScoreCardsToStorage(scoreCard);
						finishedCards.SaveFinishedCardsToFile(context);
						Intent intent = new Intent(context,MainMenuActivity.class);
						startActivity(intent);
					}
				})
				.setNeutralButton("Save", new DialogInterface.OnClickListener(){
					@Override
					public void onClick(DialogInterface dialog, int which){
						unFinishedCards.AddScoreCardsToStorage(scoreCard);
						unFinishedCards.SaveUnFinishedCardListToFile(context);
						Intent intent = new Intent(context, MainMenuActivity.class);
						startActivity(intent);
					}
				});
		AlertDialog art = aat.create();

		art.show();

	}

    //endregion

    //region Private Helper Methods

    private void retrieveGameData(){
        Bundle b = this.getIntent().getExtras();
        if (b != null){

			scoreCard = (ScoreCard) b.getSerializable("Unfinished Game");

			//If coming from resume game menu....
			if (scoreCard != null){
				players = scoreCard.getPlayerArray();
				course = scoreCard.getCourse();
			}

			//else if coming from new game path.
			else{
				players = toPlayerArray((ArrayList<Player>) b.getSerializable("Players"));
				course = (Course) b.getSerializable("Course");
			}

        }

		unFinishedCards = ScoreCardStorage.LoadUnFinishedCardStorage(context);
		if (unFinishedCards == null){
			unFinishedCards = new ScoreCardStorage();
		}

		finishedCards = ScoreCardStorage.LoadFinishedCardStorage(context);
		if (finishedCards == null){
			finishedCards = new ScoreCardStorage();
		}

    }

    private void updateSelectedCell(){
        TableRow tr = (TableRow) scoreTable.getChildAt(scoreCard.getCurrentPlayerSelected());
        TextView tv = (TextView) tr.getChildAt(scoreCard.getCurrentHole()-1);

        //Change last red color to white again.
        if (lastTVBackgroundChanged != null) {
            lastTVBackgroundChanged.setBackgroundResource(R.drawable.cell_shape);
        }

        //Reset reference to current background and change it to red.
        lastTVBackgroundChanged = tv;
        lastTVBackgroundChanged.setBackgroundResource(R.drawable.cell_shape_red);

        //Set focus to current cell.
        tv.getParent().requestChildFocus(tv,tv);
    }

    private void updateScoreTable(){
        //data model has been updated to reflect the current player and hole that just got incremented/decremented
        //use that data to then get the approprite tv in the score and scoreTotal tv and update them

        TableRow tr = (TableRow) scoreTable.getChildAt(scoreCard.getCurrentPlayerSelected());
        TextView tv = (TextView) tr.getChildAt(scoreCard.getCurrentHole()-1);

        int score = players[scoreCard.getCurrentPlayerSelected()].getScore()[scoreCard.getCurrentHole()-1];
        //Update the current box needing updated
        tv.setText(score + "");
        //Update scoreTotal
        scoreTotalTextViews[scoreCard.getCurrentPlayerSelected()].setText(players[scoreCard.getCurrentPlayerSelected()].getCurrentParDifference(course.getParTotal()) + "");
    }

    private Player[] toPlayerArray(ArrayList<Player> list){
        Player[] ret = new Player[list.size()];
        for(int i = 0;i < ret.length;i++)
            ret[i] = list.get(i);
        return ret;
    }

    private void initPlayers(Player[] players){
        for (Player player : players){
            player.StartGame(course);
        }
    }

    private TextView setupTextViewInTable(String setText, int layoutWidthParam, int resourceID ){
        TextView tv = new TextView(context);
        tv.setLayoutParams(new TableRow.LayoutParams(layoutWidthParam,
                TableRow.LayoutParams.MATCH_PARENT, 1.0f));
        tv.setBackgroundResource(resourceID);
        tv.setTextColor(Color.BLACK);
		//tv.setTextAppearance(context,android.R.style.TextAppearance_Large);
		int TEXT_SIZE = 20;
		tv.setTextSize(TEXT_SIZE);
		tv.setGravity(Gravity.CENTER);
        tv.setPadding(15, 5, 15, 5);
        tv.setText(setText);
        return tv;
    }

    private void generateScoreTable(Player[] players, int courseHoleCount) {
        // setup score info table
        scoreTable.removeAllViews();

        //Generate dynamic table.
        for (int i = 1; i <= players.length; i++){
            // outer for loop
            TableRow row = new TableRow(context);
            row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            // inner for loop
            for (int j = 1; j <= courseHoleCount; j++) {
                TextView tv = setupTextViewInTable(String.valueOf(players[i-1].getScore()[j-1] ), applyLayoutWidth(LAYOUT_WIDTH), R.drawable.cell_shape);
                row.addView(tv);
            }
            scoreTable.addView(row);
        }

    }

    private void setupStaticHeaderRows(){
        if (staticHeaderRowsTable.getChildCount() < 1) {
            String[] textInput = {"Hole", "Par"};
			for (String aTextInput : textInput) {

				LinearLayout tableRow = new LinearLayout(context); //Must me linear layout, since TableRow won't actually fill parent.
				TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 1f);
				tableRow.setLayoutParams(params);

				TextView tv = setupTextViewInTable(aTextInput,
						TableRow.LayoutParams.MATCH_PARENT,//Setting only width param
						R.drawable.cell_shape_light_green); //This forced width but the next line should be a quick fix for it.

				tableRow.addView(tv);
				staticHeaderRowsTable.addView(tableRow);
			}
        }
    }

    private void setupStaticParCountTable(String[] textArray){
        if (staticParCountTable.getChildCount() < 1){
            for(int i = 0; i<2; i++) {
                TableRow tr = new TableRow(context); //<-- This time Linear Layout is acting like "Wrap content". TableRow works though.
                TableRow.LayoutParams params =
                        new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT, 1f);
                tr.setLayoutParams(params);

                TextView tv = setupTextViewInTable(textArray[i], applyLayoutWidth(TOTAL_COLUMN_WIDTH), R.drawable.cell_shape_light_green);
                if (i == 0){
                    tv.setTypeface(null, Typeface.BOLD);
                }

                tr.addView(tv);
                staticParCountTable.addView(tr);
            }
        }
    }

    private void setupDynamicHeaderTable() {
        if (dynamicHeaderTable.getChildCount() < 1){

			dynamicHeaderTable.setStretchAllColumns(true);
			//Setup Hole# row
            TableRow holeNum = new TableRow(this);
            TableLayout.LayoutParams params = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 1.0f);
            holeNum.setLayoutParams(params);


            for (int a = 0; a<course.getParArray().length; a++){

				TextView tv = setupTextViewInTable(String.valueOf(a+1), applyLayoutWidth(LAYOUT_WIDTH),R.drawable.cell_shape_light_green);
                tv.setGravity(Gravity.CENTER);
                tv.setTypeface(null, Typeface.BOLD);

                holeNum.addView(tv);
            }
            dynamicHeaderTable.addView(holeNum);

            //setup par row
            TableRow parNum = new TableRow(this);
            parNum.setLayoutParams(params);

            for (int a = 0; a<course.getParArray().length; a++){
                TextView tv = setupTextViewInTable(String.valueOf(course.getParArray()[a]),TableRow.LayoutParams.MATCH_PARENT,R.drawable.cell_shape_light_green);
                tv.setGravity(Gravity.CENTER);
                parNum.addView(tv);
            }
            dynamicHeaderTable.addView(parNum);


        }
    }

    private void setupCurrentScoreColumn(Player[] players) {

        int i = 0;
        currentScoreTable.removeAllViews();
		for (Player player : players) {
			LinearLayout _linearLayout = new LinearLayout(context);
			TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT, 1f);
			_linearLayout.setLayoutParams(params);

			TextView tv = setupTextViewInTable(player.getCurrentParDifference(course.getParTotal()) + "", applyLayoutWidth(TOTAL_COLUMN_WIDTH), R.drawable.cell_shape);
			tv.setSingleLine();
            scoreTotalTextViews[i] = tv;
			_linearLayout.addView(tv);
			currentScoreTable.addView(_linearLayout);
			i++;
		}

    }

    private void setupNameColumn(Player[] players) {
        if (nameTable.getChildCount() < 1){
			for (Player player : players) {
				LinearLayout linearLayout = new LinearLayout(this);
				TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 1f);
				linearLayout.setLayoutParams(params);

				TextView tv = setupTextViewInTable(player.getName(), TableRow.LayoutParams.MATCH_PARENT, R.drawable.cell_shape); //This forced width but the next line should be a quick fix for it.
				tv.setSingleLine();
				linearLayout.addView(tv);

				nameTable.addView(linearLayout);
			}
        }
    }

	private int applyLayoutWidth(int width){
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, width, getResources().getDisplayMetrics());
	}

    //endregion

    //region Overridden Methods

    //Custom Interface Override-- Handles the syncing of the scrollviews.

    //Handle Vertical Scrollview Sync
    @Override
    public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy) {
        if (scrollView == nameScrollView){
            scoreScrollView.scrollTo(x,y);
            currentScoreSV.scrollTo(x,y);
        } else if (scrollView == scoreScrollView){
            nameScrollView.scrollTo(x,y);
            currentScoreSV.scrollTo(x,y);
        } else if (scrollView == currentScoreSV){
            scoreScrollView.scrollTo(x,y);
            nameScrollView.scrollTo(x,y);
        }

    }

    //Handle Horizontal Scrollview Sync
    @Override
    public void onScrollChanged(ObservableHorizontalScrollView horizontalScrollView, int x, int y, int oldx, int oldy) {
        if (horizontalScrollView == scoreHorizontalScrollView){
            parHorizontalScrollView.scrollTo(x,y);
        } else if (horizontalScrollView == parHorizontalScrollView){
            scoreHorizontalScrollView.scrollTo(x,y);
        }
    }

    //Android Product Lifecycle overrides

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        players = (Player[])savedInstanceState.getSerializable("RestorePlayers");
        scoreCard = (ScoreCard) savedInstanceState.getSerializable("RestoreScoreCard");
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putSerializable("RestorePlayers",players);
        outState.putSerializable("RestoreScoreCard", scoreCard);

    }

	@Override
	public void onBackPressed() {

		if (gameStarted){ //If game started the normal way
			unFinishedCards.AddScoreCardsToStorage(scoreCard);
			unFinishedCards.SaveUnFinishedCardListToFile(context);
			Intent intent = new Intent(context, MainMenuActivity.class);
			startActivity(intent);
		}

		else {
			Bundle b = this.getIntent().getExtras();

			//If Coming from resume game picker, save game and go to main menu on back press
			//Fixes the bug of going back to an adapter that deleted that game on going to it.
			if (b != null && b.getInt("From Resume Game Picker") == 2) {
				unFinishedCards.AddScoreCardsToStorage(scoreCard);
				unFinishedCards.SaveUnFinishedCardListToFile(context);
				Intent intent = new Intent(context, MainMenuActivity.class);
				startActivity(intent);
			}
		}

		super.onBackPressed();

	}



	//endregion

}
