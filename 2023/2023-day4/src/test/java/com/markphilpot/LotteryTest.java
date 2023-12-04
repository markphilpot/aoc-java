package com.markphilpot;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

public class LotteryTest {
  private static final Logger log = LogManager.getLogger(LotteryTest.class);

  @Test
  public void testSamplePart1() {
    var testInput =
        Map.of(
            "Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53", 8,
            "Card 2: 13 32 20 16 61 | 61 30 68 82 17 32 24 19", 2,
            "Card 3:  1 21 53 59 44 | 69 82 63 72 16 21 14  1", 2,
            "Card 4: 41 92 73 84 69 | 59 84 76 51 58  5 54 83", 1,
            "Card 5: 87 83 26 28 32 | 88 30 70 12 93 22 82 36", 0,
            "Card 6: 31 18 13 56 72 | 74 77 10 23 35 67 36 11", 0);

    var total =
        testInput.entrySet().stream()
            .map(
                e -> {
                  var lineTotal = Lottery.evalLine(e.getKey());
                  assertThat(lineTotal, is(e.getValue()));
                  return lineTotal;
                })
            .reduce(0, Integer::sum);

    assertThat(total, is(13));
  }

  @Test
  public void testInputPart1() {
    var inputStream = LotteryTest.class.getClassLoader().getResourceAsStream("input.txt");

    var total =
        ParsingUtils.streamToList(inputStream).stream()
            .map(Lottery::evalLine)
            .reduce(0, Integer::sum);

    log.info(total);
    // assertThat(numRanges, is(0));
  }

  @Test
  public void testSamplePart2() {
    var inputStream = LotteryTest.class.getClassLoader().getResourceAsStream("sample.txt");

    var total = Lottery.findTotalCards(ParsingUtils.streamToList(inputStream));

    assertThat(total, is(30));
  }

  @Test
  public void testInputPart2() {
    var inputStream = LotteryTest.class.getClassLoader().getResourceAsStream("input.txt");

    var total = Lottery.findTotalCards(ParsingUtils.streamToList(inputStream));

    log.info(total);
    assertThat(total, is(10378710));
  }
}
