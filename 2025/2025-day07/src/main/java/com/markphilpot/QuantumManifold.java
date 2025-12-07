package com.markphilpot;

import com.markphilpot.Grid.Point;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;
import java.util.*;

public class QuantumManifold {
  private static final Logger log = LogManager.getLogger(QuantumManifold.class);

  private final Grid<Tile> grid;
  private final Point start;
  private final Map<Point, Long> pathCache = new HashMap<>();

  public enum Tile {
    EMPTY("."),
    START("S"),
    SPLIT("^");

    private final String value;

    Tile(String s) {
      this.value = s;
    }

    public String getValue() {
      return value;
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

  public QuantumManifold(Grid<Tile> grid, Point start) {
    this.grid = grid;
    this.start = start;
  }

  public long run() {
    return run(start);
  }

  private long run(Point p) {
    Grid.RPV<Tile> next = grid.walk(p, Grid.Direction.S);

    while(next.v() != null) {
      if(pathCache.containsKey(next.rc().point())) return pathCache.get(next.rc().point());

      if(next.v().equals(Tile.SPLIT)) {
        var left = grid.walk(next.rc().point(), Grid.Direction.E).rc().point();
        var numLeft = run(left);
        pathCache.put(left, numLeft);

        var right = grid.walk(next.rc().point(), Grid.Direction.W).rc().point();
        var numRight = run(right);
        pathCache.put(left, numLeft);

        pathCache.put(next.rc().point(), numLeft + numRight);
        return numLeft + numRight;
      } else {
        next = grid.walk(next.rc().point(), Grid.Direction.S);
      }
    }

    // Reached end
    return 1L;
  }

  public static QuantumManifold parse(InputStream inputStream) {
    try (var scanner = new Scanner(inputStream)) {
      var grid = new ArrayList<List<Tile>>();
      var start = new Point(0, 0);

      while (scanner.hasNextLine()) {
        var line = scanner.nextLine();
        var s = line.indexOf("S");
        if(s != -1) start = new Point(s, 0);

        grid.add(new ArrayList<>(ParsingUtils.lineToList(line).stream().map(Tile::fromString).toList()));
      }

      return new QuantumManifold(new Grid<>(grid), start);
    }
  }
}
