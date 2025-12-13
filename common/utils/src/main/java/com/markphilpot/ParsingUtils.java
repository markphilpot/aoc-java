package com.markphilpot;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class ParsingUtils {

  public static List<List<String>> streamToGrid(InputStream input) {
    var lines = streamToList(input);
    return lines.stream().map(line -> lineToStream(line).toList()).toList();
  }

  public static String streamToString(InputStream input) throws IOException {
    return new String(input.readAllBytes(), StandardCharsets.UTF_8);
  }

  public static List<String> lineToList(String line) {
    return lineToStream(line).toList();
  }

  public static Stream<String> lineToStream(String line) {
    return line.chars().mapToObj(x -> (char) x).map(Object::toString);
  }

  public static Stream<String> lineToStreamByWhitespace(String line) {
    try(var scanner = new Scanner(line)) {
      var list = new ArrayList<String>();
      while (scanner.hasNext()) {
        list.add(scanner.next());
      }
      return list.stream();
    }
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
