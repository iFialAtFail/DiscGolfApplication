package com.manleysoftware.michael.discgolfapp.time;

import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class TimeUtilityTest {

    public static final String DATE_TIME_FORMAT = "dd MMM yyyy HH:mm";

    @Test
    public void canPrintFormattedZonedDateTime() {
        ZonedDateTime now = ZonedDateTime.of(1990, 1, 26, 12, 1, 1, 1, ZoneId.systemDefault());
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT, Locale.getDefault());
        MatcherAssert.assertThat(dateFormat.format(now), CoreMatchers.is("26 Jan 1990 12:01"));
    }

    public static void main(String[] args) {
        ZonedDateTime now = ZonedDateTime.now();
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_TIME_FORMAT, Locale.getDefault());
        System.out.println(dateFormat.format(now));

    }
}
