package com.example.michael.discgolfapp;

/**
 * Created by Michael on 5/21/2016.
 */
public class Course {
    private int[] courseHoleArray;
    private String courseName;

    public Course(String courseName, int numberOfHoles){
        this.courseName = courseName;
        courseHoleArray = new int[numberOfHoles];
        initializeGenericCoursePar();

    }

    private void initializeGenericCoursePar(){
        if (courseHoleArray != null){
            for (int i : courseHoleArray){
                i = 3;
            }
        }
    }

    public String getCourseName(){
        return courseName;
    }

    public void setCourseName(String name){

         courseName = name;
    }

    public int getHoleLength(){
        int lengthOfCourse = -1;
        if (courseHoleArray != null){
            lengthOfCourse = courseHoleArray.length;
        }
        return lengthOfCourse;
    }

    public int getCurrentHolePar(int currentHole){
        return courseHoleArray[currentHole];
    }
}
