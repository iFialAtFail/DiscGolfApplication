package com.example.michael.discgolfapp;

/**
 * Created by Michael on 5/20/2016.
 */
public class ScoreCard extends DiscGolfGameManager {

    private String date;
    private Course currentCourse;
    private PlayerInfo[] playerInfos;
    private int frontNinePar;
    private int backNinePar;

    public ScoreCard(Course currentCourse, PlayerInfo[] playerInfos, String date){
        this.currentCourse = currentCourse;
        this.playerInfos = playerInfos;
        this.date = date;



    }

}
