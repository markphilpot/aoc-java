package com.markphilpot;

import java.util.*;
import java.util.regex.MatchResult;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Lottery {
  private static final Logger log = LogManager.getLogger(Lottery.class);

  /**
   * Card 1: 41 48 83 86 17 | 83 86 6 31 17 9 48 53
   *
   * @param line
   * @return
   */
  public static int evalLine(String line) {
    var scanner = new Scanner(line);
    scanner.findInLine("Card\\s+(\\d+): ([^|]+) \\| (.*)");
    MatchResult result = scanner.match();
    var cardId = result.group(1);
    var cardValues = result.group(2).trim();
    var selfValues = result.group(3).trim();

    var cardSet = ParsingUtils.readInts(cardValues).collect(Collectors.toSet());
    var selfList = ParsingUtils.readInts(selfValues).toList();

    var winners = selfList.stream().filter(cardSet::contains).toList();

    return (int) ((!winners.isEmpty() ? 1 : 0) * Math.pow(2, (Math.max(winners.size() - 1, 0))));
  }

  public record Round(int cardId, Set<Integer> cardSet, List<Integer> selfSet, int winners) {}

  public static Round parseRound(String line) {
    var scanner = new Scanner(line);
    scanner.findInLine("Card\\s+(\\d+): ([^|]+) \\| (.*)");
    MatchResult result = scanner.match();
    var cardId = result.group(1);
    var cardValues = result.group(2).trim();
    var selfValues = result.group(3).trim();

    var cardSet = ParsingUtils.readInts(cardValues).collect(Collectors.toSet());
    var selfList = ParsingUtils.readInts(selfValues).toList();

    var winners = selfList.stream().filter(cardSet::contains).toList();

    return new Round(Integer.parseInt(cardId), cardSet, selfList, winners.size());
  }

  public static int findTotalCards(List<String> lines) {
    var originalCards = lines.stream().map(Lottery::parseRound).toList();
    var cardCopies = new ArrayList<Round>();

    //    for (var i = 0; i < originalCards.size(); i++) {
    //      var r = originalCards.get(i);
    //
    //      if (r.winners() > 0) {
    //        cardCopies.addAll(originalCards.subList(i + 1, i + 1 + r.winners()));
    //      }
    //    }

    originalCards.forEach(
        StreamUtils.forEachWithIndex(
            (i, r) -> {
              if (r.winners() > 0) {
                cardCopies.addAll(originalCards.subList(i + 1, i + 1 + r.winners()));
              }
            }));

    for (var i = 0; i < cardCopies.size(); i++) {
      var r = cardCopies.get(i);

      if (r.winners() > 0) {
        cardCopies.addAll(originalCards.subList(r.cardId(), r.cardId() + r.winners()));
      }
    }

    return originalCards.size() + cardCopies.size();
  }
}
