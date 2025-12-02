package com.markphilpot;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

public class AntennaTest {
  private static final Logger log = LogManager.getLogger(AntennaTest.class);

  @Test
  public void testAtoms() {
    var a = new Antenna(new Point(4, 3), "a");
    var b = new Antenna(new Point(5, 5), "a");
    var pair = new AntennaPair(a, b);
    var nodes = pair.getAntinodes();

    var x = new Point(3, 1);
    var y = new Point(6, 7);

    log.info(nodes);

    assertThat(nodes.getFirst().p().equals(x) || nodes.getFirst().p().equals(y), is(true));
    assertThat(nodes.getLast().p().equals(x) || nodes.getLast().p().equals(y), is(true));
  }

  @Test
  public void testSamplePart1() {
    var inputStream = AntennaTest.class.getClassLoader().getResourceAsStream("sample.txt");

    var num = 0;

    assertThat(num, is(0));
  }

  @Test
  public void testInputPart1() {
    var inputStream = AntennaTest.class.getClassLoader().getResourceAsStream("input.txt");

    var num = 0;

    log.info(num);
  }

  @Test
  public void testSamplePart2() {
    var inputStream = AntennaTest.class.getClassLoader().getResourceAsStream("sample.txt");

    var num = 0;

    assertThat(num, is(0));
  }

  @Test
  public void testInputPart2() {
    var inputStream = AntennaTest.class.getClassLoader().getResourceAsStream("input.txt");

    var num = 0;

    log.info(num);
  }
}
