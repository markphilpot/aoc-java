package com.markphilpot;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class MasSearch {
  private static final Logger log = LogManager.getLogger(MasSearch.class);

  public static int findAll(List<List<String>> grid) {
    var numFound = 0;

    var g = new Grid<>(grid);

    for(int y = 0; y < grid.size(); y++) {
      for(int x = 0; x < grid.getFirst().size(); x++) {
        var at = grid.get(y).get(x);

        if(at.equals("A")) {
          var found = List.of(
//                  checkNS(x,y,g),
//                  checkEW(x,y,g),
                  checkNWSE(x,y,g),
                  checkNESW(x,y,g)
          );

          numFound += found.stream().map(v -> v ? 1 : 0).reduce(0, Integer::sum) == 2 ? 1 : 0;
        }
      }
    }

    return numFound;
  }

  private static boolean checkNS(int x, int y, Grid<String> g) {
    var line = List.of(
            new Grid.Point(x, y-1),
            new Grid.Point(x, y),
            new Grid.Point(x, y+1)
    );

    var word = line.stream().map(g::get).filter(Objects::nonNull).collect(Collectors.joining());

    return word.equals("MAS") || word.equals("SAM");
  }

  private static boolean checkEW(int x, int y, Grid<String> g) {
    var line = List.of(
            new Grid.Point(x-1, y),
            new Grid.Point(x, y),
            new Grid.Point(x+1, y)
    );

    var word = line.stream().map(g::get).filter(Objects::nonNull).collect(Collectors.joining());

    return word.equals("MAS") || word.equals("SAM");
  }

  private static boolean checkNESW(int x, int y, Grid<String> g) {
    var line = List.of(
            new Grid.Point(x+1, y-1),
            new Grid.Point(x, y),
            new Grid.Point(x-1, y+1)
    );

    var word = line.stream().map(g::get).filter(Objects::nonNull).collect(Collectors.joining());

    return word.equals("MAS") || word.equals("SAM");
  }

  private static boolean checkNWSE(int x, int y, Grid<String> g) {
    var line = List.of(
            new Grid.Point(x-1, y-1),
            new Grid.Point(x, y),
            new Grid.Point(x+1, y+1)
    );

    var word = line.stream().map(g::get).filter(Objects::nonNull).collect(Collectors.joining());

    return word.equals("MAS") || word.equals("SAM");
  }

}
