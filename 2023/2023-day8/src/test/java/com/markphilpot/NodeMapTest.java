package com.markphilpot;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

public class NodeMapTest {
  private static final Logger log = LogManager.getLogger(NodeMapTest.class);

  @Test
  public void testSamplePart1() {
    var inputStream = NodeMapTest.class.getClassLoader().getResourceAsStream("sample.txt");

    var directions = NodeMap.parse(inputStream);
    var num = NodeMap.countSteps(directions);

    assertThat(num, is(2));

    inputStream = NodeMapTest.class.getClassLoader().getResourceAsStream("sample2.txt");

    directions = NodeMap.parse(inputStream);
    num = NodeMap.countSteps(directions);

    assertThat(num, is(6));
  }

  @Test
  public void testInputPart1() {
    var inputStream = NodeMapTest.class.getClassLoader().getResourceAsStream("input.txt");

    var directions = NodeMap.parse(inputStream);
    var num = NodeMap.countSteps(directions);

    log.info(num);
  }

  @Test
  public void testSamplePart2() {
    var inputStream = NodeMapTest.class.getClassLoader().getResourceAsStream("sample3.txt");

    var directions = NodeMap.parse(inputStream);
    var num = NodeMap.countGhostSteps(directions);

    assertThat(num, is(6L));
  }

  @Test
  public void testInputPart2() {
    var inputStream = NodeMapTest.class.getClassLoader().getResourceAsStream("input.txt");

    var directions = NodeMap.parse(inputStream);
    var num = NodeMap.countGhostSteps(directions);

    log.info(num);
  }
}
