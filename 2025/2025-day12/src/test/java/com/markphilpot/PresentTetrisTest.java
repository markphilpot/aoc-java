package com.markphilpot;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

public class PresentTetrisTest {
  private static final Logger log = LogManager.getLogger(PresentTetrisTest.class);

  @Test
  public void testSamplePart1() {
    var inputStream = PresentTetrisTest.class.getClassLoader().getResourceAsStream("sample.txt");

    var manifest = PresentTetris.parse(inputStream);
    var num = manifest.underTrees().stream().filter(ut -> PresentTetris.check(ut, manifest.shapes())).count();

    assertThat(num, is(2L));
  }

  @Test
  public void testInputPart1() {
    var inputStream = PresentTetrisTest.class.getClassLoader().getResourceAsStream("input.txt");

    var manifest = PresentTetris.parse(inputStream);
    var num = manifest.underTrees().stream().filter(ut -> PresentTetris.check(ut, manifest.shapes())).count();

    log.info(num);
  }

  @Test
  public void testSamplePart2() {
    var inputStream = PresentTetrisTest.class.getClassLoader().getResourceAsStream("sample.txt");

    var num = 0;

    assertThat(num, is(0));
  }

  @Test
  public void testInputPart2() {
    var inputStream = PresentTetrisTest.class.getClassLoader().getResourceAsStream("input.txt");

    var num = 0;

    log.info(num);
  }
}
