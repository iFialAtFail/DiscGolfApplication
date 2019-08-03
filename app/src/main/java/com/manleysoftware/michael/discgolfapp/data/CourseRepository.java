package com.manleysoftware.michael.discgolfapp.data;

import android.content.Context;

import com.manleysoftware.michael.discgolfapp.Application.CourseExistsAlreadyException;
import com.manleysoftware.michael.discgolfapp.data.Model.Course;

import java.util.List;

public interface CourseRepository extends Repository<Course> {
    List<Course> getAllCourses();
}
