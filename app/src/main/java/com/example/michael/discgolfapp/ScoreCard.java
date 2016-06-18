package com.example.michael.discgolfapp;

/**
 * Created by Michael on 5/20/2016.
 */
public class ScoreCard extends DiscGolfGameManager {

    private Player[] players;
    private static int currentHole;
    private Course course;



    public ScoreCard(Player[] players, Course course)
    {
        this.players = players;
        this.course = course;
        currentHole = 1;
    }


    public int getCurrentHole(){
        return currentHole;
    }

    public Player[] getPlayers(){
        return players;
    }
    public void setPlayers(Player[] value){
        players = value;
    }

    public int getNumberOfPlayers(){
        return players.length;
    }

    public void NextHole()
    {
        if (currentHole < course.getNumberOfHoles())
        {
            currentHole++;
        }
    }

    public void PreviousHole()
    {
        if (currentHole > 1)
        {
            currentHole--;
        }
    }


}
