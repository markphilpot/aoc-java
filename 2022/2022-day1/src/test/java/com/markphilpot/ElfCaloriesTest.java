package com.markphilpot;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

public class ElfCaloriesTest {
  private static final Logger log = LogManager.getLogger(ElfCaloriesTest.class);

  @Test
  public void testSample() {
    var inputStream = ElfCaloriesTest.class.getClassLoader().getResourceAsStream("sample.txt");

    var leader = Elf.findLeader(inputStream);

    assertThat(leader.index(), is(4));
    assertThat(leader.calories(), is(24000));
  }

  @Test
  public void testInput() {
    var inputStream = ElfCaloriesTest.class.getClassLoader().getResourceAsStream("input.txt");

    var leader = Elf.findLeader(inputStream);

    log.info(leader);

    assertThat(leader.index(), is(7));
    assertThat(leader.calories(), is(67633));
  }

  @Test
  public void testSampleTopThree() {
    var inputStream = ElfCaloriesTest.class.getClassLoader().getResourceAsStream("sample.txt");

    var leaders = Elf.findTopThree(inputStream);

    log.info(leaders);

    var calories = leaders.stream().map(Elf.ElfCalories::calories).reduce(0, Integer::sum);

    assertThat(calories, is(45000));
  }

  @Test
  public void testInputTopThree() {
    var inputStream = ElfCaloriesTest.class.getClassLoader().getResourceAsStream("input.txt");

    var leaders = Elf.findTopThree(inputStream);

    var calories = leaders.stream().map(Elf.ElfCalories::calories).reduce(0, Integer::sum);

    log.info(leaders);
    log.info(calories);
  }
}
