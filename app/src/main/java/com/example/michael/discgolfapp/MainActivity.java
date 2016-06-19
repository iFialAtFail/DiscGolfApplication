package com.example.michael.discgolfapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Course courseBigRapids;
    Player mike;
    Player[] players;
    ScoreCard newGame;

    Button btnIncrementScore;
    Button btnDecrementScore;
    Button btnNextHole;
    Button btnPreviousHole;

    TextView tvName;

    TextView tvHole;
    TextView tvHole1;
    TextView tvHole2;
    TextView tvHole3;
    TextView tvHole4;
    TextView tvHole5;
    TextView tvHole6;
    TextView tvHole7;
    TextView tvHole8;
    TextView tvHole9;
    TextView tvHole10;
    TextView tvHole11;
    TextView tvHole12;
    TextView tvHole13;
    TextView tvHole14;
    TextView tvHole15;
    TextView tvHole16;
    TextView tvHole17;


    TextView[] tvArray;


    Integer incrementedInteger = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnIncrementScore = (Button)findViewById(R.id.btnIncrementScore);
        btnDecrementScore = (Button)findViewById(R.id.btnDecrementScore);
        btnNextHole = (Button)findViewById(R.id.btnNextHole);
        btnPreviousHole = (Button) findViewById(R.id.btnPreviousHole);

        tvName = (TextView) findViewById(R.id.tvName);


        tvHole = (TextView)findViewById(R.id.textView);
        tvHole1 = (TextView)findViewById(R.id.textView1);
        tvHole2 = (TextView)findViewById(R.id.textView2);
        tvHole3 = (TextView)findViewById(R.id.textView3);
        tvHole4 = (TextView)findViewById(R.id.textView4);
        tvHole5 = (TextView)findViewById(R.id.textView5);
        tvHole6 = (TextView)findViewById(R.id.textView6);
        tvHole7 = (TextView)findViewById(R.id.textView7);
        tvHole8 = (TextView)findViewById(R.id.textView8);
        tvHole9 = (TextView)findViewById(R.id.textView9);
        tvHole10 = (TextView)findViewById(R.id.textView10);
        tvHole11 = (TextView)findViewById(R.id.textView11);
        tvHole12 = (TextView)findViewById(R.id.textView12);
        tvHole13 = (TextView)findViewById(R.id.textView13);
        tvHole14 = (TextView)findViewById(R.id.textView14);
        tvHole15 = (TextView)findViewById(R.id.textView15);
        tvHole16 = (TextView)findViewById(R.id.textView16);
        tvHole17 = (TextView)findViewById(R.id.textView17);

        if (savedInstanceState == null) {

            courseBigRapids = new Course("Big Rapids",18);
            mike = new Player("Mike",courseBigRapids);
            players = new Player[]{mike};
            newGame  = new ScoreCard(players,courseBigRapids);
            generateTable();

        }
        else {
            updateTable();
        }
    }

    private void generateTable() {
        if (tvArray == null){
            tvArray = new TextView[]{
                    tvHole,
                    tvHole1,
                    tvHole2,
                    tvHole3,
                    tvHole4,
                    tvHole5,
                    tvHole6,
                    tvHole7,
                    tvHole8,
                    tvHole9,
                    tvHole10,
                    tvHole11,
                    tvHole12,
                    tvHole13,
                    tvHole14,
                    tvHole15,
                    tvHole16,
                    tvHole17,
            };
            for (int j = 0; j < courseBigRapids.getNumberOfHoles(); j++){
                String temp = String.valueOf(newGame.getPlayers()[0].getScore()[j]);
                tvArray[j].setText(temp);
            }

            if (tvName != null){
                tvName.setText(newGame.getPlayers()[0].getPlayerName());
            }

        }
    }

    private void updateTable(){
        if (tvArray != null){
            for (int j = 0; j < courseBigRapids.getNumberOfHoles(); j++){
                tvArray[j].setText(String.valueOf(newGame.getPlayers()[0].getScore()[j]));
            }
        }
    }

    public void OnIncrementScoreClick(View v){
        newGame.getPlayers()[0].IncrementCurrentScore(newGame.getCurrentHole());
        updateTable();
    }
    public void OnDecrementScoreClick(View v){
        newGame.getPlayers()[0].DecrementCurrentScore(newGame.getCurrentHole());
        updateTable();
    }
    public void OnNextHoleClick(View v){
        newGame.NextHole();
        updateTable();
    }
    public void OnPreviousHoleClick(View v){
        newGame.PreviousHole();
        updateTable();
    }


}
