package com.markphilpot;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;

public class GuardTest {
  private static final Logger log = LogManager.getLogger(GuardTest.class);

  @Test
  public void testSamplePart1() {
    var inputStream = GuardTest.class.getClassLoader().getResourceAsStream("sample.txt");

    var guard = Guard.parse(inputStream);

    assertThat(guard.gridX, is(10));
    assertThat(guard.gridY, is(10));
    assertThat(guard.loc.x(), is(4));
    assertThat(guard.loc.y(), is(6));
    assertThat(guard.direction, is(Direction.NORTH));

    var num = guard.walk();

    assertThat(num, is(41));
  }

  @Test
  public void testInputPart1() {
    var inputStream = GuardTest.class.getClassLoader().getResourceAsStream("input.txt");

    var guard = Guard.parse(inputStream);

    var num = guard.walk();

    log.info(num);
  }

  @Test
  public void testSamplePart2() {
    var inputStream = GuardTest.class.getClassLoader().getResourceAsStream("sample.txt");

    var guardList = GuardLoop.parse(inputStream);
    var numLoops = guardList.stream().parallel().map(GuardLoop::walk).filter(x -> x).count();

    assertThat(numLoops, is(6L));
  }

  @Test
  public void testInputPart2() {
    var inputStream = GuardTest.class.getClassLoader().getResourceAsStream("input.txt");

    var guardList = GuardLoop.parse(inputStream);

    log.info("Num Maps :: %s".formatted(guardList.size()));

    var numLoops = guardList.stream().parallel().map(GuardLoop::walk).filter(x -> x).count();

    log.info(numLoops);
  }
}
