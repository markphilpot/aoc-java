package com.markphilpot;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

public class ManifoldTest {
  private static final Logger log = LogManager.getLogger(ManifoldTest.class);

  @Test
  public void testSamplePart1() {
    var inputStream = ManifoldTest.class.getClassLoader().getResourceAsStream("sample.txt");

    var manifold = Manifold.parse(inputStream);

    manifold.run();

    var num = manifold.getNumBeamSplits();

    assertThat(num, is(21));
  }

  @Test
  public void testInputPart1() {
    var inputStream = ManifoldTest.class.getClassLoader().getResourceAsStream("input.txt");

    var manifold = Manifold.parse(inputStream);

    manifold.run();

    var num = manifold.getNumBeamSplits();

    // 1698 is too high...
    log.info(num);
  }

  @Test
  public void testSamplePart2() {
    var inputStream = ManifoldTest.class.getClassLoader().getResourceAsStream("sample.txt");

    var manifold = QuantumManifold.parse(inputStream);

    var num = manifold.run();

    assertThat(num, is(40));
  }

  @Test
  public void testInputPart2() {
    var inputStream = ManifoldTest.class.getClassLoader().getResourceAsStream("input.txt");

    var manifold = QuantumManifold.parse(inputStream);

    var num = manifold.run();

    log.info(num);
  }
}
