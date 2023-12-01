package com.markphilpot;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import java.util.ArrayList;
import java.util.Scanner;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

public class RucksackTest {
  private static final Logger log = LogManager.getLogger(RucksackTest.class);

  @Test
  public void testSamplePart1() {
    var inputStream = RucksackTest.class.getClassLoader().getResourceAsStream("sample.txt");

    var scanner = new Scanner(inputStream).useDelimiter("\n");

    var sacks = new ArrayList<Rucksack>();

    while (scanner.hasNext()) {
      sacks.add(new Rucksack(scanner.next()));
    }

    var total = sacks.stream().map(Rucksack::getPriority).reduce(0, Integer::sum);

    assertThat(total, is(157));
  }

  @Test
  public void testInputPart1() {
    var inputStream = RucksackTest.class.getClassLoader().getResourceAsStream("input.txt");

    var scanner = new Scanner(inputStream).useDelimiter("\n");

    var sacks = new ArrayList<Rucksack>();

    while (scanner.hasNext()) {
      sacks.add(new Rucksack(scanner.next()));
    }

    var total = sacks.stream().map(Rucksack::getPriority).reduce(0, Integer::sum);

    log.info(total);
  }

  @Test
  public void testSamplePart2() {
    var inputStream = RucksackTest.class.getClassLoader().getResourceAsStream("sample.txt");

    var scanner = new Scanner(inputStream).useDelimiter("\n");

    var sacks = new ArrayList<RucksackGroup>();

    while (scanner.hasNext()) {
      sacks.add(new RucksackGroup(scanner.next(), scanner.next(), scanner.next()));
    }

    var total = sacks.stream().map(RucksackGroup::getPriority).reduce(0, Integer::sum);

    assertThat(total, is(70));
  }

  @Test
  public void testInputPart2() {
    var inputStream = RucksackTest.class.getClassLoader().getResourceAsStream("input.txt");

    var scanner = new Scanner(inputStream).useDelimiter("\n");

    var sacks = new ArrayList<RucksackGroup>();

    while (scanner.hasNext()) {
      sacks.add(new RucksackGroup(scanner.next(), scanner.next(), scanner.next()));
    }

    var total = sacks.stream().map(RucksackGroup::getPriority).reduce(0, Integer::sum);

    log.info(total);
  }
}
