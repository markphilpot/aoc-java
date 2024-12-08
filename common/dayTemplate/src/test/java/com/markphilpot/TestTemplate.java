package com.markphilpot;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

public class TestTemplate {
  private static final Logger log = LogManager.getLogger(TestTemplate.class);

  @Test
  public void testSamplePart1() {
    var inputStream = TestTemplate.class.getClassLoader().getResourceAsStream("sample.txt");

    var num = 0;

    assertThat(num, is(0));
  }

  @Test
  public void testInputPart1() {
    var inputStream = TestTemplate.class.getClassLoader().getResourceAsStream("input.txt");

    var num = 0;

    log.info(num);
  }

  @Test
  public void testSamplePart2() {
    var inputStream = TestTemplate.class.getClassLoader().getResourceAsStream("sample.txt");

    var num = 0;

    assertThat(num, is(0));
  }

  @Test
  public void testInputPart2() {
    var inputStream = TestTemplate.class.getClassLoader().getResourceAsStream("input.txt");

    var num = 0;

    log.info(num);
  }
}
