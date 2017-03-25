package com.manleysoftware.michael.discgolfapp.Model;

import java.io.Serializable;

/**
 * Created by Michael on 5/21/2016.
 */
public class Course implements Serializable{

    //region private fields
    private int[] holes;
    private String courseName;
    //endregion

    //region Constructors

    public Course(String courseName, int numHoles){

        validateCourseName(courseName);
        validateHoles(numHoles);
        initializeCoursePars(); //Set all holes to generic par 3.
    }

    public Course (String courseName, int[] prePopulatedPars){

        validateCourseName(courseName);

        validatePrePopulatedParValues(prePopulatedPars);
    }



    //endregion

    //region Getters and Setters

    public String getName(){
        return courseName;
    }

    public void setName(String value){
        validateCourseName(value);
    }

    public int getHoleCount(){
        return holes.length;
    }

    public int[] getParArray(){
        return holes;
    }

	public void setParArray(int[] parArray) {
		this.holes = parArray;
	}

    public int getParTotal(){
        int totalPars = 0;
        for (int par : holes) {
            totalPars += par;
        }
        return totalPars;
    }


    //endregion

    //region Private Helper Methods

    private void initializeCoursePars()
    {
        for (int i = 0; i < holes.length; i++)
        {
            holes[i] = 3;
        }
    }

    private boolean isValidParArray(int[] prePopulatedPars) {
        for (int indexer : prePopulatedPars){
            if (indexer <2){
                return false;
            }
        }
        return true;
    }

    public static boolean isValidCoursename(String nameInput)
    {
        nameInput = nameInput.trim();
        if (nameInput.isEmpty()){
            return false;
        }

        //Convert to char array and iterate through
        //characters to ensure no special characters.
        char[] charArray = nameInput.toCharArray();

        for(char letter : charArray)
        {
            if (Character.isLetterOrDigit(letter) || letter == ' ')
            {
                //Let it keep iterating through the characters.
            }
            else { return false; } //Break the cycle
        }
        return true; //Continue if nothing was out of place.
    }

    private void validateCourseName(String courseName) {

        if (isValidCoursename(courseName))
        {
            this.courseName = courseName;
        }
        else
        {
            throw new IllegalArgumentException("Must be only AlphaNumeric characters.");
        }
    }

    private void validateHoles(int numHoles) {

        if (numHoles > 0) {
            holes = new int[numHoles];
        }

        else {
            throw new IllegalArgumentException("Holes must be greater than 0.");
        }
    }

    private void validatePrePopulatedParValues(int[] prePopulatedPars) {

        if (prePopulatedPars.length > 0 && isValidParArray(prePopulatedPars)){
            holes = prePopulatedPars;
        }
    }

    //endregion

    //region Public Methods
    public void IncrementHolePar(int currentHole)
    {
        holes[currentHole]++;
    }

    public void DecrementHolePar(int currentHole)
    {
        if (holes[currentHole] > 1) {

            holes[currentHole]--;
        }

    }

    //endregion

    //region Overrides

    @Override
    public String toString() {
        return courseName;
    }

    @Override
    public boolean equals(Object o) {
		return (o instanceof Course) && (((Course) o).getName().equals(this.getName()));
    }



	//endregion
}
