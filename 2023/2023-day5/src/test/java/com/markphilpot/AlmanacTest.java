package com.markphilpot;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import java.util.Map;
import java.util.TreeMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

public class AlmanacTest {
  private static final Logger log = LogManager.getLogger(AlmanacTest.class);

  @Test
  public void testSamplePart1() {
    var inputStream = AlmanacTest.class.getClassLoader().getResourceAsStream("sample.txt");

    var record = Almanac.parse(inputStream);

    var soilTest =
        new TreeMap<>(
            Map.of(
                79L, 81L,
                14L, 14L,
                55L, 57L,
                13L, 13L));

    soilTest.forEach(
        (key, value) -> {
          assertThat(record.followToDest(Almanac.To.seed_to_soil, key), is(value));
        });

    var loc = Almanac.findLowestLocation(record);

    assertThat(loc, is(35L));
  }

  @Test
  public void testInputPart1() {
    var inputStream = AlmanacTest.class.getClassLoader().getResourceAsStream("input.txt");

    var record = Almanac.parse(inputStream);
    var loc = Almanac.findLowestLocation(record);

    log.info(loc);

    //    assertThat(numRanges, is(0));
  }

  @Test
  public void testSamplePart2() {
    var inputStream = AlmanacTest.class.getClassLoader().getResourceAsStream("sample.txt");

    var record = Almanac.parseRange(inputStream);
    var loc = Almanac.findLowestLocation(record);

    assertThat(loc, is(46L));
  }

  @Test
  public void testInputPart2() {
    var inputStream = AlmanacTest.class.getClassLoader().getResourceAsStream("input.txt");

    var record = Almanac.parseRange(inputStream);

    // This takes 3:31 to run on a M1 MBP
    //    var loc = Almanac.findLowestLocation(record);
    //    log.info(loc);
  }
}
