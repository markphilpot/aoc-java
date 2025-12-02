package com.markphilpot;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

public class CombinationSafeTest {
  private static final Logger log = LogManager.getLogger(CombinationSafeTest.class);

  @Test
  public void testInstructions() {
    var safe = new CombinationSafe();

    assertThat(safe.isZero(), is(false));
    assertThat(safe.getCurrent(), is(50));

    safe.apply(CombinationSafe.Direction.L, 68);

    assertThat(safe.isZero(), is(false));
    assertThat(safe.getCurrent(), is(82));

    safe.apply(CombinationSafe.Direction.L, 30);
    assertThat(safe.getCurrent(), is(52));

    safe.apply(CombinationSafe.Direction.R, 48);
    assertThat(safe.isZero(), is(true));
  }

  @Test
  public void testSamplePart1() {
    var inputStream = CombinationSafeTest.class.getClassLoader().getResourceAsStream("sample.txt");

    var entries = CombinationSafe.parseAlt(inputStream);

    var num = 0;

    var safe = new CombinationSafe();

    for(var e : entries) {
      safe.apply(e.direction(), e.index());
      if(safe.isZero()) {
        num++;
      }
    }

    assertThat(num, is(3));
  }

  @Test
  public void testInputPart1() {
    var inputStream = CombinationSafeTest.class.getClassLoader().getResourceAsStream("input.txt");

    var entries = CombinationSafe.parse(inputStream);

    var num = 0;

    var safe = new CombinationSafe();

    for(var e : entries) {
      safe.apply(e.direction(), e.index());
      if(safe.isZero()) {
        num++;
      }
    }

    log.info(num);
  }

  @Test
  public void testCountingInstructions() {
    var safe = new CountingCombinationSafe();

    assertThat(safe.getNumZeros(), is(0));

    safe.apply(CombinationSafe.Direction.R, 1000);

    assertThat(safe.getNumZeros(), is(10));
  }

  @Test
  public void testSamplePart2() {
    var inputStream = CombinationSafeTest.class.getClassLoader().getResourceAsStream("sample.txt");

    var entries = CombinationSafe.parse(inputStream);

    var num = 0;

    var safe = new CountingCombinationSafe();

    for(var e : entries) {
      safe.apply(e.direction(), e.index());
    }

    assertThat(safe.getNumZeros(), is(6));
  }

  @Test
  public void testInputPart2() {
    var inputStream = CombinationSafeTest.class.getClassLoader().getResourceAsStream("input.txt");

    var entries = CombinationSafe.parse(inputStream);

    var safe = new CountingCombinationSafe();

    for(var e : entries) {
      safe.apply(e.direction(), e.index());
    }

    // 6216 to low...
    log.info(safe.getNumZeros());
  }
}
