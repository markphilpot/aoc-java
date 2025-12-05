package com.markphilpot;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;
import java.util.*;

public class RollDiagram {
  private static final Logger log = LogManager.getLogger(RollDiagram.class);

  public enum Tile {
    EMPTY("."),
    ROLL("@");

    private final String value;

    public String getValue() {
      return value;
    }

    Tile(String s) {
      value = s;
    }

    public static Tile fromString(String s) {
      return Arrays.stream(Tile.values())
              .filter(t -> t.value.equals(s))
              .findFirst()
              .orElseThrow(IllegalStateException::new);
    }

    @Override
    public String toString() {
      return value;
    }
  }

  public static int numRollsFree(Grid<Tile> grid) {
    var numFree = 0;

    for(int y = 0; y < grid.numRows(); y++) {
      for(int x = 0; x < grid.numCols(); x++) {
        var point = grid.get(new Grid.Point(x, y));
        if(point.equals(Tile.ROLL)) {
          var adjacent = grid.getAdjacent(new Grid.Point(x, y));

          var numAround = adjacent.stream().filter(Optional::isPresent).map(Optional::get)
                  .map(grid::get)
                  .filter(t -> t.equals(Tile.ROLL)).count();

          if(numAround < 4) {
            numFree++;
          }
        }
      }
    }

    return numFree;
  }

  public static void removeFreeRolls(Grid<Tile> grid) {
    var pointsToRemove = new ArrayList<Grid.Point>();

    for(int y = 0; y < grid.numRows(); y++) {
      for(int x = 0; x < grid.numCols(); x++) {
        var point = grid.get(new Grid.Point(x, y));
        if(point.equals(Tile.ROLL)) {
          var adjacent = grid.getAdjacent(new Grid.Point(x, y));

          var numAround = adjacent.stream().filter(Optional::isPresent).map(Optional::get)
                  .map(grid::get)
                  .filter(t -> t.equals(Tile.ROLL)).count();

          if(numAround < 4) {
            pointsToRemove.add(new Grid.Point(x, y));
          }
        }
      }
    }

    for (Grid.Point p : pointsToRemove) {
      grid.set(p, Tile.EMPTY);
    }
  }

  public static Grid<Tile> parse(InputStream inputStream) {
    try (var scanner = new Scanner(inputStream)) {
      var grid = new ArrayList<List<Tile>>();

      while (scanner.hasNextLine()) {
        var line = scanner.nextLine();
        grid.add(new ArrayList<>(ParsingUtils.lineToList(line).stream().map(Tile::fromString).toList()));
      }

      return new Grid<>(grid);
    }
  }
}
