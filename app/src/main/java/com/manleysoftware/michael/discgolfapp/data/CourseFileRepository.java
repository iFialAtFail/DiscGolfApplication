package com.manleysoftware.michael.discgolfapp.data;

import android.content.Context;

import com.manleysoftware.michael.discgolfapp.Model.Course;

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
public class CourseFileRepository implements CourseRepository {

    //region Private Fields

    private static final long serialVersionUID = 2L;
    private static final String COURSE_STORAGE_FILE = "courseList.data";
    private List<Course> courses;

    //endregion

    //region Constructors

    public CourseFileRepository(Context context){
        CourseFileRepository repo = LoadFromFile(context);
        if (repo != null){
            courses = repo.getCourses();
        } else{
            courses = new ArrayList<>();
        }
    }

    public CourseFileRepository(Course[] arrayOfCourses){
        courses = new ArrayList<Course>();
        courses.addAll(Arrays.asList(arrayOfCourses));
    }

    public CourseFileRepository(ArrayList<Course> listOfCourses){
        courses = listOfCourses;
    }

    //endregion

    //region Getters and Setters

    @Override
    public List<Course> getCourses() {
        return courses;
    }

    //endregion

    //region Public Methods

    @Override
    public void addCourse(Course course){
        if (isUniqueCourse(course))
            courses.add(course);
    }

    private boolean isUniqueCourse(Course course) {
        for (Course c : courses){
            if (course.getName().equals(c.getName())){
                return true;
            }
        }
        return false;
    }

    @Override
    public void removeCourse(Course course){
        courses.remove(course);
    }

    public static CourseFileRepository LoadFromFile(Context context){
        CourseFileRepository retrieve;
        try {
            // Read from disk using FileInputStream
            FileInputStream fis = context.openFileInput(COURSE_STORAGE_FILE);

            // Read object using ObjectInputStream
            ObjectInputStream ois =
                    new ObjectInputStream (fis);

            // Read an object
            Object obj = ois.readObject();

            if (obj instanceof CourseFileRepository)
            {
                retrieve = (CourseFileRepository) obj;
                return retrieve;
            }

        }catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean Save(Context context){
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
