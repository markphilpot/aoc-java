package com.markphilpot;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class CleanupTest {
    private static final Logger log = LogManager.getLogger(CleanupTest.class);

    @Test
    public void testSamplePart1() {
        var inputStream = CleanupTest.class.getClassLoader().getResourceAsStream("sample.txt");

        var groups = Cleanup.parse(inputStream);

        var numRanges = groups.stream().map(Cleanup.Group::contains).map(x -> x ? 1 : 0).reduce(0, Integer::sum);

        assertThat(numRanges, is(2));
    }

    @Test
    public void testInputPart1() {
        var inputStream = CleanupTest.class.getClassLoader().getResourceAsStream("input.txt");

        var groups = Cleanup.parse(inputStream);

        var numRanges = groups.stream().map(Cleanup.Group::contains).map(x -> x ? 1 : 0).reduce(0, Integer::sum);

        // assertThat(numRanges, is(2));
        log.info(numRanges);
    }

    @Test
    public void testSamplePart2() {
        var inputStream = CleanupTest.class.getClassLoader().getResourceAsStream("sample.txt");

        var groups = Cleanup.parse(inputStream);

        var numRanges = groups.stream().map(Cleanup.Group::overlaps).map(x -> x ? 1 : 0).reduce(0, Integer::sum);

        assertThat(numRanges, is(4));
    }

    @Test
    public void testInputPart2() {
        var inputStream = CleanupTest.class.getClassLoader().getResourceAsStream("input.txt");

        var groups = Cleanup.parse(inputStream);

        var numRanges = groups.stream().map(Cleanup.Group::overlaps).map(x -> x ? 1 : 0).reduce(0, Integer::sum);

        // assertThat(numRanges, is(2));
        log.info(numRanges);
    }
}
