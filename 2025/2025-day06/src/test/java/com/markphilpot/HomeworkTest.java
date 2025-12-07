package com.markphilpot;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

public class HomeworkTest {
  private static final Logger log = LogManager.getLogger(HomeworkTest.class);

  @Test
  public void testSamplePart1() {
    var inputStream = HomeworkTest.class.getClassLoader().getResourceAsStream("sample.txt");

    var columns = Homework.parse(inputStream);

    var num = columns.stream().map(Homework.Column::compute).reduce(0L, Long::sum);

    assertThat(num, is(4277556L));
  }

  @Test
  public void testInputPart1() {
    var inputStream = HomeworkTest.class.getClassLoader().getResourceAsStream("input.txt");

    var columns = Homework.parse(inputStream);

    var num = columns.stream().map(Homework.Column::compute).reduce(0L, Long::sum);

    log.info(num);
  }

  @Test
  public void testSamplePart2() {
    var inputStream = HomeworkTest.class.getClassLoader().getResourceAsStream("sample.txt");

    var hwg = HomeworkGrid.parse(inputStream, 3);

    var num = HomeworkGrid.compute(hwg);

    assertThat(num, is(3263827L));
  }

  @Test
  public void testInputPart2() {
    var inputStream = HomeworkTest.class.getClassLoader().getResourceAsStream("input.txt");

    var hwg = HomeworkGrid.parse(inputStream, 4);

    var num = HomeworkGrid.compute(hwg);

    log.info(num);
  }
}
