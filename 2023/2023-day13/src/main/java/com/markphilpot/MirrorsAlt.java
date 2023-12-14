package com.markphilpot;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MirrorsAlt {
  private static final Logger log = LogManager.getLogger(MirrorsAlt.class);

  public enum FoldType {
    HORIZONTAL,
    VERTICAL;
  }

  public record Fold(FoldType type, int index) {}

  public static List<String> parse(InputStream inputStream) throws IOException {
    var input = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

    var els = input.split("\n\n");

    return Arrays.stream(els).toList();
  }

  public static int scoreFold(Fold fold) {
    return switch (fold.type) {
      case HORIZONTAL -> 100 * fold.index;
      case VERTICAL -> fold.index;
    };
  }

  public static Fold findFold(String input) {
    return findVerticalFold(input).or(() -> findHorizontalFold(input)).orElseThrow();
  }

  public static Fold findFoldECC(String input) {
    return findHorizontalFoldECC(input).or(() -> findVerticalFoldECC(input)).orElseThrow();
  }

  public static Optional<Fold> findHorizontalFold(String input) {
    final var lines = input.lines().toList();
    return StreamUtils.zipWithIndex(input.lines()).collect(new WindowCollector<>(2)).stream()
        .filter(
            w -> {
              var a = w.getFirst();
              var b = w.getLast();
              return a.value().equals(b.value());
            })
        .filter(
            w -> {
              var a = w.getFirst();
              var b = w.getLast();

              var linesAbove = lines.subList(0, (int) a.index() + 1).reversed();
              var linesBelow = lines.subList((int) b.index(), lines.size());

              return StreamUtils.zip(linesAbove.stream(), linesBelow.stream(), List::of)
                  .allMatch(
                      window -> {
                        var x = window.getFirst();
                        var y = window.getLast();
                        return x.equals(y);
                      });
            })
        .findFirst()
        .map(
            w -> {
              var a = w.getFirst();
              return new Fold(FoldType.HORIZONTAL, (int) (a.index() + 1));
            });
  }

  public static Optional<Fold> findVerticalFold(String input) {
    var lines = input.lines().map(ParsingUtils::lineToList).toList();

    var transformed = new ArrayList<List<String>>();

    for (var x = 0; x < lines.getFirst().size(); x++) {
      var newRow = new ArrayList<String>();
      for (var y = 0; y < lines.size(); y++) {
        newRow.add(lines.get(y).get(x));
      }
      transformed.add(newRow);
    }

    // Flatten
    var tInput =
        transformed.stream().map(x -> String.join("", x)).collect(Collectors.joining("\n"));

    return findHorizontalFold(tInput).map(f -> new Fold(FoldType.VERTICAL, f.index));
  }

  private static int getNumDiff(String a, String b) {
    return (int)
        StreamUtils.zip(
                ParsingUtils.lineToList(a).stream(), ParsingUtils.lineToList(b).stream(), List::of)
            .filter(w -> !w.getFirst().equals(w.getLast()))
            .count();
  }

  public static Optional<Fold> findHorizontalFoldECC(String input) {
    final var lines = input.lines().toList();
    return StreamUtils.zipWithIndex(input.lines()).collect(new WindowCollector<>(2)).stream()
        .filter(
            w -> {
              var a = w.getFirst();
              var b = w.getLast();
              return a.value().equals(b.value()) || getNumDiff(a.value(), b.value()) == 1;
            })
        .filter(
            w -> {
              var a = w.getFirst();
              var b = w.getLast();

              var linesAbove = lines.subList(0, (int) a.index() + 1).reversed();
              var linesBelow = lines.subList((int) b.index(), lines.size());

              return StreamUtils.zip(linesAbove.stream(), linesBelow.stream(), List::of)
                      .map(
                          window -> {
                            var x = window.getFirst();
                            var y = window.getLast();
                            return getNumDiff(x, y);
                          })
                      .reduce(0, Integer::sum)
                  == 1;
            })
        .findFirst()
        .map(
            w -> {
              var a = w.getFirst();
              return new Fold(FoldType.HORIZONTAL, (int) (a.index() + 1));
            });
  }

  public static Optional<Fold> findVerticalFoldECC(String input) {
    var lines = input.lines().map(ParsingUtils::lineToList).toList();

    var transformed = new ArrayList<List<String>>();

    for (var x = 0; x < lines.getFirst().size(); x++) {
      var newRow = new ArrayList<String>();
      for (var y = 0; y < lines.size(); y++) {
        newRow.add(lines.get(y).get(x));
      }
      transformed.add(newRow);
    }

    // Flatten
    var tInput =
        transformed.stream().map(x -> String.join("", x)).collect(Collectors.joining("\n"));

    return findHorizontalFoldECC(tInput).map(f -> new Fold(FoldType.VERTICAL, f.index));
  }
}
