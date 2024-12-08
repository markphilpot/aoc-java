package com.markphilpot;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class XmasSearch {
  private static final Logger log = LogManager.getLogger(XmasSearch.class);

  public static final String SEED = "XMAS";
  public static final List<String> NEEDLE = ParsingUtils.lineToStream(SEED).toList();

  public static int findAll(List<List<String>> grid) {
    var numFound = 0;

    var g = new Grid<>(grid);

    for(int y = 0; y < grid.size(); y++) {
      for(int x = 0; x < grid.getFirst().size(); x++) {
        var at = grid.get(y).get(x);

        if(at.equals(NEEDLE.getFirst())) {
          var found = List.of(
                  checkN(x,y,g),
                  checkE(x,y,g),
                  checkS(x,y,g),
                  checkW(x,y,g),
                  checkNE(x,y,g),
                  checkNW(x,y,g),
                  checkSE(x,y,g),
                  checkSW(x,y,g)
          );

          numFound += found.stream().map(v -> v ? 1 : 0).reduce(0, Integer::sum);
        }
      }
    }

    return numFound;
  }

  private static boolean checkN(int x, int y, Grid<String> g) {
    var line = List.of(
            new Grid.Point(x, y),
            new Grid.Point(x, y-1),
            new Grid.Point(x, y-2),
            new Grid.Point(x, y-3)
    );

    var word = line.stream().map(g::get).filter(Objects::nonNull).collect(Collectors.joining());

    return word.equals(SEED);
  }

  private static boolean checkS(int x, int y, Grid<String> g) {
    var line = List.of(
            new Grid.Point(x, y),
            new Grid.Point(x, y+1),
            new Grid.Point(x, y+2),
            new Grid.Point(x, y+3)
    );

    var word = line.stream().map(g::get).filter(Objects::nonNull).collect(Collectors.joining());

    return word.equals(SEED);
  }

  private static boolean checkE(int x, int y, Grid<String> g) {
    var line = List.of(
            new Grid.Point(x, y),
            new Grid.Point(x+1, y),
            new Grid.Point(x+2, y),
            new Grid.Point(x+3, y)
    );

    var word = line.stream().map(g::get).filter(Objects::nonNull).collect(Collectors.joining());

    return word.equals(SEED);
  }

  private static boolean checkW(int x, int y, Grid<String> g) {
    var line = List.of(
            new Grid.Point(x, y),
            new Grid.Point(x-1, y),
            new Grid.Point(x-2, y),
            new Grid.Point(x-3, y)
    );

    var word = line.stream().map(g::get).filter(Objects::nonNull).collect(Collectors.joining());

    return word.equals(SEED);
  }

  private static boolean checkNE(int x, int y, Grid<String> g) {
    var line = List.of(
            new Grid.Point(x, y),
            new Grid.Point(x+1, y-1),
            new Grid.Point(x+2, y-2),
            new Grid.Point(x+3, y-3)
    );

    var word = line.stream().map(g::get).filter(Objects::nonNull).collect(Collectors.joining());

    return word.equals(SEED);
  }

  private static boolean checkNW(int x, int y, Grid<String> g) {
    var line = List.of(
            new Grid.Point(x, y),
            new Grid.Point(x-1, y-1),
            new Grid.Point(x-2, y-2),
            new Grid.Point(x-3, y-3)
    );

    var word = line.stream().map(g::get).filter(Objects::nonNull).collect(Collectors.joining());

    return word.equals(SEED);
  }

  private static boolean checkSW(int x, int y, Grid<String> g) {
    var line = List.of(
            new Grid.Point(x, y),
            new Grid.Point(x-1, y+1),
            new Grid.Point(x-2, y+2),
            new Grid.Point(x-3, y+3)
    );

    var word = line.stream().map(g::get).filter(Objects::nonNull).collect(Collectors.joining());

    return word.equals(SEED);
  }

  private static boolean checkSE(int x, int y, Grid<String> g) {
    var line = List.of(
            new Grid.Point(x, y),
            new Grid.Point(x+1, y+1),
            new Grid.Point(x+2, y+2),
            new Grid.Point(x+3, y+3)
    );

    var word = line.stream().map(g::get).filter(Objects::nonNull).collect(Collectors.joining());

    return word.equals(SEED);
  }
}
