package com.markphilpot;

import java.util.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Calibration {
  private static final Logger log = LogManager.getLogger(Calibration.class);

  public static int getCalibrationValue(List<String> input) {
    var firstNumber = input.stream().filter(v -> v.matches("\\d")).findFirst().get();
    var rList = input.reversed();
    var secondNumber = rList.stream().filter(v -> v.matches("\\d")).findFirst().get();

    return Integer.parseInt("%s%s".formatted(firstNumber, secondNumber));
  }

  public static List<String> wordNumbers =
      List.of("one", "two", "three", "four", "five", "six", "seven", "eight", "nine");

  public record WordMatch(String word, Integer index) {}

  public static int getCalibrationValueWithWords(String line) {
    var input = LineParsing.lineToList(line);

    var firstNumber = input.stream().filter(v -> v.matches("\\d")).findFirst();
    var firstNumberIndex = firstNumber.map(input::indexOf).orElse(-1);

    var rList = input.reversed();
    ;

    var lastNumber = rList.stream().filter(v -> v.matches("\\d")).findFirst();
    var lastNumberIndex = lastNumber.map(input::lastIndexOf).orElse(-1);

    var earliestWordIndexes =
        wordNumbers.stream().map(word -> new WordMatch(word, line.indexOf(word))).toList();
    var latestWordIndexes =
        wordNumbers.stream().map(word -> new WordMatch(word, line.lastIndexOf(word))).toList();

    var earliest =
        earliestWordIndexes.stream()
            .filter(wm -> wm.index() != -1)
            .min(Comparator.comparing(WordMatch::index));
    var latest =
        latestWordIndexes.stream()
            .filter(wm -> wm.index() != -1)
            .max(Comparator.comparing(WordMatch::index));

    String first, second;

    if (earliest.isEmpty() && firstNumber.isEmpty()) {
      log.error("First Error :: %s".formatted(line));
    }
    if (latest.isEmpty() && lastNumber.isEmpty()) {
      log.error("Second Error :: %s".formatted(line));
    }

    if (earliest.isEmpty()
        || (firstNumber.isPresent() && firstNumberIndex < earliest.get().index())) {
      first = firstNumber.get();
    } else {
      first = Integer.toString(wordNumbers.indexOf(earliest.get().word()) + 1);
    }

    if (latest.isEmpty() || (lastNumber.isPresent() && lastNumberIndex > latest.get().index())) {
      second = lastNumber.get();
    } else {
      second = Integer.toString(wordNumbers.indexOf(latest.get().word()) + 1);
    }

    return Integer.parseInt("%s%s".formatted(first, second));
  }
}
