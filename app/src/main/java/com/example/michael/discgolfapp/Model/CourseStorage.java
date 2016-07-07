package com.example.michael.discgolfapp.Model;

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

    //endregion
}
