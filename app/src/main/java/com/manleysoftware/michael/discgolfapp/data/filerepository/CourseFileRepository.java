package com.manleysoftware.michael.discgolfapp.data.filerepository;

import android.content.Context;

import com.manleysoftware.michael.discgolfapp.application.AlreadyExistsException;
import com.manleysoftware.michael.discgolfapp.data.CourseRepository;
import com.manleysoftware.michael.discgolfapp.domain.Course;

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
public class CourseFileRepository implements CourseRepository, Serializable {

    //region Private Fields

    private static final long serialVersionUID = 2L;
    private static final String COURSE_STORAGE_FILE = "courseList.data";
    private List<Course> courses;

    //endregion

    //region Constructors

    public CourseFileRepository(Context context){
        CourseFileRepository repo = LoadFromFile(context);
        if (repo != null){
            courses = repo.getAllCourses();
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
    public List<Course> getAllCourses() {
        return courses;
    }

    //endregion

    //region Public Methods

    @Override
    public void add(Course course, Context context) throws AlreadyExistsException {
        if (isUniqueCourse(course)){
            courses.add(course);
            save(context);
        } else{
            throw new AlreadyExistsException();
        }

    }

    @Override
    public void update(Course entity, Context context) {
        if (isUniqueCourse(entity)){
            save(context);
        }
    }

    private boolean isUniqueCourse(Course course) {
        for (Course c : courses){
            if (course.getName().equals(c.getName())){
                return false;
            }
        }
        return true;
    }

    @Override
    public void delete(Course course, Context context){
        courses.remove(course);
        save(context);
    }

    @Override
    public Course findByPrimaryKey(Course template) {
        Course retval = null;
        for(Course course: courses){
            if (course.getName().equals(template.getName())){
                retval = course;
            }
        }
        return retval;
    }

    private static CourseFileRepository LoadFromFile(Context context){
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

    private boolean save(Context context){
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
