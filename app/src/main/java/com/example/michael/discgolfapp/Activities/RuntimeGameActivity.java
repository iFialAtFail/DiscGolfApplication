package com.example.michael.discgolfapp.Activities;


import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.michael.discgolfapp.CustomViews.ObservableHorizontalScrollView;
import com.example.michael.discgolfapp.CustomViews.ObservableScrollView;
import com.example.michael.discgolfapp.Interfaces.IHorizontalScrollViewListener;
import com.example.michael.discgolfapp.Interfaces.IScrollViewListener;
import com.example.michael.discgolfapp.Model.Course;
import com.example.michael.discgolfapp.Model.Player;
import com.example.michael.discgolfapp.Model.ScoreCard;
import com.example.michael.discgolfapp.R;

import java.util.ArrayList;
import java.util.List;

public class RuntimeGameActivity extends AppCompatActivity implements IScrollViewListener, IHorizontalScrollViewListener {

    //region Private Model Variables

    Course course;
    Player[] players;
    ScoreCard scoreCard;
    //int currentPlayerSelected = 0;
    Context context;

    //endregion

    //region Constants

    private final String PLAYER_SCORES = "Player Score";
    private final String PLAYER_NAMES = "Player Name";
    private final String CURRENT_HOLE = "Current Hole";
    private final int TEXT_WIDTH = 80;
    private final int TEXT_SIZE = 20;

    //endregion

    //region Control/View references

    Button btnIncrementScore;
    Button btnDecrementScore;
    Button btnNextHole;
    Button btnPreviousHole;

    List<TableLayout> tableDiscGolf;

    //Custom Scrollviews
    ObservableHorizontalScrollView parHorizontalScrollView;
    ObservableHorizontalScrollView scoreHorizontalScrollView;
    ObservableScrollView nameScrollView;
    ObservableScrollView scoreScrollView;
    ObservableScrollView currentScoreSV;

    //Static in name implies a stationary view.
    //Dynimic in name implies a view wrapped in a custom scrollview.
    TableLayout scoreTable;
    TableLayout nameTable;
    TableLayout dynamicHeaderTable;
    TableLayout staticHeaderRowsTable;
    TableLayout currentScoreTable;
    TableLayout staticParCountTable;
    LinearLayout currentScoreLayout;

    //endregion

    //region Android Product Lifecycle

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTheme(R.style.NoActionBarTheme);
        setContentView(R.layout.runtime_game_layout);
        context = this;

        //region Setup View References

        btnIncrementScore = (Button)findViewById(R.id.btnIncrementScore);
        btnDecrementScore = (Button)findViewById(R.id.btnDecrementScore);
        btnNextHole = (Button)findViewById(R.id.btnNextHole);
        btnPreviousHole = (Button) findViewById(R.id.btnPreviousHole);

        tableDiscGolf = new ArrayList<TableLayout>();
        scoreTable = (TableLayout) findViewById(R.id.scoreTable);
        nameTable = (TableLayout) findViewById(R.id.nameTable);
        dynamicHeaderTable = (TableLayout) findViewById(R.id.parTable);
        staticHeaderRowsTable = (TableLayout) findViewById(R.id.parCellTable);
        staticParCountTable = (TableLayout) findViewById(R.id.staticParCountTable);
        currentScoreTable = (TableLayout) findViewById(R.id.currentScoreTable);
        currentScoreLayout = (LinearLayout) findViewById(R.id.currentScoreLayout);

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
            scoreCard = new ScoreCard(players, course);
            generateTables(players, course.getHoleCount());

            //todo IMPLEMENT current score per player
            //tvCurrentTotal.setText(String.valueOf(players[0].getCurrentTotal()));

        }

        //If on recreation, pull out the saved state information and reset the object
        //to the previous state.
        if (savedInstanceState != null) {
            players = (Player[])savedInstanceState.getSerializable("RestorePlayers");
            scoreCard = (ScoreCard) savedInstanceState.getSerializable("RestoreScoreCard");
            generateTables(players, course.getHoleCount());
        }

        //initially set block to color red

        TableRow tr = (TableRow) scoreTable.getChildAt(scoreCard.getCurrentPlayerSelected());
        TextView tv = (TextView) tr.getChildAt(scoreCard.getCurrentHole()-1);
        ((GradientDrawable)tv.getBackground()).setColor(Color.RED);
        TextView tv2 = (TextView) tr.getChildAt(scoreCard.getCurrentHole());
        ((GradientDrawable)tv2.getBackground()).setColor(Color.WHITE);

    }

    //endregion

    private void generateTables(Player[] players, int courseHoleCount){

        //Setup Static cells (Par, Hole#)
        setupStaticHeaderRows();

        //Setup Static Header Par and Holes cells
        String[] input = {"T", String.valueOf(course.getCoursePar())};
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
        players[scoreCard.getCurrentPlayerSelected()].IncrementCurrentScore(scoreCard.getCurrentHole()); //TODO replace 0 with current player selected
        generateScoreTable(players, course.getHoleCount());
        setupCurrentScoreColumn(players);

        TableRow tr = (TableRow) scoreTable.getChildAt(scoreCard.getCurrentPlayerSelected());

        TextView tv = (TextView) tr.getChildAt(scoreCard.getCurrentHole()-1);

       ((GradientDrawable)tv.getBackground()).setColor(Color.RED);

        if (scoreCard.getCurrentHole()-2 < 0) {
            TextView tv2 = (TextView) tr.getChildAt(scoreCard.getCurrentHole());
            ((GradientDrawable)tv2.getBackground()).setColor(Color.WHITE);
        }
        else {
            TextView tv2 = (TextView) tr.getChildAt(scoreCard.getCurrentHole() - 2);
            ((GradientDrawable)tv2.getBackground()).setColor(Color.WHITE);
        }


    }
    public void OnDecrementScoreClick(View v){

        scoreCard.getPlayerArray()[scoreCard.getCurrentPlayerSelected()].DecrementCurrentScore(scoreCard.getCurrentHole());
        generateScoreTable(players, course.getHoleCount());
        setupCurrentScoreColumn(players);



        TableRow tr = (TableRow) scoreTable.getChildAt(scoreCard.getCurrentPlayerSelected());

        TextView tv = (TextView) tr.getChildAt(scoreCard.getCurrentHole()-1);
        ((GradientDrawable)tv.getBackground()).setColor(Color.RED);

        if (scoreCard.getCurrentHole()-2 < 0) {
            TextView tv2 = (TextView) tr.getChildAt(scoreCard.getCurrentHole());
            ((GradientDrawable)tv2.getBackground()).setColor(Color.WHITE);
        }
        else {
            TextView tv2 = (TextView) tr.getChildAt(scoreCard.getCurrentHole() - 2);
            ((GradientDrawable)tv2.getBackground()).setColor(Color.WHITE);
        }

    }
    public void OnNextHoleClick(View v){
        scoreCard.NextHole();

        TableRow tr = (TableRow) scoreTable.getChildAt(scoreCard.getCurrentPlayerSelected());
        TextView tv = (TextView) tr.getChildAt(scoreCard.getCurrentHole()-1);
        ((GradientDrawable)tv.getBackground()).setColor(Color.RED);
        TextView tv2 = (TextView) tr.getChildAt(scoreCard.getCurrentHole()-2);
        ((GradientDrawable)tv2.getBackground()).setColor(Color.WHITE);

        tv.getParent().requestChildFocus(tv,tv);


    }
    public void OnPreviousHoleClick(View v){
        scoreCard.PreviousHole();

        TableRow tr = (TableRow) scoreTable.getChildAt(scoreCard.getCurrentPlayerSelected());
        TextView tv = (TextView) tr.getChildAt(scoreCard.getCurrentHole()-1);
        ((GradientDrawable)tv.getBackground()).setColor(Color.RED);
        TextView tv2 = (TextView) tr.getChildAt(scoreCard.getCurrentHole());
        ((GradientDrawable)tv2.getBackground()).setColor(Color.WHITE);

        tv.getParent().requestChildFocus(tv,tv);

    }

    public void OnLastPlayerClick(View view) {
        if (scoreCard.getCurrentPlayerSelected() > 0 ){
            scoreCard.LastPlayer();
        }
    }

    public void OnNxtPlayerClick(View view) {
        if (scoreCard.getCurrentPlayerSelected() < players.length -1){
            scoreCard.NextPlayer();
        }
    }

    //endregion

    //region Private Helper Methods

    private void retrieveGameData(){
        Bundle b = this.getIntent().getExtras();
        if (b != null){
            players = toPlayerArray((ArrayList<Player>) b.getSerializable("Players"));
            course = (Course) b.getSerializable("Course");

            if (course != null && players != null){
                Toast.makeText(this,"SUCCESS!",Toast.LENGTH_LONG).show();
            }
        }
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

    private TextView setupTextViewInTable(String setText, int resourceID ){


        TextView tv = new TextView(context);
        tv.setLayoutParams(new TableRow.LayoutParams(TEXT_WIDTH,
                TableRow.LayoutParams.MATCH_PARENT));
        tv.setBackgroundResource(resourceID);
        tv.setTextColor(Color.BLACK);
        tv.setTextSize(TEXT_SIZE);
        tv.setPadding(5, 5, 5, 5);
        tv.setText(setText);
        return tv;
    }

    //Overloaded setup Text View to add ability to change layout params
    private TextView setupTextViewInTable(String setText, int layoutWidthParam, int resourceID ){
        TextView tv = new TextView(context);
        tv.setLayoutParams(new TableRow.LayoutParams(layoutWidthParam,
                TableRow.LayoutParams.MATCH_PARENT));
        tv.setBackgroundResource(resourceID);
        tv.setTextColor(Color.BLACK);
        tv.setTextSize(TEXT_SIZE);
        tv.setPadding(5, 5, 5, 5);
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
            row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            // inner for loop
            for (int j = 1; j <= courseHoleCount; j++) {
                TextView tv = setupTextViewInTable(String.valueOf(players[i-1].getScore()[j-1]), R.drawable.cell_shape);
                row.addView(tv);
            }
            scoreTable.addView(row);
        }
    }

    private void setupStaticHeaderRows(){
        if (staticHeaderRowsTable.getChildCount() < 1) {
            String[] textInput = {"Hole", "Par"};
            for (int i = 0; i < textInput.length; i++) {

                LinearLayout tableRow = new LinearLayout(context); //Must me linear layout, since TableRow won't actually fill parent.
                TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 1f);
                tableRow.setLayoutParams(params);

                TextView tv = setupTextViewInTable(textInput[i],
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
                        new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 1f);
                tr.setLayoutParams(params);

                TextView tv = setupTextViewInTable(textArray[i], TableRow.LayoutParams.WRAP_CONTENT, R.drawable.cell_shape_light_green);
                if (i == 0){
                    tv.setTypeface(null, Typeface.BOLD);
                }
                tv.setGravity(Gravity.CENTER);

                tr.addView(tv);
                staticParCountTable.addView(tr);
            }
        }
    }

    private void setupDynamicHeaderTable() {
        if (dynamicHeaderTable.getChildCount() < 1){

            //Setup Hole# row
            LinearLayout holeNum = new LinearLayout(this);
            TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT,1f);
            holeNum.setLayoutParams(params);

            for (int a = 0; a<course.getCurrentHolePar().length; a++){
                TextView tv = setupTextViewInTable(String.valueOf(a+1),TEXT_WIDTH,R.drawable.cell_shape_light_green);
                tv.setGravity(Gravity.CENTER);
                tv.setTypeface(null, Typeface.BOLD);
                holeNum.addView(tv);
            }
            dynamicHeaderTable.addView(holeNum);

            //setup par row
            LinearLayout parNum = new LinearLayout(this);
            parNum.setLayoutParams(params);

            for (int a = 0; a<course.getCurrentHolePar().length; a++){
                TextView tv = setupTextViewInTable(String.valueOf(course.getCurrentHolePar()[a]),TEXT_WIDTH,R.drawable.cell_shape_light_green);
                tv.setGravity(Gravity.CENTER);
                parNum.addView(tv);
            }
            dynamicHeaderTable.addView(parNum);


        }
    }

    private void setupCurrentScoreColumn(Player[] players) {

        currentScoreTable.removeAllViews();
            for (int count = 0; count < players.length; count++){
                LinearLayout _linearLayout = new LinearLayout(context);
                TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT,1f);
                _linearLayout.setLayoutParams(params);

                TextView tv = setupTextViewInTable(String.valueOf(players[count].getCurrentTotal()), R.drawable.cell_shape);
                tv.setSingleLine();
                _linearLayout.addView(tv);
                currentScoreTable.addView(_linearLayout);
            }

    }

    private void setupNameColumn(Player[] players) {
        if (nameTable.getChildCount() < 1){
            for (int z = 0; z < players.length; z++) {
                LinearLayout linearLayout = new LinearLayout(this);
                TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT,1f);
                linearLayout.setLayoutParams(params);

                TextView tv = setupTextViewInTable(players[z].getName(),TableRow.LayoutParams.MATCH_PARENT, R.drawable.cell_shape); //This forced width but the next line should be a quick fix for it.
                tv.setSingleLine();
                linearLayout.addView(tv);

                nameTable.addView(linearLayout);
            }
        }
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

    //endregion
}
