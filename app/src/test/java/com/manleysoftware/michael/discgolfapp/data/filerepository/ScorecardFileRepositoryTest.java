package com.manleysoftware.michael.discgolfapp.data.filerepository;

import static org.junit.Assert.assertEquals;

import android.content.Context;

import com.manleysoftware.michael.discgolfapp.domain.Course;
import com.manleysoftware.michael.discgolfapp.domain.Player;
import com.manleysoftware.michael.discgolfapp.domain.Players;
import com.manleysoftware.michael.discgolfapp.domain.Scorecard;

import org.junit.Test;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class ScorecardFileRepositoryTest {

    private ScorecardFileRepository testling;

    @Test
    public void shouldFindAllFinishedScorecards() {
        List<Scorecard> scorecards = List.of(
                getGenericScorecardForPlayerWithName("Mike"),
                getGenericScorecardForPlayerWithName("Brian")
        );
        scorecards.get(0).setArchived();
        testling = new ScorecardFileRepository(scorecards, 0L);

        List<Scorecard> results = testling.findAllFinishedScorecards();

        assertEquals(1, results.size());
    }

    @Test
    public void shouldFindAllUnfinishedScorecards() {
        List<Scorecard> scorecards = List.of(
                getGenericScorecardForPlayerWithName("Mike"),
                getGenericScorecardForPlayerWithName("Brian"),
                getGenericScorecardForPlayerWithName("Andrew")
        );
        scorecards.get(0).setArchived();
        testling = new ScorecardFileRepository(scorecards, 0L);

        List<Scorecard> results = testling.findAllUnfinishedScorecards();

        assertEquals(2, results.size());
    }

    @Test
    public void shouldFindNoScorecards_unfinished() {
        testling = new ScorecardFileRepository(new ArrayList<>(), 0L);

        List<Scorecard> results = testling.findAllUnfinishedScorecards();

        assertEquals(0, results.size());
    }

    @Test
    public void shouldFindNoScorecards_finished() {
        testling = new ScorecardFileRepository(new ArrayList<>(), 0L);

        List<Scorecard> results = testling.findAllFinishedScorecards();

        assertEquals(0, results.size());
    }

    @Test
    public void shouldDeleteScorecardFromList(){
        List<Scorecard> scorecards = new LinkedList<>(Arrays.asList(
                getGenericScorecardForPlayerWithName("Mike"),
                getGenericScorecardForPlayerWithName("Brian"),
                getGenericScorecardForPlayerWithName("Andrew")
        ));
        testling = new ScorecardFileRepository(scorecards, 0L) {
            @Override
            protected boolean saveToFile(Context context, ScorecardFileRepository objectToWriteToFile) {
                return true;
            }
        };
        Scorecard toDeleteScorecard = getGenericScorecardForPlayerWithName("Mike");
        testling.delete(toDeleteScorecard, null);

        assertEquals(2, testling.getAllScorecards().size());
    }

    private static Scorecard getGenericScorecardForPlayerWithName(String playerName) {
        return new Scorecard(
                new Players(List.of(new Player(playerName))),
                getGenericCourse(), ZonedDateTime.now()
        );
    }

    private static Course getGenericCourse() {
        return new Course("Big Rapids", 18);
    }
}