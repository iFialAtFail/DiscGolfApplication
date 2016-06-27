package com.example.michael.discgolfapp.Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Michael on 6/27/2016.
 */
public class CourseStorage implements Serializable {
    List<Course> courseStorage;

    //region Constructors

    public CourseStorage(){
        courseStorage = new ArrayList<Course>();
    }

    public CourseStorage(Course[] arrayOfCourses){
        courseStorage = new ArrayList<Course>();

        for (Course course : arrayOfCourses){
            courseStorage.add(course);
        }
    }

    public CourseStorage(ArrayList<Course> listOfCourses){
        courseStorage  = new ArrayList<Course>(listOfCourses);
    }

    //endregion

    public List<Course> getCourseStorage() {
        return courseStorage;
    }

    public void setCourseStorage(List<Course> courseStorage) {
        this.courseStorage = courseStorage;
    }

    public int getNumberOfStoredCourses(){
        if (!courseStorage.isEmpty()){
            return courseStorage.size();
        }
        return  0;
    }
}
