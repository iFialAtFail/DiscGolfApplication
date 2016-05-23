package com.example.michael.discgolfapp;

/**
 * Created by Michael on 5/22/2016.
 */
public class PlayerInfo {
    private String name;
    private int[] scores;

    //Record this value into the player data as well.
    private Course currentCourse;

    public PlayerInfo(String name, Course currentCourse){
        this.name = name;
        this.currentCourse = currentCourse;
        initializeScoresToPar(currentCourse);
    }

    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }

    public int getScoreForHole(int holeNumber){
        return scores[holeNumber];
    }

    public void IncrementScoreForHole(int holeNumber){
        scores[holeNumber] += 1;
    }
    public void DecrementScoreForHole(int holeNumber){
        scores[holeNumber] -= 1;
    }

    private void initializeScoresToPar(Course currentCourse){
        scores = currentCourse.getCourseHoleArray().clone();
    }

    //TODO add player previous game data, stack push/pop style
}
