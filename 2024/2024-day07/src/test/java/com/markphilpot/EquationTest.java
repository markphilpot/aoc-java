package com.markphilpot;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

public class EquationTest {
  private static final Logger log = LogManager.getLogger(EquationTest.class);

  @Test
  public void testSamplePart1() {
    var inputStream = EquationTest.class.getClassLoader().getResourceAsStream("sample.txt");

    var num = ParsingUtils.streamToList(inputStream).stream()
            .map(Equation::parse)
            .filter(Equation::isValid)
            .map(Equation::result)
            .reduce(0L, Long::sum);

    assertThat(num, is(3749L));
  }

  @Test
  public void testInputPart1() {
    var inputStream = EquationTest.class.getClassLoader().getResourceAsStream("input.txt");

    var num = ParsingUtils.streamToList(inputStream).stream()
            .map(Equation::parse)
            .filter(Equation::isValid)
            .map(Equation::result)
            .reduce(0L, Long::sum);

    log.info(num);
  }

  @Test
  public void testSamplePart2() {
    var inputStream = EquationTest.class.getClassLoader().getResourceAsStream("sample.txt");

    var num = ParsingUtils.streamToList(inputStream).stream()
            .map(SuperEquation::parse)
            .filter(SuperEquation::isValid)
            .map(SuperEquation::result)
            .reduce(0L, Long::sum);

    assertThat(num, is(11387L));
  }

  @Test
  public void testInputPart2() {
    var inputStream = EquationTest.class.getClassLoader().getResourceAsStream("input.txt");

    var num = ParsingUtils.streamToList(inputStream).stream()
            .map(SuperEquation::parse)
            .filter(SuperEquation::isValid)
            .map(SuperEquation::result)
            .reduce(0L, Long::sum);

    log.info(num);
  }
}
