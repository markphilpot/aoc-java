package com.markphilpot;

import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CamelCards {
  private static final Logger log = LogManager.getLogger(CamelCards.class);

  public static Map<String, Integer> cardRanks =
      new HashMap<>() {
        {
          put("2", 1);
          put("3", 2);
          put("4", 3);
          put("5", 4);
          put("6", 5);
          put("7", 6);
          put("8", 7);
          put("9", 8);
          put("T", 9);
          put("J", 10);
          put("Q", 11);
          put("K", 12);
          put("A", 13);
        }
      };

  public enum HandType {
    FIVE,
    FOUR,
    FULL,
    THREE,
    TWO,
    ONE,
    HIGH,
    NONE,
  }

  public static Map<HandType, Integer> handRanks =
      new HashMap<>() {
        {
          put(HandType.NONE, 0);
          put(HandType.HIGH, 1);
          put(HandType.ONE, 2);
          put(HandType.TWO, 3);
          put(HandType.THREE, 4);
          put(HandType.FULL, 5);
          put(HandType.FOUR, 6);
          put(HandType.FIVE, 7);
        }
      };

  public record Card(String c) implements Comparable<Card> {

    @Override
    public int compareTo(Card o) {
      return cardRanks.get(c).compareTo(cardRanks.get(o.c));
    }
  }

  public record Hand(List<Card> cards, long bid) implements Comparable<Hand> {

    public HandType getHandType() {
      var cardSet = new TreeSet<>(cards);
      var cardCounts =
          cardSet.stream()
              .map(c -> cards.stream().filter(x -> x.equals(c)).count())
              .collect(Collectors.toSet());

      return switch (cardSet.size()) {
        case 5 -> HandType.HIGH;
        case 4 -> HandType.ONE;
        case 3 -> {
          if (cardCounts.contains(3L)) {
            yield HandType.THREE;
          } else {
            yield HandType.TWO;
          }
        }
        case 2 -> {
          if (cardCounts.contains(4L)) {
            yield HandType.FOUR;
          } else {
            yield HandType.FULL;
          }
        }
        case 1 -> HandType.FIVE;
        default -> throw new IllegalStateException("Invalid card hand");
      };
    }

    @Override
    public int compareTo(Hand o) {
      if (getHandType().equals(o.getHandType())) {
        return StreamUtils.zip(cards.stream(), o.cards().stream(), Card::compareTo)
            .filter(x -> x != 0)
            .findFirst()
            .orElseThrow(IllegalStateException::new);
      } else {
        return handRanks.get(getHandType()).compareTo(handRanks.get(o.getHandType()));
      }
    }

    @Override
    public String toString() {
      return "Hand{"
          + "cards="
          + cards.stream().map(c -> c.c).collect(Collectors.joining(""))
          + ", bid="
          + bid
          + ", rank="
          + getHandType()
          + '}';
    }
  }

  public static List<Hand> parse(InputStream inputStream) {
    try (var scanner = new Scanner(inputStream)) {
      var hands = new ArrayList<Hand>();

      while (scanner.hasNextLine()) {
        var el = scanner.nextLine().split(" ");
        var cards = ParsingUtils.lineToList(el[0]);
        var bid = Long.parseLong(el[1]);

        hands.add(new Hand(cards.stream().map(Card::new).toList(), bid));
      }

      return hands;
    }
  }

  public static List<Hand> rank(List<Hand> hands) {
    return hands.stream().sorted().toList();
  }

  public static long score(List<Hand> hands) {
    return StreamUtils.zipWithIndex(hands.stream())
        .map(x -> x.value().bid() * (x.index() + 1))
        .reduce(0L, Long::sum);
  }
}
