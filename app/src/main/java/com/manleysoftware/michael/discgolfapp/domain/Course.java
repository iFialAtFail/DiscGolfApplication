package com.manleysoftware.michael.discgolfapp.domain;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Created by Michael on 5/21/2016.
 */
public class Course implements Serializable {

    public static final int MAX_HOLE_COUNT = 64;

    private int[] holes;

    @NonNull
    private String courseName;

    public Course(String courseName, int numHoles) {
        validateCourseName(courseName);
        validateHoleCount(numHoles);
        initializeCoursePars(); //Set all holes to generic par 3.
    }

    public Course(String courseName, int[] prePopulatedPars) {
        validateCourseName(courseName);
        validatePrePopulatedParValues(prePopulatedPars);
    }

    public String getName() {
        return courseName;
    }

    public void setName(String value) {
        validateCourseName(value);
    }

    public int getHoleCount() {
        return holes.length;
    }

    public int[] getParArray() {
        return holes;
    }

    public void setParArray(int[] parArray) {
        this.holes = parArray;
    }

    public int getParTotal() {
        return Arrays.stream(holes).sum();
    }

    private void initializeCoursePars() {
        Arrays.fill(holes, 3);
    }

    private boolean isValidParArray(int[] prePopulatedPars) {
        return Arrays.stream(prePopulatedPars).noneMatch(i -> i < 2);
    }

    public static boolean isValidCoursename(String nameInput) {
        nameInput = nameInput.trim();
        if (nameInput.isEmpty()) {
            return false;
        }

        //Convert to char array and iterate through
        //characters to ensure no special characters.
        char[] charArray = nameInput.toCharArray();

        for (char letter : charArray) {
            if (Character.isLetterOrDigit(letter) || letter == ' ') {
                //Let it keep iterating through the characters.
            } else {
                return false;
            } //Break the cycle
        }
        return true; //Continue if nothing was out of place.
    }

    private void validateCourseName(String courseName) {
        if (isValidCoursename(courseName)) {
            this.courseName = courseName;
        } else {
            throw new IllegalArgumentException("Must be only AlphaNumeric characters.");
        }
    }

    private void validateHoleCount(int numHoles) {
        if (numHoles <= 0) {
            throw new IllegalArgumentException("Holes must be greater than 0.");
        }
        holes = new int[numHoles];
    }

    private void validatePrePopulatedParValues(int[] prePopulatedPars) {
        if (prePopulatedPars.length > 0 && isValidParArray(prePopulatedPars)) {
            holes = prePopulatedPars;
        }
    }

    @Override
    public String toString() {
        return courseName;
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof Course) && (((Course) o).getName().equals(this.getName()));
    }

}
