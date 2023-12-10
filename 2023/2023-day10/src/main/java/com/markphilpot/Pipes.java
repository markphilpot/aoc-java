package com.markphilpot;

import static com.markphilpot.Grid.Direction.*;
import static com.markphilpot.Pipes.Tile.*;

import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Pipes {
  private static final Logger log = LogManager.getLogger(Pipes.class);

  public enum Tile {
    VERTICAL("|"),
    HORIZONTAL("-"),
    BEND_NE("L"),
    BEND_NW("J"),
    BEND_SW("7"),
    BEND_SE("F"),
    GROUND("."),
    START("S");

    private final String value;

    public String getValue() {
      return value;
    }

    Tile(String s) {
      value = s;
    }

    public boolean validEnter(Grid.Direction d) {
      return switch (d) {
        case N -> switch (this) {
          case VERTICAL, BEND_NE, BEND_NW -> true;
          default -> false;
        };
        case S -> switch (this) {
          case VERTICAL, BEND_SE, BEND_SW -> true;
          default -> false;
        };
        case E -> switch (this) {
          case HORIZONTAL, BEND_SW, BEND_NW -> true;
          default -> false;
        };
        case W -> switch (this) {
          case HORIZONTAL, BEND_SE, BEND_NE -> true;
          default -> false;
        };
      };
    }

    public Grid.Direction getExit(Grid.Direction in) {
      return switch (in) {
        case N -> switch (this) {
          case VERTICAL -> S;
          case BEND_NE -> E;
          case BEND_NW -> W;
          default -> throw new IllegalStateException();
        };
        case S -> switch (this) {
          case VERTICAL -> N;
          case BEND_SE -> E;
          case BEND_SW -> W;
          default -> throw new IllegalStateException();
        };
        case E -> switch (this) {
          case HORIZONTAL -> W;
          case BEND_NE -> N;
          case BEND_SE -> S;
          default -> throw new IllegalStateException();
        };
        case W -> switch (this) {
          case HORIZONTAL -> E;
          case BEND_NW -> N;
          case BEND_SW -> S;
          default -> throw new IllegalStateException();
        };
      };
    }

    public static Tile fromString(String s) {
      return Arrays.stream(Tile.values())
          .filter(t -> t.value.equals(s))
          .findFirst()
          .orElseThrow(IllegalStateException::new);
    }
  }

  public record PipeGrid(Grid<Tile> grid, Grid.Point start) {
    public Tile getStartEquivalent() {
      var directions =
          grid.getRelOrthoVal(start).stream()
              .filter(
                  rpv ->
                      rpv.map(tileRPV -> tileRPV.v().validEnter(tileRPV.rc().d().getInvert()))
                          .orElse(false))
              .map(rpv -> rpv.get().rc().d())
              .collect(Collectors.toSet());

      // Getting a little punchy
      if (directions.contains(S) && directions.contains(W)) {
        return BEND_SE;
      } else if (directions.contains(S) && directions.contains(N)) {
        return VERTICAL;
      } else if (directions.contains(E) && directions.contains(W)) {
        return HORIZONTAL;
      } else if (directions.contains(S) && directions.contains(E)) {
        return BEND_SW;
      } else if (directions.contains(N) && directions.contains(W)) {
        return BEND_NE;
      } else if (directions.contains(N) && directions.contains(E)) {
        return BEND_NW;
      } else {
        throw new IllegalStateException(directions.toString());
      }
    }
  }

  public static PipeGrid parse(InputStream inputStream) {
    try (var scanner = new Scanner(inputStream)) {
      var grid = new ArrayList<List<Tile>>();
      Grid.Point start = null;
      int currentColumn = 0;

      while (scanner.hasNextLine()) {
        var line = scanner.nextLine();
        grid.add(ParsingUtils.lineToList(line).stream().map(Tile::fromString).toList());

        var startX = line.indexOf("S");

        if (startX != -1) {
          start = new Grid.Point(startX, currentColumn);
        }

        currentColumn++;
      }

      return new PipeGrid(new Grid<>(grid), start);
    }
  }

  public static int countSteps(PipeGrid pipeGrid) {
    var step = 0;
    log.info(pipeGrid.grid.getRelOrthoVal(pipeGrid.start));
    var nextDirection =
        pipeGrid.grid.getRelOrthoVal(pipeGrid.start).stream()
            .filter(
                rpv ->
                    rpv.map(tileRPV -> tileRPV.v().validEnter(tileRPV.rc().d().getInvert()))
                        .orElse(false))
            .findFirst()
            .orElseThrow()
            .orElseThrow()
            .rc()
            .d();

    Grid.Point currentPoint = pipeGrid.start;
    Grid.RPV<Tile> next;

    while (true) {
      next = pipeGrid.grid.walk(currentPoint, nextDirection);
      currentPoint = next.rc().point();
      step++;

      // Hack
      if (next.v().equals(Tile.START)) {
        break;
      }
      nextDirection = next.v().getExit(nextDirection.getInvert());
    }

    return step / 2;
  }

  public static Set<Grid.Point> getLinePoints(PipeGrid pipeGrid) {
    var linePoints = new HashSet<Grid.Point>();

    var nextDirection =
        pipeGrid.grid.getRelOrthoVal(pipeGrid.start).stream()
            .filter(
                rpv ->
                    rpv.map(tileRPV -> tileRPV.v().validEnter(tileRPV.rc().d().getInvert()))
                        .orElse(false))
            .findFirst()
            .orElseThrow()
            .orElseThrow()
            .rc()
            .d();

    Grid.Point currentPoint = pipeGrid.start;
    Grid.RPV<Tile> next;

    while (true) {
      next = pipeGrid.grid.walk(currentPoint, nextDirection);
      currentPoint = next.rc().point();

      linePoints.add(currentPoint);

      if (next.v().equals(Tile.START)) {
        break;
      }

      nextDirection = next.v().getExit(nextDirection.getInvert());
    }

    return linePoints;
  }

  public static int countInside(PipeGrid pipeGrid) {
    var grid = pipeGrid.grid;

    var line = getLinePoints(pipeGrid);

    var inside = 0;

    var startH = Set.of(BEND_NE, BEND_SE);
    var endH = Set.of(BEND_NW, BEND_SW);

    for (var y = 0; y < pipeGrid.grid.numRows(); y++) {
      var numIntersections = 0;
      Tile horizontalSegmentStart = null;

      //            log.info("Considering %s".formatted(grid.getRow(y).stream()
      //                    .map(Tile::getValue).collect(Collectors.joining())));

      for (var x = 0; x < pipeGrid.grid.numCols(); x++) {
        var p = new Grid.Point(x, y);

        if (line.contains(p)) {
          var tile = grid.get(p);

          if (tile.equals(START)) {
            // Find equivalent
            tile = pipeGrid.getStartEquivalent();
          }

          // Check if we are starting a horizontal segment from left to right
          if (startH.contains(tile)) {
            horizontalSegmentStart = tile;
          } else if (endH.contains(tile)) {
            // Only counts if it goes from N -> S or S to N
            if (horizontalSegmentStart.equals(BEND_NE) && tile.equals(BEND_SW)
                || horizontalSegmentStart.equals(BEND_SE) && tile.equals(BEND_NW)) {
              numIntersections++;
            }
            horizontalSegmentStart = null;
          } else if (horizontalSegmentStart == null) {
            numIntersections++;
          }
        } else if (numIntersections % 2 != 0) {
          inside++;
        }
      }
    }

    return inside;
  }
}
