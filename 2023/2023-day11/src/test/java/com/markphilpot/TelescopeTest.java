package com.markphilpot;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

public class TelescopeTest {
  private static final Logger log = LogManager.getLogger(TelescopeTest.class);

  @Test
  public void testSamplePart1() {
    var inputStream = TelescopeTest.class.getClassLoader().getResourceAsStream("sample.txt");

    var image = Telescope.parse(inputStream);

    log.info(image);

    image = Telescope.handleExpansion(image, 1);

    log.info(image);

    var pairs = Telescope.findDistances(image);

    log.info(pairs);

    var total = pairs.stream().map(Telescope.Pair::numSteps).reduce(0L, Long::sum);

    assertThat(total, is(374L));
  }

  @Test
  public void testInputPart1() {
    var inputStream = TelescopeTest.class.getClassLoader().getResourceAsStream("input.txt");

    var image = Telescope.parse(inputStream);
    image = Telescope.handleExpansion(image, 1);
    var pairs = Telescope.findDistances(image);
    var total = pairs.stream().map(Telescope.Pair::numSteps).reduce(0L, Long::sum);

    log.info(total);
  }

  @Test
  public void testSamplePart2() {
    var inputStream = TelescopeTest.class.getClassLoader().getResourceAsStream("sample.txt");

    var image = Telescope.parse(inputStream);

    // This scale factor *should* be 10, but 9 gets the correct answer.
    // I must be misunderstanding the question somehow...
    image = Telescope.handleExpansion(image, 9);

    log.info(image);

    var pairs = Telescope.findDistances(image);

    log.info(pairs);

    var total = pairs.stream().map(Telescope.Pair::numSteps).reduce(0L, Long::sum);

    assertThat(total, is(1030L));
  }

  @Test
  public void testSamplePart22() {
    var inputStream = TelescopeTest.class.getClassLoader().getResourceAsStream("sample.txt");

    var image = Telescope.parse(inputStream);
    image = Telescope.handleExpansion(image, 99);
    var pairs = Telescope.findDistances(image);
    var total = pairs.stream().map(Telescope.Pair::numSteps).reduce(0L, Long::sum);

    assertThat(total, is(8410L));
  }

  @Test
  public void testInputPart2() {
    var inputStream = TelescopeTest.class.getClassLoader().getResourceAsStream("input.txt");

    var image = Telescope.parse(inputStream);
    image = Telescope.handleExpansion(image, 999_999);
    var pairs = Telescope.findDistances(image);
    var total = pairs.stream().map(Telescope.Pair::numSteps).reduce(0L, Long::sum);

    log.info(total);
  }
}
