package com.markphilpot;

import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Dish {
  private static final Logger log = LogManager.getLogger(Dish.class);

  public enum Tile {
    ROUND("O"),
    CUBE("#"),
    EMPTY(".");

    private final String value;

    Tile(String o) {
      this.value = o;
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
  }

  public static class Grid {
    private List<List<Tile>> points;

    public Grid(List<List<Tile>> points) {
      this.points = points;
    }

    public Tile get(int x, int y) {
      return points.get(y).get(x);
    }

    public void set(int x, int y, Tile t) {
      points.get(y).set(x, t);
    }

    public void unset(int x, int y) {
      points.get(y).set(x, Tile.EMPTY);
    }

    public int xDim() {
      return points.getFirst().size();
    }

    public int yDim() {
      return points.size();
    }
  }

  public static Grid parse(InputStream inputStream) {
    try (var scanner = new Scanner(inputStream)) {
      var y = 0;
      var points = new ArrayList<List<Tile>>();

      while (scanner.hasNextLine()) {
        var line = ParsingUtils.lineToList(scanner.nextLine());
        points.add(
            new ArrayList<>(
                StreamUtils.zipWithIndex(line.stream())
                    .map(i -> Tile.fromString(i.value()))
                    .toList()));
        y++;
      }

      return new Grid(points);
    }
  }

  public static record CacheKey(String map, String direction) {}

  public static Set<CacheKey> cache = new HashSet<>();

  public static void spin(Grid grid) {
    CacheKey cacheKey = null;

    rollNorth(grid);
    //        cacheKey = new CacheKey(render(grid), "N");
    //        if(cache.contains(cacheKey)) {
    //            log.info("NORTH CACHE");
    //        } else {
    //            cache.add(cacheKey);
    //        }
    //        log.info("After North\n%s\n".formatted(render(grid)));
    rollWest(grid);
    //        cacheKey = new CacheKey(render(grid), "W");
    //        if(cache.contains(cacheKey)) {
    //            log.info("WEST CACHE");
    //        } else {
    //            cache.add(cacheKey);
    //        }
    //        log.info("After West\n%s\n".formatted(render(grid)));
    rollSouth(grid);
    //        cacheKey = new CacheKey(render(grid), "S");
    //        if(cache.contains(cacheKey)) {
    //            log.info("SOUTH CACHE");
    //        } else {
    //            cache.add(cacheKey);
    //        }
    //        log.info("After South\n%s\n".formatted(render(grid)));
    rollEast(grid);
    //        cacheKey = new CacheKey(render(grid), "E");
    //        if(cache.contains(cacheKey)) {
    //            log.info("EAST CACHE");
    //        } else {
    //            cache.add(cacheKey);
    //        }
    //        log.info("After East\n%s\n".formatted(render(grid)));
  }

  public static void rollNorth(Grid grid) {
    for (var x = 0; x < grid.xDim(); x++) {
      for (var y = 0; y < grid.yDim(); y++) {
        var tile = grid.get(x, y);

        if (tile.equals(Tile.ROUND)) {
          var targetY = y;

          while (targetY - 1 >= 0 && grid.get(x, targetY - 1).equals(Tile.EMPTY)) {
            targetY--;
          }

          if (targetY != y) {
            // Move it
            grid.unset(x, y);
            grid.set(x, targetY, Tile.ROUND);
          }
        }
      }
    }
  }

  public static void rollSouth(Grid grid) {
    for (var x = grid.xDim() - 1; x >= 0; x--) {
      for (var y = grid.yDim() - 1; y >= 0; y--) {
        var tile = grid.get(x, y);

        if (tile.equals(Tile.ROUND)) {
          var targetY = y;

          while (targetY + 1 < grid.yDim() && grid.get(x, targetY + 1).equals(Tile.EMPTY)) {
            targetY++;
          }

          if (targetY != y) {
            // Move it
            grid.unset(x, y);
            grid.set(x, targetY, Tile.ROUND);
          }
        }
      }
    }
  }

  public static void rollWest(Grid grid) {
    for (var x = 0; x < grid.xDim(); x++) {
      for (var y = 0; y < grid.yDim(); y++) {
        var tile = grid.get(x, y);

        if (tile.equals(Tile.ROUND)) {
          var targetX = x;

          while (targetX - 1 >= 0 && grid.get(targetX - 1, y).equals(Tile.EMPTY)) {
            targetX--;
          }

          if (targetX != x) {
            // Move it
            grid.unset(x, y);
            grid.set(targetX, y, Tile.ROUND);
          }
        }
      }
    }
  }

  public static void rollEast(Grid grid) {
    for (var x = grid.xDim() - 1; x >= 0; x--) {
      for (var y = grid.yDim() - 1; y >= 0; y--) {
        var tile = grid.get(x, y);

        if (tile.equals(Tile.ROUND)) {
          var targetX = x;

          while (targetX + 1 < grid.xDim() && grid.get(targetX + 1, y).equals(Tile.EMPTY)) {
            targetX++;
          }

          if (targetX != x) {
            // Move it
            grid.unset(x, y);
            grid.set(targetX, y, Tile.ROUND);
          }
        }
      }
    }
  }

  public static int findTotalLoad(Grid grid) {
    var loads = new ArrayList<Integer>();
    for (var x = 0; x < grid.xDim(); x++) {
      for (var y = 0; y < grid.yDim(); y++) {
        var tile = grid.get(x, y);

        if (tile.equals(Tile.ROUND)) {
          loads.add(grid.yDim() - y);
        }
      }
    }
    return loads.stream().reduce(0, Integer::sum);
  }

  public static String render(Grid grid) {
    return grid.points.stream()
        .map(x -> String.join("", x.stream().map(Tile::getValue).toList()))
        .collect(Collectors.joining("\n"));
  }

  public static record Cycle(int offset, int length) {}

  public static Cycle findCycle(Grid grid) {
    var cache = new HashMap<String, Integer>();

    var numSpins = 0;
    String render = null;

    while (true) {
      spin(grid);
      numSpins++;

      render = render(grid);

      if (cache.containsKey(render)) {
        var offset = cache.get(render);
        return new Cycle(offset, numSpins - offset);
      } else {
        cache.put(render, numSpins);
      }
    }
  }
}
