package com.markphilpot;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

public class RaceTest {
  private static final Logger log = LogManager.getLogger(RaceTest.class);

  @Test
  public void testSamplePart1() {
    var inputStream = RaceTest.class.getClassLoader().getResourceAsStream("sample.txt");

    var records = Race.parse(inputStream);

    log.info(records);

    var numWays =
        records.stream().map(Race::findNumWinningScenarios).reduce(1L, NumberUtils::product);

    assertThat(numWays, is(288L));
  }

  @Test
  public void testInputPart1() {
    var inputStream = RaceTest.class.getClassLoader().getResourceAsStream("input.txt");

    var records = Race.parse(inputStream);

    var numWays =
        records.stream().map(Race::findNumWinningScenarios).reduce(1L, NumberUtils::product);

    log.info(numWays);
  }

  @Test
  public void testSamplePart2() {
    var inputStream = RaceTest.class.getClassLoader().getResourceAsStream("sample.txt");

    var records = Race.parseSingleRace(inputStream);

    log.info(records);

    var numWays =
        records.stream().map(Race::findNumWinningScenarios).reduce(1L, NumberUtils::product);

    assertThat(numWays, is(71503L));
  }

  @Test
  public void testInputPart2() {
    var inputStream = RaceTest.class.getClassLoader().getResourceAsStream("input.txt");

    var records = Race.parseSingleRace(inputStream);

    var numWays =
        records.stream().map(Race::findNumWinningScenarios).reduce(1L, NumberUtils::product);

    log.info(numWays);
  }
}
