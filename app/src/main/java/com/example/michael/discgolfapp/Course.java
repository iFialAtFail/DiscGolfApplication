package com.example.michael.discgolfapp;

/**
 * Created by Michael on 5/21/2016.
 */
public class Course {

    private int[] holes;
    private String courseName;





    public Course(String courseName, int numHoles)
    {
        if (isValidCoursename(courseName))
        {
            this.courseName = courseName;
        }
        else
        {
            //TODO Implement java exception
            //throw new ArgumentException("Invalid input for course name. AlphaNumeric characters and spaces only.");
        }

        if (numHoles > 0)
        {
            holes = new int[numHoles];
        }
        else
        {   //todo implement java exception
            //throw new ArgumentOutOfRangeException("Amount of holes per course must be greater than one.");
        }

        initializeCoursePars();
    }

    public String getCourseName(){
        return courseName;
    }

    public void setCourseName(String value){
        if (isValidCoursename(value)){
            courseName = value;
        }
        else {
            //TODO implement java exception
        }
    }

    public int getNumberOfHoles(){
        return holes.length;
    }

    public int[] getCurrentHolePar(){
        return holes;
    }




    private void initializeCoursePars()
    {
        for (int i = 0; i < holes.length; i++)
        {
            holes[i] = 3;
        }
    }

    private boolean isValidCoursename(String nameInput)
    {
        char[] charArray = nameInput.toCharArray();


        for(char letter : charArray)
        {
            if (Character.isLetterOrDigit(letter) || letter == ' ')
            {
                //Let it keep iterating through the characters.
            }
            else { return false; }
        }
        return true;
    }

    public void IncrementHolePar(int currentHole)
    {
        holes[currentHole]++;
    }

    public void DecrementHolePar(int currentHole)
    {
        if (holes[currentHole] <= 1)
        {
            //Do nothing, this is the bottom limit
        }
        else
        {
            holes[currentHole]--;
        }

    }



}
