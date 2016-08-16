package com.example.michael.discgolfapp.Model;

import java.io.Serializable;

/**
 * Created by Michael on 5/20/2016.
 */
public class ScoreCard implements Serializable {

    //region Private Fields

    private Player[] players;
    private int currentHole;
    private int currentPlayerSelected;
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

    public int getPlayersCount(){
        return players.length;
    }

    public int getCurrentPlayerSelected(){
        return currentPlayerSelected;
    }

	public String getCourseName(){
		return course.getName();
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

    public void NextPlayer(){
        currentPlayerSelected++;
    }

    public void LastPlayer(){
        currentPlayerSelected--;
    }

    //endregion

    //region Private Helper Methods

    private boolean isWithinCourseConstraints(int value) {
        return (value >= 1 && value <= course.getHoleCount());
    }

    //endregion

}
