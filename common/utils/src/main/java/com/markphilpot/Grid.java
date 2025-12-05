package com.markphilpot;

import static com.markphilpot.Grid.Direction.*;

import java.util.List;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Grid<T> {
  private static final Logger log = LogManager.getLogger(Grid.class);

  public int numRows() {
    return grid.size();
  }

  public int numCols() {
    return grid.getFirst().size();
  }

  public List<T> getRow(int y) {
    return grid.get(y);
  }

  public enum Direction {
    N,
    S,
    E,
    W;

    public Direction getInvert() {
      return switch (this) {
        case N -> S;
        case S -> N;
        case E -> W;
        case W -> E;
      };
    }
  }

  public record Point(int x, int y) {}

  public record RP(Point point, Direction d) {}

  public record RPV<T>(RP rc, T v) {}

  private final List<List<T>> grid;

  public Grid(List<List<T>> grid) {
    this.grid = grid;
  }

  public T get(Point c) {
    if(c.y < 0 || c.y >= grid.size() || c.x < 0 || c.x >= grid.getFirst().size()) {
      return null;
    }

    return grid.get(c.y).get(c.x);
  }

  public void set(Point c, T v) {
    grid.get(c.y).set(c.x, v);
  }

  public List<Optional<Point>> getAdjacent(Point c) {
    var targets =
        List.of(
            new Point(c.x - 1, c.y - 1),
            new Point(c.x, c.y - 1),
            new Point(c.x + 1, c.y - 1),
            new Point(c.x - 1, c.y),
            new Point(c.x + 1, c.y),
            new Point(c.x - 1, c.y + 1),
            new Point(c.x, c.y + 1),
            new Point(c.x + 1, c.y + 1));

    return targets.stream()
        .map(
            p ->
                p.x >= 0 && p.y >= 0 && p.x() < grid.getFirst().size() && p.y() < grid.size()
                    ? Optional.of(p)
                    : Optional.<Point>empty())
        .toList();
  }

  // Returns N,S,E,W
  public List<Optional<Point>> getOrthogonal(Point c) {
    var targets =
        List.of(
            new Point(c.x, c.y - 1),
            new Point(c.x, c.y + 1),
            new Point(c.x - 1, c.y),
            new Point(c.x + 1, c.y));

    return targets.stream()
        .map(
            p ->
                p.x >= 0 && p.y >= 0 && p.x() < grid.getFirst().size() && p.y() < grid.size()
                    ? Optional.of(p)
                    : Optional.<Point>empty())
        .toList();
  }

  public List<Optional<RP>> getRelOrthogonal(Point c) {
    var targets =
        List.of(
            new RP(new Point(c.x, c.y - 1), N),
            new RP(new Point(c.x, c.y + 1), S),
            new RP(new Point(c.x - 1, c.y), E),
            new RP(new Point(c.x + 1, c.y), W));

    return targets.stream()
        .map(
            p ->
                p.point.x >= 0
                        && p.point.y >= 0
                        && p.point.x < grid.getFirst().size()
                        && p.point.y < grid.size()
                    ? Optional.of(p)
                    : Optional.<RP>empty())
        .toList();
  }

  public List<Optional<RPV<T>>> getRelOrthoVal(Point p) {
    return getRelOrthogonal(p).stream()
        .map(rc -> rc.map(rp -> new RPV<>(rp, get(rp.point))))
        .toList();
  }

  public RPV<T> walk(Point p, Direction d) {
    var nextPoint =
        switch (d) {
          case N -> new RP(new Point(p.x, p.y - 1), S);
          case S -> new RP(new Point(p.x, p.y + 1), N);
          case E -> new RP(new Point(p.x + 1, p.y), W);
          case W -> new RP(new Point(p.x - 1, p.y), E);
        };
    return new RPV<T>(nextPoint, get(nextPoint.point));
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("\n");
    for (int y = 0; y < grid.size(); y++) {
      for (int x = 0; x < grid.get(y).size(); x++) {
        sb.append(grid.get(y).get(x));
      }
      if (y < grid.size() - 1) {
        sb.append("\n");
      }
    }
    return sb.toString();
  }
}
