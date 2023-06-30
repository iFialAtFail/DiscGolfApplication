package com.manleysoftware.michael.discgolfapp.ui.main.ui.course;

import static com.manleysoftware.michael.discgolfapp.domain.Course.MAX_HOLE_COUNT;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class NewCourseViewModel extends ViewModel {

    public static final int DEFAULT_PAR_VALUE = 3;
    public static final int DEFAULT_HOLE_COUNT = 18;
    public static final int MIN_HOLE_COUNT = 1;

    MutableLiveData<List<Integer>> holeList = new MutableLiveData<>();
    MutableLiveData<Integer> holeCount = new MutableLiveData<>();

    public NewCourseViewModel() {
        List<Integer> defaultHoleList = createDefaultHoleList();
        holeList.setValue(defaultHoleList);
        holeCount.setValue(DEFAULT_HOLE_COUNT);
    }

    public List<Integer> createDefaultHoleList() {
        return IntStream.range(0, DEFAULT_HOLE_COUNT).mapToObj(i -> 3)
                .collect(Collectors.toCollection(() -> new ArrayList<>(DEFAULT_HOLE_COUNT)));
    }

    protected void incrementViewHoleCount() {
        if (holeCount.getValue() >= MAX_HOLE_COUNT) {
            return;
        }
        holeCount.setValue(holeCount.getValue() + 1);
        List<Integer> holes = holeList.getValue();
        holes.add(DEFAULT_PAR_VALUE);
        holeList.setValue(holes);
    }

    protected void decrementHoleCount() {
        if (holeCount.getValue() > MIN_HOLE_COUNT) {
            holeCount.setValue(holeCount.getValue() - 1);
            List<Integer> holes = holeList.getValue();
            holes.remove(holes.size() - 1);
            holeList.setValue(holes);
        }
    }
}
