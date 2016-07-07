package com.example.michael.discgolfapp.Model;

/**
 * Created by Michael on 5/20/2016.
 */
public class ScoreCard {

    //region Private Fields

    private Player[] players;
    private int currentHole;
    private Course course;

    //endregion

    //region Constructors

    public ScoreCard(Player[] players, Course course)
    {
        this.players = players;
        this.course = course;
        currentHole = 1;
    }

    //endregion

    //region Getters and Setters

    public int getCurrentHole(){
        return currentHole;
    }

    public void setCurrentHole(int value){
        if (isWithinCourseConstraints(value)){
            currentHole = value;
        }
    }

    public Player[] getPlayerArray(){
        return players;
    }

    public void setPlayerArray(Player[] value){
        players = value;
    }

    public int gerPlayersCount(){
        return players.length;
    }

    //endregion

    //region Public Methods

    public void NextHole()
    {
        if (currentHole < course.getHoleCount())
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

    //endregion

    //region Private Helper Methods

    private boolean isWithinCourseConstraints(int value) {
        return (value >= 1 && value <= course.getHoleCount());
    }

    //endregion

}
