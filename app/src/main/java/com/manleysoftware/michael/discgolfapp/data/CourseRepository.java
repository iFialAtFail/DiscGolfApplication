package com.manleysoftware.michael.discgolfapp.data;

import com.manleysoftware.michael.discgolfapp.domain.Course;

import java.util.List;

public interface CourseRepository extends Repository<Course> {
    List<Course> getAllCourses();
}
