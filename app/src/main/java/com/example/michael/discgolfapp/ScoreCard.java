package com.example.michael.discgolfapp;

/**
 * Created by Michael on 5/20/2016.
 */
public class ScoreCard extends DiscGolfGameManager {

    private int[] player;
    private String date;
    private int currentHoleSelected= 0;


    public ScoreCard(Course courseLength){
        try {
            player = new  int[courseLength.getHoleLength()];
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
        if (player != null){
            player[currentHoleSelected]++;
        }
    }
    protected void ReduceStroke(){
        if (player != null){
            player[currentHoleSelected]--;
        }
    }
}
