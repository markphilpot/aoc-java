package com.markphilpot;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

public class PipesTest {
  private static final Logger log = LogManager.getLogger(PipesTest.class);

  @Test
  public void testSamplePart1() {
    var inputStream = PipesTest.class.getClassLoader().getResourceAsStream("sample.txt");

    var pipeGrid = Pipes.parse(inputStream);

    log.info(pipeGrid.start());

    var numSteps = Pipes.countSteps(pipeGrid);

    assertThat(numSteps, is(4));
  }

  @Test
  public void testSample2Part1() {
    var inputStream = PipesTest.class.getClassLoader().getResourceAsStream("sample2.txt");

    var pipeGrid = Pipes.parse(inputStream);

    var numSteps = Pipes.countSteps(pipeGrid);

    assertThat(numSteps, is(8));
  }

  @Test
  public void testInputPart1() {
    var inputStream = PipesTest.class.getClassLoader().getResourceAsStream("input.txt");

    var pipeGrid = Pipes.parse(inputStream);
    var numSteps = Pipes.countSteps(pipeGrid);

    log.info(numSteps);
  }

  @Test
  public void testSamplePart2() {
    var inputStream = PipesTest.class.getClassLoader().getResourceAsStream("part2/sample1.txt");

    var pipeGrid = Pipes.parse(inputStream);
    var numInside = Pipes.countInside(pipeGrid);

    assertThat(numInside, is(4));

    inputStream = PipesTest.class.getClassLoader().getResourceAsStream("part2/sample2.txt");

    pipeGrid = Pipes.parse(inputStream);
    numInside = Pipes.countInside(pipeGrid);

    assertThat(numInside, is(8));

    inputStream = PipesTest.class.getClassLoader().getResourceAsStream("part2/sample3.txt");

    pipeGrid = Pipes.parse(inputStream);
    numInside = Pipes.countInside(pipeGrid);

    assertThat(numInside, is(10));
  }

  @Test
  public void testInputPart2() {
    var inputStream = PipesTest.class.getClassLoader().getResourceAsStream("input.txt");

    var pipeGrid = Pipes.parse(inputStream);
    var numInside = Pipes.countInside(pipeGrid);

    log.info(numInside);
  }
}
