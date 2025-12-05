package com.markphilpot;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

public class BatteryBanksTest {
  private static final Logger log = LogManager.getLogger(BatteryBanksTest.class);

  @Test
  public void testSamplePart1() {
    var inputStream = BatteryBanksTest.class.getClassLoader().getResourceAsStream("sample.txt");

    var banks = BatteryBanks.parse(inputStream);

    var num = banks.stream().map(BatteryBanks::findLargest).reduce(0, Integer::sum);

    assertThat(num, is(357));
  }

  @Test
  public void testInputPart1() {
    var inputStream = BatteryBanksTest.class.getClassLoader().getResourceAsStream("input.txt");

    var banks = BatteryBanks.parse(inputStream);

    var num = banks.stream().map(BatteryBanks::findLargest).reduce(0, Integer::sum);

    log.info(num);
  }

  @Test
  public void testSamplePart2() {
    var inputStream = BatteryBanksTest.class.getClassLoader().getResourceAsStream("sample.txt");

    var banks = BatteryBanks.parse(inputStream);

    var num = banks.stream().map(BatteryBanks::findLargestN).reduce(0L, Long::sum);

    assertThat(num, is(3121910778619L));
  }

  @Test
  public void testInputPart2() {
    var inputStream = BatteryBanksTest.class.getClassLoader().getResourceAsStream("input.txt");

    var banks = BatteryBanks.parse(inputStream);

    var num = banks.stream().map(BatteryBanks::findLargestN).reduce(0L, Long::sum);

    log.info(num);
  }
}
