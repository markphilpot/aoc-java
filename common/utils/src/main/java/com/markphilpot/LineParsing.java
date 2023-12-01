package com.markphilpot;

import java.util.List;

public class LineParsing {
  public static List<String> lineToList(String line) {
    return line.chars().mapToObj(x -> (char) x).map(Object::toString).toList();
  }
}
