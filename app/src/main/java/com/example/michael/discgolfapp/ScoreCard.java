package com.example.michael.discgolfapp;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Michael on 5/20/2016.
 */
public class ScoreCard extends DiscGolfGameManager {

    private int[] playerScoreCard;
    private String date;
    private int currentHoleSelected= 0;


    public ScoreCard(Course courseLength){
        try {
            playerScoreCard = new  int[courseLength.getHoleLength()];
        }
        catch(NegativeArraySizeException ex)
        {
            //TODO do this the right way.
            //catch the exception
        }

    }

    public Integer getCurrentHoleSelected() {
        return currentHoleSelected;
    }

    protected void NextHole(){
        if(currentHoleSelected != 18) currentHoleSelected++;
    }

    protected void PreviousHole(){
        if(currentHoleSelected != 0) currentHoleSelected--;
    }

    protected void AddStroke(){
        if (playerScoreCard != null){
            playerScoreCard[currentHoleSelected]++;
        }
    }
    protected void ReduceStroke(){
        if (playerScoreCard != null){
            playerScoreCard[currentHoleSelected]--;
        }
    }
}
