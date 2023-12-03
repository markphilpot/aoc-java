package com.markphilpot;

import java.io.InputStream;
import java.util.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GameBag {
  private static final Logger log = LogManager.getLogger(GameBag.class);

  public enum Color {
    red,
    green,
    blue;
  }

  public record Game(int id, List<HashMap<Color, Integer>> rounds) {}

  public static List<Game> parse(InputStream inputStream) {
    var lines = ParsingUtils.streamToList(inputStream);

    return lines.stream()
        .map(
            line -> {
              var s = new Scanner(line);
              s.findInLine("Game (\\d+): (.*)");
              var mr = s.match();
              var id = mr.group(1);
              var roundSpec = mr.group(2);

              return new Game(Integer.parseInt(id), parseRoundSpec(roundSpec.trim()));
            })
        .toList();
  }

  private static List<HashMap<Color, Integer>> parseRoundSpec(String roundSpec) {
    var rounds = roundSpec.split(";");

    return Arrays.stream(rounds)
        .map(
            r -> {
              var els = Arrays.stream(r.trim().split(",")).map(String::trim).toList();

              var map = new HashMap<Color, Integer>();

              els.forEach(
                  el -> {
                    var elSplit = el.split(" ");
                    var num = Integer.parseInt(elSplit[0]);
                    var color = Color.valueOf(elSplit[1]);
                    map.put(color, num);
                  });

              return map;
            })
        .toList();
  }

  public static boolean isInvalid(Map<Color, Integer> ruleSet, Game game) {
    var result =
        game.rounds().stream()
            .map(
                r -> {
                  return (!r.containsKey(Color.red) || r.get(Color.red) <= ruleSet.get(Color.red))
                      && (!r.containsKey(Color.green)
                          || r.get(Color.green) <= ruleSet.get(Color.green))
                      && (!r.containsKey(Color.blue)
                          || r.get(Color.blue) <= ruleSet.get(Color.blue));
                })
            .toList();

    return result.stream().anyMatch(x -> !x);
  }

  public static Map<Color, Integer> findMinRuleSet(Game game) {
    var ruleSet = new HashMap<Color, Integer>();

    ruleSet.put(Color.red, 0);
    ruleSet.put(Color.green, 0);
    ruleSet.put(Color.blue, 0);

    game.rounds()
        .forEach(
            r -> {
              Arrays.stream(Color.values())
                  .forEach(
                      c -> {
                        if (r.containsKey(c)) {
                          ruleSet.put(c, Integer.max(ruleSet.get(c), r.get(c)));
                        }
                      });
            });

    return ruleSet;
  }
}
