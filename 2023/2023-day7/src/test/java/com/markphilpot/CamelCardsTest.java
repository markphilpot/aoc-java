package com.markphilpot;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;

import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

public class CamelCardsTest {
  private static final Logger log = LogManager.getLogger(CamelCardsTest.class);

  @Test
  public void testSamplePart1() {
    var inputStream = CamelCardsTest.class.getClassLoader().getResourceAsStream("sample.txt");

    var hands = CamelCards.parse(inputStream);
    var rankedHands = CamelCards.rank(hands);
    var total = CamelCards.score(rankedHands);

    assertThat(total, is(6440L));
  }

  @Test
  public void testInputPart1() {
    var inputStream = CamelCardsTest.class.getClassLoader().getResourceAsStream("input.txt");

    var hands = CamelCards.parse(inputStream);
    var rankedHands = CamelCards.rank(hands);

    //    rankedHands.forEach(h -> log.info("%s %s".formatted(h.cards()
    //            .stream().map(CamelCards.Card::c).collect(Collectors.joining("")),
    // h.getHandType())));

    var total = CamelCards.score(rankedHands);

    log.info(total);
    assertThat(total, is(not(254805650L)));
  }

  @Test
  public void testSamplePart2() {
    var inputStream = CamelCardsTest.class.getClassLoader().getResourceAsStream("sample.txt");

    var hands = CamelCardsJoker.parse(inputStream);
    var rankedHands = CamelCardsJoker.rank(hands);
    var total = CamelCardsJoker.score(rankedHands);

    assertThat(total, is(5905L));
  }

  @Test
  public void testInputPart2() {
    var inputStream = CamelCardsTest.class.getClassLoader().getResourceAsStream("input.txt");

    var hands = CamelCardsJoker.parse(inputStream);
    var rankedHands = CamelCardsJoker.rank(hands);

    rankedHands.forEach(
        h ->
            log.info(
                "%s %s -> %s"
                    .formatted(
                        h.cards().stream()
                            .map(CamelCardsJoker.Card::c)
                            .collect(Collectors.joining("")),
                        h.getHandTypeNoJoker(),
                        h.getHandType())));

    var total = CamelCardsJoker.score(rankedHands);

    assertThat(total, is(254494947L));
    log.info(total);
  }
}
