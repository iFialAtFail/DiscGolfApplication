package com.manleysoftware.michael.discgolfapp.domain;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Created by Michael on 5/22/2016.
 */
public class Player implements Serializable {

    private String name;

    private boolean gameStarted = false;
    private Course course = null;
    private int[] scores;

    private static final int maxScoreAllowable = 20;

    public Player(String name) {
        validatePlayerName(name);
    }

    public String getName() {
        return name;
    }

    public void setName(String value) {
        name = value;
    }

    public int[] getScores() {
        if (gameStarted) {
            return scores;
        }
        return new int[0];
    }

    public int getCurrentTotal() {
        if (!gameStarted) {
            return -1;
        }
        return Arrays.stream(scores).sum();
    }

    public int getCurrentParDifference(int par) {
        return getCurrentTotal() - par;
    }

    public void incrementScore(int currentHole) {
        if (!gameStarted) {
            return;
        }

        if (currentHole >= 1 && currentHole <= course.getHoleCount()) {
            if (scores[currentHole - 1] >= maxScoreAllowable) {
                return;
            }
            scores[currentHole - 1]++;
        }

    }

    public void decrementScore(int currentHole) {
        if (!gameStarted) {
            return;
        }

        if (currentHole >= 1 && currentHole <= course.getHoleCount()) {
            if (scores[currentHole - 1] > 1) {
                scores[currentHole - 1]--;
            }
        }

    }

    public void startGame(Course course) {
        if (this.course == null) {
            this.course = course;
            initializeScoreToCoursePars();
        }

        gameStarted = true;
    }

    public static boolean isValidPlayerName(String nameInput) {
        String name = nameInput.trim();

        if (name.isEmpty()) {
            return false;
        }

        char[] charArray = nameInput.toCharArray();

        for (char letter : charArray) {
            if (Character.isLetterOrDigit(letter) || letter == ' ') {
                //Let it keep iterating through the characters.
            } else {
                return false;
            }
        }

        return true;
    }

    private void initializeScoreToCoursePars() {
        if (course != null) {
            scores = course.getParArray().clone();
        }
    }


    private void validatePlayerName(String playerName) {
        if (isValidPlayerName(playerName)) {
            this.name = playerName;
        } else {
            throw new IllegalArgumentException("Invalid input for player name. AlphaNumeric characters and spaces only.");
        }
    }

    @Override
    public String toString() {
        return "Player{" +
                "name='" + name + '\'' +
                ", gameStarted=" + gameStarted +
                ", course=" + course +
                ", scores=" + Arrays.toString(scores) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof Player) && (((Player) o).getName().equals(this.getName()));
    }

    public int getScoreForHole(int selectedHole) {
        if (selectedHole > scores.length) {
            throw new IllegalArgumentException("Selected hole is out of bounds of players scores");
        }
        return scores[selectedHole];
    }


    //endregion

}
