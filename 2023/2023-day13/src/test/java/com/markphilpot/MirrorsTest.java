package com.markphilpot;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

public class MirrorsTest {
  private static final Logger log = LogManager.getLogger(MirrorsTest.class);

  @Test
  public void testSamplePart1() {
    var inputStream = MirrorsTest.class.getClassLoader().getResourceAsStream("sample.txt");

    var regions = Mirrors.parse(inputStream);

    regions.forEach(log::info);

    var rowIndexes = regions.stream().map(Mirrors::findRowReflectionIndex).toList();

    log.info(rowIndexes);

    var colIndexes = regions.stream().map(Mirrors::findColumnReflectionIndex).toList();

    log.info(colIndexes);

    var total = StreamUtils.zip(rowIndexes.stream(), colIndexes.stream(), Mirrors::getRegionScore).reduce(0, Integer::sum);

    assertThat(total, is(405));
  }

  @Test
  public void testInputPart1() {
    var inputStream = MirrorsTest.class.getClassLoader().getResourceAsStream("input.txt");

    var regions = Mirrors.parse(inputStream);
    var rowIndexes = regions.stream().map(Mirrors::findRowReflectionIndex).toList();
    var colIndexes = regions.stream().map(Mirrors::findColumnReflectionIndex).toList();

    var total = StreamUtils.zip(rowIndexes.stream(), colIndexes.stream(), Mirrors::getRegionScore).reduce(0, Integer::sum);

    log.info(total);
  }

  @Test
  public void testSamplePart2() {
    var inputStream = MirrorsTest.class.getClassLoader().getResourceAsStream("sample.txt");

    var regions = Mirrors.parse(inputStream);

    regions.forEach(log::info);

    var rowIndexes = regions.stream().map(Mirrors::findRowReflectionIndex).toList();

    log.info(rowIndexes);

    var colIndexes = regions.stream().map(Mirrors::findColumnReflectionIndex).toList();

    log.info(colIndexes);

    var total = StreamUtils.zip(rowIndexes.stream(), colIndexes.stream(), Mirrors::getRegionScore).reduce(0, Integer::sum);

    assertThat(total, is(400));
  }

  @Test
  public void testInputPart2() {
    var inputStream = MirrorsTest.class.getClassLoader().getResourceAsStream("input.txt");

    var numRanges = 0;

    log.info(numRanges);

    assertThat(numRanges, is(0));
  }
}
