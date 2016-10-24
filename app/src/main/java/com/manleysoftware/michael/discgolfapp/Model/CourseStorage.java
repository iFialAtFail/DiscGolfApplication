package com.manleysoftware.michael.discgolfapp.Model;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Michael on 6/27/2016.
 */
public class CourseStorage implements Serializable {

    //region Private Fields

    private static final long serialVersionUID = 2L;
    private static final String COURSE_STORAGE_FILE = "courseList.data";
    private List<Course> courseStorage;

    //endregion

    //region Constructors

    public CourseStorage(){
        courseStorage = new ArrayList<Course>();
    }

    public CourseStorage(Course[] arrayOfCourses){
        courseStorage = new ArrayList<Course>();
        courseStorage.addAll(Arrays.asList(arrayOfCourses));
    }

    public CourseStorage(ArrayList<Course> listOfCourses){
        courseStorage  = new ArrayList<Course>(listOfCourses);
    }

    //endregion

    //region Getters and Setters

    public List<Course> getCourseStorage() {
        return courseStorage;
    }

    public void setCourseStorage(List<Course> courseStorage) {
        this.courseStorage = courseStorage;
    }

    public int getStoredCoursesCount(){
        if (!courseStorage.isEmpty()){
            return courseStorage.size();
        }
        return  0;
    }

    //endregion

    //region Public Methods

    public void AddCourseToStorage(Course course){
        for (Course c : courseStorage){
            if (course.getName().equals(c.getName())){
                return;
            }
        }

        courseStorage.add(course);
    }

    public void DeleteCourseFromStorage(int position){
        courseStorage.remove(position);
    }

    public void DeleteCourseFromStorage(Course course){
        courseStorage.remove(course);
    }

    public static CourseStorage LoadFromFile(Context context){
        CourseStorage retrieve;
        try {
            // Read from disk using FileInputStream
            FileInputStream fis = context.openFileInput(COURSE_STORAGE_FILE);

            // Read object using ObjectInputStream
            ObjectInputStream ois =
                    new ObjectInputStream (fis);

            // Read an object
            Object obj = ois.readObject();

            if (obj instanceof CourseStorage)
            {
                retrieve = (CourseStorage) obj;
                return retrieve;
            }

        }catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }

    public boolean SaveToFile(Context context){
        try {
            // Write to disk with FileOutputStream
            FileOutputStream fos = context.openFileOutput(COURSE_STORAGE_FILE, Context.MODE_PRIVATE);

            // Write object with ObjectOutputStream
            ObjectOutputStream oos = new
                    ObjectOutputStream (fos);

            // Write object out to disk
            oos.writeObject ( this );

            oos.close();
            fos.close();

        } catch(Exception ex){
            ex.printStackTrace();
            return false;
        }

        return true;
    }

    //endregion
}
