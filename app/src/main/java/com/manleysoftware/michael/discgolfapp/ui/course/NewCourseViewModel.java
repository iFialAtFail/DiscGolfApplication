package com.manleysoftware.michael.discgolfapp.ui.course;

import static com.manleysoftware.michael.discgolfapp.domain.Course.MAX_HOLE_COUNT;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.manleysoftware.michael.discgolfapp.application.AlreadyExistsException;
import com.manleysoftware.michael.discgolfapp.data.CourseRepository;
import com.manleysoftware.michael.discgolfapp.domain.Course;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class NewCourseViewModel extends ViewModel {

    public static final int DEFAULT_PAR_VALUE = 3;
    public static final int DEFAULT_HOLE_COUNT = 18;
    public static final int MIN_HOLE_COUNT = 1;

    protected CourseRepository courseRepository;

    @Inject
    public NewCourseViewModel(CourseRepository courseRepository){
        List<Integer> defaultHoleList = createDefaultHoleList();
        holeList.setValue(defaultHoleList);
        holeCount.setValue(DEFAULT_HOLE_COUNT);
        this.courseRepository = courseRepository;
    }

    MutableLiveData<List<Integer>> holeList = new MutableLiveData<>();
    MutableLiveData<Integer> holeCount = new MutableLiveData<>();

    public List<Integer> createDefaultHoleList() {
        return IntStream.range(0, DEFAULT_HOLE_COUNT).mapToObj(i -> 3)
                .collect(Collectors.toCollection(() -> new ArrayList<>(DEFAULT_HOLE_COUNT)));
    }

    protected void incrementViewHoleCount() {
        if (holeCount.getValue() == null || holeCount.getValue() >= MAX_HOLE_COUNT) {
            return;
        }
        holeCount.setValue(holeCount.getValue() + 1);
        List<Integer> holes = holeList.getValue();
        if (holes == null) throw new IllegalStateException();
        holes.add(DEFAULT_PAR_VALUE);
        holeList.setValue(holes);
    }

    protected void decrementHoleCount() {
        if (holeCount.getValue() == null) return;
        if (holeCount.getValue() > MIN_HOLE_COUNT) {
            holeCount.setValue(holeCount.getValue() - 1);
            List<Integer> holes = holeList.getValue();
            if (holes == null) return;
            holes.remove(holes.size() - 1);
            holeList.setValue(holes);
        }
    }

    public void createCourse(String rawNameInput, Context context) throws InvalidCourseNameException, AlreadyExistsException {
        String nameInput = rawNameInput.trim();
        validateCourseName(nameInput);
        if (holeList.getValue() == null) throw new IllegalStateException("Hole list was null.");
        int[] populatedPars = toIntArray(holeList.getValue());
        Course course = new Course(nameInput, populatedPars);
        courseRepository.add(course, context);
    }

    private void validateCourseName(String nameInput) throws InvalidCourseNameException {
        if (!Course.isValidCoursename(nameInput)) {
            throw new InvalidCourseNameException();
        }
    }

    private int[] toIntArray(List<Integer> list) {
        int[] ret = new int[list.size()];
        for (int i = 0; i < ret.length; i++)
            ret[i] = list.get(i);
        return ret;
    }
}
