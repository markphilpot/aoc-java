package com.markphilpot;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

public class MachineLightsTest {
  private static final Logger log = LogManager.getLogger(MachineLightsTest.class);

  @Test
  public void testSamplePart1() {
    var inputStream = MachineLightsTest.class.getClassLoader().getResourceAsStream("sample.txt");

    var machines = MachineLights.parse(inputStream);

    var num = machines.stream().parallel().map(MachineLights::run).reduce(0, Integer::sum);

    assertThat(num, is(7));
  }

  @Test
  public void testInputPart1() {
    var inputStream = MachineLightsTest.class.getClassLoader().getResourceAsStream("input.txt");

    var machines = MachineLights.parse(inputStream);

    var num = machines.stream().map(MachineLights::run).reduce(0, Integer::sum);

    log.info(num);
  }

  @Test
  public void testSamplePart2() {
    var inputStream = MachineLightsTest.class.getClassLoader().getResourceAsStream("sample.txt");

    var machines = MachineLights.parse(inputStream);

    var num = machines.stream().map(MachineLights::runJoltage).reduce(0, Integer::sum);

    assertThat(num, is(33));
  }

  @Test
  public void testInputPart2() {
    var inputStream = MachineLightsTest.class.getClassLoader().getResourceAsStream("input.txt");

    // Need a different algo...
    
//    var machines = MachineLights.parse(inputStream);
//    var num = machines.stream().parallel().map(MachineLights::runJoltage).reduce(0, Integer::sum);

    log.info(num);
  }
}
