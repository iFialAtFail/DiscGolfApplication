package com.manleysoftware.michael.discgolfapp.domain;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import java.time.ZonedDateTime;
import java.util.List;

public class ScorecardTest {

    private Scorecard testling;

    private ZonedDateTime dateTime;

    @Before
    public void setup() {
        //nothing
        dateTime = ZonedDateTime.now();
    }

    @Test(expected = IllegalStateException.class)
    public void canNotCreateScorecardWithEmptyPlayers() {
        testling = new Scorecard(new Players(), getGenericCourse(), dateTime);
    }

    @Test
    public void canGetListOfPlayerNamesWithSinglePlayer() {
        testling = new Scorecard(
                new Players(List.of(new Player("Mike"))),
                getGenericCourse(), dateTime
        );

        String results = testling.playerNames();

        assertEquals("Mike", results);
    }

    @Test
    public void canGetListOfPlayerNamesWithMultiplePlayers() {
        testling = new Scorecard(
                new Players(List.of(new Player("Mike"), new Player("Ben"), new Player("Andrew"))),
                getGenericCourse(), dateTime
        );

        String results = testling.playerNames();

        assertEquals("Mike, Ben, Andrew", results);
    }


    private static Course getGenericCourse() {
        return new Course("Big Rapids", 18);
    }
}