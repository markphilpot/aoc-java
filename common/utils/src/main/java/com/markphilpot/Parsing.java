package com.markphilpot;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Parsing {
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
}
