package com.manleysoftware.michael.discgolfapp.data;

import android.content.Context;

import com.manleysoftware.michael.discgolfapp.data.Model.Scorecard;

import java.util.List;

public interface ScorecardRepository extends Repository<Scorecard> {
    List<Scorecard>  findAllFinishedScorecards();
    List<Scorecard> findAllUnfinishedScorecards();
    List<Scorecard> getAllScorecards();
    void deleteAllFinishedScorecards(Context context);
    void deleteAllUnfinishedScorecards(Context context);
}
