package com.markphilpot;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class ParsingUtils {
  public static List<String> lineToList(String line) {
    return line.chars().mapToObj(x -> (char) x).map(Object::toString).toList();
  }

  public static List<String> streamToList(InputStream inputStream) {
    try (var scanner = new Scanner(inputStream)) {
      var list = new ArrayList<String>();
      while (scanner.hasNextLine()) {
        list.add(scanner.nextLine());
      }
      return list;
    }
  }

  public static Stream<Integer> readInts(String whitespaceDelimitedInts) {
    try (var s = new Scanner(whitespaceDelimitedInts)) {
      var stream = Stream.<Integer>builder();
      while (s.hasNextInt()) {
        stream.add(s.nextInt());
      }
      return stream.build();
    }
  }

  public static Stream<Long> readLongs(String whitespaceDelimitedInts) {
    try (var s = new Scanner(whitespaceDelimitedInts)) {
      var stream = Stream.<Long>builder();
      while (s.hasNextLong()) {
        stream.add(s.nextLong());
      }
      return stream.build();
    }
  }

  public static Stream<Integer> findIndexesOf(String line, String needle) {
    return IntStream.iterate(line.indexOf(needle), i -> i >= 0, i -> line.indexOf(needle, i + 1))
                    .boxed();
  }
}
