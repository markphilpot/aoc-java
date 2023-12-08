package com.markphilpot;

import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CamelCardsJoker {
  private static final Logger log = LogManager.getLogger(CamelCardsJoker.class);

  public static Map<String, Integer> cardRanks =
      new HashMap<>() {
        {
          put("J", 0);
          put("2", 1);
          put("3", 2);
          put("4", 3);
          put("5", 4);
          put("6", 5);
          put("7", 6);
          put("8", 7);
          put("9", 8);
          put("T", 9);
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
      // Remove Jokers for calculation
      var numJokers = (int) cards.stream().filter(c -> c.c().equals("J")).count();
      var remainingCards = cards.stream().filter(c -> !c.c().equals("J")).toList();
      var cardCounts = new ArrayList<Integer>();
      List<Card> tmp = new ArrayList<>(remainingCards);
      while (!tmp.isEmpty()) {
        var c = tmp.getFirst();
        var count = tmp.stream().filter(c::equals).count();
        cardCounts.add((int) count);
        tmp = tmp.stream().filter(x -> !x.equals(c)).toList();
      }

      //            log.info(remainingCards);
      //            log.info(cardCounts);
      //            log.info(cards);

      //            var filteredHand =
      // remainingCards.stream().map(Card::c).collect(Collectors.joining());

      var handWithoutJoker =
          switch (cardCounts.size()) {
            case 5 -> HandType.HIGH;
            case 4 -> {
              if (cardCounts.contains(2)) {
                yield HandType.ONE;
              } else {
                yield HandType.HIGH;
              }
            }
            case 3 -> {
              if (cardCounts.contains(3)) {
                yield HandType.THREE;
              } else if (Collections.frequency(cardCounts, 2) == 2) {
                yield HandType.TWO;
              } else if (cardCounts.contains(2)) {
                yield HandType.ONE;
              } else {
                yield HandType.HIGH;
              }
            }
            case 2 -> {
              if (cardCounts.contains(4)) {
                yield HandType.FOUR;
              } else if (cardCounts.contains(3) && cardCounts.contains(2)) {
                yield HandType.FULL;
              } else if (cardCounts.contains(3)) {
                yield HandType.THREE;
              } else if (Collections.frequency(cardCounts, 2) == 2) {
                yield HandType.TWO;
              } else if (cardCounts.contains(2)) {
                yield HandType.ONE;
              } else {
                yield HandType.HIGH;
              }
            }
            case 1 -> {
              if (cardCounts.contains(5)) {
                yield HandType.FIVE;
              } else if (cardCounts.contains(4)) {
                yield HandType.FOUR;
              } else if (cardCounts.contains(3)) {
                yield HandType.THREE;
              } else if (cardCounts.contains(2)) {
                yield HandType.ONE;
              } else {
                yield HandType.HIGH;
              }
            }
            case 0 -> HandType.NONE;
            default -> throw new IllegalStateException("Invalid card hand");
          };

      //            log.info("%s
      // %s".formatted(remainingCards.stream().map(Card::c).collect(Collectors.joining()),
      // handWithoutJoker));

      return switch (handWithoutJoker) {
        case FIVE -> HandType.FIVE;
        case FOUR -> switch (numJokers) {
          case 0 -> HandType.FOUR;
          case 1 -> HandType.FIVE;
          default -> throw new IllegalStateException();
        };
        case FULL -> switch (numJokers) {
          case 0 -> HandType.FULL;
          default -> throw new IllegalStateException(cards.toString());
        };
        case THREE -> switch (numJokers) {
          case 0 -> HandType.THREE;
          case 1 -> HandType.FOUR;
          case 2 -> HandType.FIVE;
          default -> throw new IllegalStateException();
        };
        case TWO -> switch (numJokers) {
          case 0 -> HandType.TWO;
          case 1 -> HandType.FULL;
          default -> throw new IllegalStateException();
        };
        case ONE -> switch (numJokers) {
          case 0 -> HandType.ONE;
          case 1 -> HandType.THREE;
          case 2 -> HandType.FOUR;
          case 3 -> HandType.FIVE;
          default -> throw new IllegalStateException();
        };
        case HIGH -> switch (numJokers) {
          case 0 -> HandType.HIGH;
          case 1 -> HandType.ONE;
          case 2 -> HandType.THREE;
          case 3 -> HandType.FOUR;
          case 4 -> HandType.FIVE;
          default -> throw new IllegalStateException();
        };
        case NONE -> switch (numJokers) {
          case 5 -> HandType.FIVE;
          default -> throw new IllegalStateException();
        };
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
