package com.markphilpot;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

public class Guard {
  private static final Logger log = LogManager.getLogger(Guard.class);

  public Point loc;
  public Direction direction;
  public Set<Point> obstructions;
  public Set<Point> seen = new HashSet<>();
  public int gridX;
  public int gridY;

  public static Guard parse(InputStream inputStream) {
    var obstructions = new HashSet<Point>();
    Point start = null;
    Direction direction = null;

    var grid = ParsingUtils.streamToGrid(inputStream);

    var gridY = grid.size();
    var gridX = grid.getFirst().size();

    for(int y = 0; y < gridY; y++) {
      for(int x = 0; x < gridX; x++) {
        var tile = grid.get(y).get(x);

        switch (tile) {
          case "#":
            obstructions.add(new Point(x, y));
            break;
          case "^":
            start = new Point(x, y);
            direction = Direction.NORTH;
            break;
          case ">":
            start = new Point(x, y);
            direction = Direction.EAST;
            break;
          case "v":
            start = new Point(x, y);
            direction = Direction.SOUTH;
            break;
          case "<":
            start = new Point(x, y);
            direction = Direction.WEST;
            break;
        }
      }
    }

    if(start == null || direction == null) {
      throw new RuntimeException("Guard Not Found");
    }

    return new Guard(start, direction, obstructions, gridX, gridY);
  }

  public Guard(Point start, Direction startDir, Set<Point> obstructions, int gridX, int gridY) {
    this.loc = start;
    this.direction = startDir;
    this.obstructions = obstructions;
    this.gridX = gridX;
    this.gridY = gridY;

    log.info("Starting %s %s".formatted(loc, direction));
  }

  public int walk() {
    do {
      // Look
      var next = switch (direction) {
        case NORTH -> new Point(loc.x(), loc.y()-1);
        case SOUTH -> new Point(loc.x(), loc.y()+1);
        case EAST -> new Point(loc.x()+1, loc.y());
        case WEST -> new Point(loc.x()-1, loc.y());
      };

      var isOb = obstructions.contains(next);

      if(isOb) {
//        log.info("Turning right at %s (%s steps)".formatted(loc, numLocs));
        turnRight();
        continue;
      }

      // Walk
      seen.add(loc);
      loc = switch (direction) {
        case NORTH -> new Point(loc.x(), loc.y()-1);
        case SOUTH -> new Point(loc.x(), loc.y()+1);
        case EAST -> new Point(loc.x()+1, loc.y());
        case WEST -> new Point(loc.x()-1, loc.y());
      };

//      log.info("NOW :: %s (%s steps)".formatted(loc, numLocs));

    } while(isOnMap());

    return seen.size();
  }

  private boolean isOnMap() {
    return loc.x() >= 0 && loc.x() < gridX && loc.y() >= 0 &&  loc.y() < gridY;
  }

  private void turnRight() {
    direction = switch (direction) {
      case NORTH -> Direction.EAST;
      case SOUTH -> Direction.WEST;
      case EAST -> Direction.SOUTH;
      case WEST -> Direction.NORTH;
    };
  }
}
