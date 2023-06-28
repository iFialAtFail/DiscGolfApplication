package com.manleysoftware.michael.discgolfapp.ui.main;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.manleysoftware.michael.discgolfapp.data.CourseRepository;
import com.manleysoftware.michael.discgolfapp.domain.Course;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class CoursePickerViewModel extends ViewModel {

    protected CourseRepository courseRepository;

    @Inject
    public CoursePickerViewModel(CourseRepository courseRepository){
        this.courseRepository = courseRepository;
        this.courses.setValue(courseRepository.getAllCourses());
    }

    private final MutableLiveData<List<Course>> courses =
            new MutableLiveData<>();
    public LiveData<List<Course>> getCourses() {
        return courses;
    }

    public int getCountOfCourses() {
        if (courses == null || courses.getValue() == null) return 0;
        return courses.getValue().size();
    }

    public void delete(Course courseToDelete, Context context) {
        courseRepository.delete(courseToDelete, context);
        courses.setValue(courseRepository.getAllCourses());
    }
}