package com.markphilpot;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GuardLoop {
  private static final Logger log = LogManager.getLogger(GuardLoop.class);

  public Point loc;
  public Direction direction;
  public Set<Point> obstructions;
  public Set<Path> seen = new HashSet<>();
  public int gridX;
  public int gridY;

  public static List<GuardLoop> parse(InputStream inputStream) {
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

    var list = new ArrayList<GuardLoop>();
    list.add(new GuardLoop(start, direction, obstructions, gridX, gridY));

    for(int y = 0; y < gridY; y++) {
      for (int x = 0; x < gridX; x++) {
        var tile = grid.get(y).get(x);

        if(tile.equals(".")) {
          var newObstructions = new HashSet<>(obstructions);
          newObstructions.add(new Point(x, y));

          list.add(new GuardLoop(start, direction, newObstructions, gridX, gridY));
        }
      }
    }

    return list;
  }

  public GuardLoop(Point start, Direction startDir, Set<Point> obstructions, int gridX, int gridY) {
    this.loc = start;
    this.direction = startDir;
    this.obstructions = obstructions;
    this.gridX = gridX;
    this.gridY = gridY;
  }

  public boolean walk() {
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
        turnRight();
        continue;
      }

      // Walk
      var p = new Path(loc, direction);

      if(seen.contains(p)) {
        // Loop found
        return true;
      }

      seen.add(new Path(loc, direction));
      loc = switch (direction) {
        case NORTH -> new Point(loc.x(), loc.y()-1);
        case SOUTH -> new Point(loc.x(), loc.y()+1);
        case EAST -> new Point(loc.x()+1, loc.y());
        case WEST -> new Point(loc.x()-1, loc.y());
      };
    } while(isOnMap());

    return false;
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
