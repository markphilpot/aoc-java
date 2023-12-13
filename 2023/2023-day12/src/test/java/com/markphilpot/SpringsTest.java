package com.markphilpot;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

public class SpringsTest {
  private static final Logger log = LogManager.getLogger(SpringsTest.class);

  @Test
  public void testSamplePart1() {
    var samples =
        List.of(
            List.of("???.### 1,1,3", 1),
            List.of(".??..??...?##. 1,1,3", 4),
            List.of("?#?#?#?#?#?#?#? 1,3,1,6", 1),
            List.of("????.#...#... 4,1,1", 1),
            List.of("????.######..#####. 1,6,5", 4),
            List.of("?###???????? 3,2,1", 10));

    samples.forEach(
        m -> {
          var entry = Springs.parse((String) m.getFirst());

          log.info(entry);

          var combinations = Springs.generateCombinations(entry);

          log.info(combinations);

          var validArrangements = combinations.filter(Springs.Entry::isValid).toList();

          log.info("Valid Arrangements");
          validArrangements.forEach(log::info);

          assertThat((String) m.getFirst(), validArrangements.size(), is(m.getLast()));
        });
  }

  @Test
  public void testInputPart1() {
    var inputStream = SpringsTest.class.getClassLoader().getResourceAsStream("input.txt");

    var entries = Springs.parse(inputStream);

    var total =
        entries.stream()
            .flatMap(Springs::generateCombinations)
            .filter(Springs.Entry::isValid)
            .count();

    log.info(total);
  }

  @Test
  public void testSamplePart2() {
    var samples =
        List.of(
            List.of("???.### 1,1,3", 1L),
            List.of(".??..??...?##. 1,1,3", 16384L),
            List.of("?#?#?#?#?#?#?#? 1,3,1,6", 1L),
            List.of("????.#...#... 4,1,1", 16L),
            List.of("????.######..#####. 1,6,5", 2500L),
            List.of("?###???????? 3,2,1", 506250L));

    samples.forEach(
        m -> {
          var entry = Springs.parse((String) m.getFirst(), 5);

          var numValid = Springs.findValidArrangements(entry);

          assertThat((String) m.getFirst(), numValid, is(m.getLast()));
        });
  }

  @Test
  public void testInputPart2() {
    var inputStream = SpringsTest.class.getClassLoader().getResourceAsStream("input.txt");

    var entries = Springs.parse(inputStream, 5);

    var total = entries.stream().map(Springs::findValidArrangements).reduce(0L, Long::sum);

    log.info(total);
  }
}
