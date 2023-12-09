package com.markphilpot;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

public class SensorTest {
  private static final Logger log = LogManager.getLogger(SensorTest.class);

  @Test
  public void testSamplePart1() {
    var inputStream = SensorTest.class.getClassLoader().getResourceAsStream("sample.txt");

    var records = Sensor.parse(inputStream);
    var history = Sensor.findHistory(records);

    log.info(history);

    var total = history.stream().reduce(0L, Long::sum);

    assertThat(total, is(114L));
  }

  @Test
  public void testSample2Part1() {
    var inputStream = SensorTest.class.getClassLoader().getResourceAsStream("sample2.txt");

    var records = Sensor.parse(inputStream);
    var history = Sensor.findHistory(records);

    log.info(history);

    var total = history.stream().reduce(0L, Long::sum);

    assertThat(total, is(1424472L));
  }

  @Test
  public void testSample3Part1() {
    var inputStream = SensorTest.class.getClassLoader().getResourceAsStream("sample3.txt");

    var records = Sensor.parse(inputStream);
    var history = Sensor.findHistory(records);

    log.info(history);

    var total = history.stream().reduce(0L, Long::sum);

    assertThat(total, is(-51L));
  }

  @Test
  public void testInputPart1() {
    var inputStream = SensorTest.class.getClassLoader().getResourceAsStream("input.txt");

    var records = Sensor.parse(inputStream);
    var history = Sensor.findHistory(records);
    var total = history.stream().reduce(0L, Long::sum);

    log.info(total);
  }

  @Test
  public void testSamplePart2() {
    var inputStream = SensorTest.class.getClassLoader().getResourceAsStream("sample.txt");

    var records = Sensor.parse(inputStream);
    var history = Sensor.findHistory(records.stream().map(List::reversed).toList());
    var total = history.stream().reduce(0L, Long::sum);

    assertThat(total, is(2L));
  }

  @Test
  public void testInputPart2() {
    var inputStream = SensorTest.class.getClassLoader().getResourceAsStream("input.txt");

    var records = Sensor.parse(inputStream);
    var history = Sensor.findHistory(records.stream().map(List::reversed).toList());
    var total = history.stream().reduce(0L, Long::sum);

    log.info(total);
  }
}
