package com.markphilpot;

import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Springs {
  private static final Logger log = LogManager.getLogger(Springs.class);

  public enum State {
    O("."),
    D("#"),
    Q("?");

    private final String value;

    public String getValue() {
      return value;
    }

    State(String s) {
      value = s;
    }

    public static State fromString(String s) {
      return Arrays.stream(State.values())
          .filter(t -> t.value.equals(s))
          .findFirst()
          .orElseThrow(IllegalStateException::new);
    }

    @Override
    public String toString() {
      return value;
    }
  }

  public record Entry(List<State> spring, List<Integer> damagedGroups) {
    public boolean isValid() {
      // Check if entry obeys rules

      var currentDamageIndex = 0;
      var currentDamageLength = 0;

      for (var i = 0; i < spring.size(); i++) {
        var el = spring.get(i);

        if (el.equals(State.D)) {
          currentDamageLength++;
        } else if (currentDamageLength > 0) {
          // End of active damage region
          if (currentDamageIndex == damagedGroups.size()) {
            // Off the end
            return false;
          }

          if (damagedGroups.get(currentDamageIndex) == currentDamageLength) {
            // Valid
            currentDamageIndex++;
            currentDamageLength = 0;
          } else {
            // Invalid, return early
            return false;
          }
        }
      }

      // Check if last part of spring is damaged
      if (currentDamageLength > 0) {
        if (currentDamageIndex == damagedGroups().size()) {
          return false;
        }
        if (damagedGroups.get(currentDamageIndex) == currentDamageLength) {
          currentDamageIndex++;
        }
      }

      return currentDamageIndex == damagedGroups.size();
    }
  }

  public static Entry parse(String line) {
    var el = line.split(" ");

    return new Entry(
        ParsingUtils.lineToList(el[0]).stream().map(State::fromString).toList(),
        Arrays.stream(el[1].split(",")).map(Integer::valueOf).toList());
  }

  public static Entry parse(String line, int copies) {
    var el = line.split(" ");
    var spring = el[0];
    var damage = el[1];

    var springCopies =
        IntStream.range(0, copies).boxed().map(i -> spring).collect(Collectors.joining("?"));
    var damageCopies =
        IntStream.range(0, copies).boxed().map(i -> damage).collect(Collectors.joining(","));

    return new Entry(
        ParsingUtils.lineToList(springCopies).stream().map(State::fromString).toList(),
        Arrays.stream(damageCopies.split(",")).map(Integer::valueOf).toList());
  }

  public static List<Entry> parse(InputStream inputStream) {
    return parse(inputStream, 1);
  }

  public static List<Entry> parse(InputStream inputStream, int numCopies) {
    try (var scanner = new Scanner(inputStream)) {
      var list = new ArrayList<Entry>();
      while (scanner.hasNextLine()) {
        list.add(parse(scanner.nextLine(), numCopies));
      }
      return list;
    }
  }

  // Can't use this in part 2... I think I need to check as we go...
  public static Stream<Entry> generateCombinations(Entry entry) {
    var spring = entry.spring;
    var questionIndicies =
        IntStream.range(0, spring.size())
            .filter(i -> spring.get(i).equals(State.Q))
            .boxed()
            .toList();

    var product = Itertools.product(List.of(State.O, State.D), questionIndicies.size());

    var newEntries =
        product.stream()
            .map(ArrayList::new)
            .map(
                p -> {
                  var newSpring = new ArrayList<>(spring); // clone

                  // Could probably do this with a zip, but maintaining the closure might be a pain
                  for (var i = 0; i < questionIndicies.size(); i++) {
                    var qIndex = questionIndicies.get(i);
                    newSpring.set(qIndex, p.get(i));
                  }

                  return new Entry(newSpring, entry.damagedGroups);
                });

    return newEntries;
  }

  // OK, Part 2 -- guessing we have to terminate recursion with a cache... but need to figure out
  // the right key...
  public record CacheKey(int springIndex, int damageIndex, int damageSegmentLength) {}

  public static Map<CacheKey, Long> cache = new HashMap<>();

  public static Long findValidArrangements(Entry entry) {
    // Help with our edge case detection so a damage segment can't end the spring
    var s = new ArrayList<>(entry.spring);
    s.add(State.O);
    var e = new Entry(s, entry.damagedGroups);
    var total = findRecurse(e, 0, 0, 0);
    cache.clear();
    return total;
  }

  private static Long findRecurse(
      Entry entry, int springIndex, int damageIndex, int damageSegmentLength) {
    var key = new CacheKey(springIndex, damageIndex, damageSegmentLength);

    if (cache.containsKey(key)) {
      // Already computed this possibility
      return cache.get(key);
    } else {
      var total = 0L;

      if (springIndex == entry.spring.size()) {
        // End of the spring (which we made to always be `.`) handle the edge case
        total = (damageIndex == entry.damagedGroups.size() && damageSegmentLength == 0) ? 1L : 0L;
      } else {
        if (Set.of(State.O, State.Q).contains(entry.spring.get(springIndex))) {
          // Possible operational
          if (damageIndex < entry.damagedGroups.size()
              && damageSegmentLength == entry.damagedGroups.get(damageIndex)) {
            // Possible ending a damaged segment
            total += findRecurse(entry, springIndex + 1, damageIndex + 1, 0);
          }
          if (damageSegmentLength == 0) {
            // Continue
            total += findRecurse(entry, springIndex + 1, damageIndex, damageSegmentLength);
          }
        }

        if (Set.of(State.D, State.Q).contains(entry.spring.get(springIndex))) {
          // Possible damage
          total += findRecurse(entry, springIndex + 1, damageIndex, damageSegmentLength + 1);
        }
      }

      cache.put(key, total);

      return total;
    }
  }
}
