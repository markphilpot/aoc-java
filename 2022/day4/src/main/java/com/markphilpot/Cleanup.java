package com.markphilpot;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Cleanup {
  public record Assignment(Integer start, Integer end) {

    public boolean contains(Assignment other) {
      return (start <= other.start && end >= other.end)
          || (other.start <= start && other.end >= end);
    }

    public boolean overlaps(Assignment other) {
      return (start >= other.start && start <= other.end)
          || (end >= other.start && end <= other.end)
          || (start <= other.start && end >= other.end);
    }
  }

  public record Group(Assignment first, Assignment second) {
    public boolean contains() {
      return first.contains(second);
    }

    public boolean overlaps() {
      return first.overlaps(second) || second.overlaps(first);
    }
  }

  public static List<Group> parse(InputStream inputStream) {
    try (var scanner = new Scanner(inputStream).useDelimiter("\n")) {
      var groups = new ArrayList<Group>();

      while (scanner.hasNext()) {
        var line = scanner.next();

        var ranges = line.split(",");
        var firstSet = Arrays.stream(ranges[0].split("-")).map(Integer::parseInt).toList();
        var secondSet = Arrays.stream(ranges[1].split("-")).map(Integer::parseInt).toList();

        groups.add(
            new Cleanup.Group(
                new Cleanup.Assignment(firstSet.get(0), firstSet.get(1)),
                new Cleanup.Assignment(secondSet.get(0), secondSet.get(1))));
      }

      return groups;
    }
  }
}
