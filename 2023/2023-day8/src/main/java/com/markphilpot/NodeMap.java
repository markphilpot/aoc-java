package com.markphilpot;

import java.io.InputStream;
import java.util.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NodeMap {
  private static final Logger log = LogManager.getLogger(NodeMap.class);

  public record Branch(String left, String right) {}

  public record Directions(List<String> turns, Map<String, Branch> nodes) {}

  public static Directions parse(InputStream inputStream) {
    try (var scanner = new Scanner(inputStream)) {
      var turnLine = scanner.nextLine();
      scanner.nextLine(); // Blank

      var nodes = new TreeMap<String, Branch>();
      while (scanner.hasNextLine()) {
        var line = scanner.nextLine();

        var el = line.split(" = ");
        var key = el[0];

        var values = el[1].trim().split(", ");

        nodes.put(key, new Branch(values[0].substring(1), values[1].replaceFirst(".$", "")));
      }

      return new Directions(ParsingUtils.lineToList(turnLine), nodes);
    }
  }

  public static int countSteps(Directions directions) {
    var currentKey = "AAA";
    var i = 0;

    while (!currentKey.equals("ZZZ")) {
      var node = directions.nodes().get(currentKey);
      var turn = directions.turns.get(i % directions.turns().size());

      currentKey =
          switch (turn) {
            case "L" -> node.left();
            case "R" -> node.right();
            default -> throw new IllegalStateException();
          };
      i++;
    }

    return i;
  }

  public static long countGhostSteps(Directions directions) {
    var startingKeys = directions.nodes().keySet().stream().filter(k -> k.endsWith("A")).toList();

    // Through internet hints, there are cycles for each "ghost", so we need to find the first cycle
    // length
    // for each ghost and then find lcm...
    var cycles =
        startingKeys.stream()
            .parallel()
            .map(
                n -> {
                  var currentNode = n;
                  var x = 0;

                  do {
                    var turn = directions.turns.get(x % directions.turns.size());
                    var fork = directions.nodes().get(currentNode);
                    currentNode =
                        switch (turn) {
                          case "L" -> fork.left();
                          case "R" -> fork.right();
                          default -> throw new IllegalStateException();
                        };
                    x++;
                  } while (!currentNode.endsWith("Z"));

                  return x;
                })
            .toList();

    log.info(cycles);

    return lcm(cycles.stream().map(Long::valueOf).toList());
  }

  // https://stackoverflow.com/a/40531215
  private static long gcd(long x, long y) {
    return (y == 0) ? x : gcd(y, x % y);
  }

  public static long lcm(List<Long> numbers) {
    return numbers.stream().reduce(1L, (x, y) -> x * (y / gcd(x, y)));
  }
}
