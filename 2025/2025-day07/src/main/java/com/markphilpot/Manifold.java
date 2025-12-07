package com.markphilpot;

import com.markphilpot.Grid.Point;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;
import java.util.*;

public class Manifold {
  private static final Logger log = LogManager.getLogger(Manifold.class);

  private final Grid<Tile> grid;
  private final Queue<Point> splitQueue = new LinkedList<>();
  private final Point start;
  private int numBeamSplits = 0;

  private Set<Point> beamPoints = new HashSet<>();

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

  public Manifold(Grid<Tile> grid, Point start) {
    this.grid = grid;
    this.start = start;
  }

  public void run() {
    run(start);

    while(!splitQueue.isEmpty()) {
      var split = splitQueue.remove();

      var left = grid.walk(split, Grid.Direction.E).rc().point();
      if(!beamPoints.contains(left)) {
          beamPoints.add(left);
          run(left);
      }
      var right = grid.walk(split, Grid.Direction.W).rc().point();
      if(!beamPoints.contains(right)) {
          beamPoints.add(right);
          run(right);
      }
    }
  }

  private void run(Point p) {
    beamPoints.add(p);
    Grid.RPV<Tile> next = grid.walk(p, Grid.Direction.S);

    while(next.v() != null) {
      if(next.v().equals(Tile.SPLIT)) {
        numBeamSplits++;
        splitQueue.add(next.rc().point());
        break;
      } else if(beamPoints.contains(next.rc().point())) {
        // Intersected existing beam
        break;
      } else {
        beamPoints.add(next.rc().point());
        next = grid.walk(next.rc().point(), Grid.Direction.S);
      }
    }
  }

  public int getNumBeamSplits() {
    return numBeamSplits;
  }

  public static Manifold parse(InputStream inputStream) {
    try (var scanner = new Scanner(inputStream)) {
      var grid = new ArrayList<List<Tile>>();
      var start = new Point(0, 0);

      while (scanner.hasNextLine()) {
        var line = scanner.nextLine();
        var s = line.indexOf("S");
        if(s != -1) start = new Point(s, 0);

        grid.add(new ArrayList<>(ParsingUtils.lineToList(line).stream().map(Tile::fromString).toList()));
      }

      return new Manifold(new Grid<>(grid), start);
    }
  }
}
