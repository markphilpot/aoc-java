package com.markphilpot;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import java.io.IOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

public class MirrorsAltTest {
  private static final Logger log = LogManager.getLogger(MirrorsAltTest.class);

  @Test
  public void testSamplePart1() throws IOException {
    var inputStream = MirrorsAltTest.class.getClassLoader().getResourceAsStream("sample.txt");

    var regions = MirrorsAlt.parse(inputStream);

    var folds = regions.stream().map(MirrorsAlt::findFold).toList();

    log.info(folds);

    var total = folds.stream().map(MirrorsAlt::scoreFold).reduce(0, Integer::sum);

    assertThat(total, is(405));
  }

  @Test
  public void testInputPart1() throws IOException {
    var inputStream = MirrorsAltTest.class.getClassLoader().getResourceAsStream("input.txt");

    var regions = MirrorsAlt.parse(inputStream);
    var folds = regions.stream().map(MirrorsAlt::findFold).toList();
    var total = folds.stream().map(MirrorsAlt::scoreFold).reduce(0, Integer::sum);

    log.info(total);
  }

  @Test
  public void testSamplePart2() throws IOException {
    var inputStream = MirrorsAltTest.class.getClassLoader().getResourceAsStream("sample.txt");

    var regions = MirrorsAlt.parse(inputStream);

    var folds = regions.stream().map(MirrorsAlt::findFoldECC).toList();

    log.info(folds);

    var total = folds.stream().map(MirrorsAlt::scoreFold).reduce(0, Integer::sum);

    assertThat(total, is(400));
  }

  @Test
  public void testInputPart2() throws IOException {
    var inputStream = MirrorsAltTest.class.getClassLoader().getResourceAsStream("input.txt");

    var regions = MirrorsAlt.parse(inputStream);
    var folds = regions.stream().map(MirrorsAlt::findFoldECC).toList();
    var total = folds.stream().map(MirrorsAlt::scoreFold).reduce(0, Integer::sum);

    log.info(total);
  }
}
