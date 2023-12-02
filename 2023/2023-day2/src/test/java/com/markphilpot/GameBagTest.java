package com.markphilpot;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

public class GameBagTest {
  private static final Logger log = LogManager.getLogger(GameBagTest.class);

  @Test
  public void testSamplePart1() {
    var inputStream = GameBagTest.class.getClassLoader().getResourceAsStream("sample.txt");

    var games = GameBag.parse(inputStream);

    var ruleSet = Map.of(GameBag.Color.red, 12, GameBag.Color.green, 13, GameBag.Color.blue, 14);

    var idTotal =
        games.stream()
            .filter(g -> !GameBag.isInvalid(ruleSet, g))
            .map(GameBag.Game::id)
            .reduce(0, Integer::sum);

    assertThat(idTotal, is(8));
  }

  @Test
  public void testInputPart1() {
    var inputStream = GameBagTest.class.getClassLoader().getResourceAsStream("input.txt");

    var games = GameBag.parse(inputStream);

    var ruleSet = Map.of(GameBag.Color.red, 12, GameBag.Color.green, 13, GameBag.Color.blue, 14);

    var idTotal =
        games.stream()
            .filter(g -> !GameBag.isInvalid(ruleSet, g))
            .map(GameBag.Game::id)
            .reduce(0, Integer::sum);

    log.info(idTotal);
  }

  @Test
  public void testSamplePart2() {
    var inputStream = GameBagTest.class.getClassLoader().getResourceAsStream("sample.txt");

    var games = GameBag.parse(inputStream);

    var minRuleSets = games.stream().map(GameBag::findMinRuleSet).toList();

    var powers =
        minRuleSets.stream()
            .map(r -> r.values().stream().reduce(1, (acc, el) -> acc * el))
            .toList();

    assertEquals(List.of(48, 12, 1560, 630, 36), powers);

    var totalPowers = powers.stream().reduce(0, Integer::sum);

    assertThat(totalPowers, is(2286));
  }

  @Test
  public void testInputPart2() {
    var inputStream = GameBagTest.class.getClassLoader().getResourceAsStream("input.txt");

    var games = GameBag.parse(inputStream);

    var minRuleSets = games.stream().map(GameBag::findMinRuleSet).toList();

    var powers =
        minRuleSets.stream()
            .map(r -> r.values().stream().reduce(1, (acc, el) -> acc * el))
            .toList();

    var totalPowers = powers.stream().reduce(0, Integer::sum);

    log.info(totalPowers);

    // assertThat(totalPowers, is(2286));
  }
}
