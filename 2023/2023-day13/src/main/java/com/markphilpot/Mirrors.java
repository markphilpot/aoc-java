package com.markphilpot;

import java.io.InputStream;
import java.util.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Mirrors {
  private static final Logger log = LogManager.getLogger(Mirrors.class);

  public record Mirror(int x, int y) {}

  public record Region(
      List<Mirror> mirrors,
      Map<Integer, List<Mirror>> mirrorRows,
      Map<Integer, List<Mirror>> mirrorColumns,
      int numX,
      int numY) {}

  private static Map<Integer, List<Mirror>> buildRowMap(List<Mirror> mirrors, int numRows) {
    var rowMap = new HashMap<Integer, List<Mirror>>();

    for (var i = 0; i < numRows; i++) {
      int finalI = i;
      rowMap.put(i, mirrors.stream().filter(m -> m.y == finalI).toList());
    }

    return rowMap;
  }

  private static Map<Integer, List<Mirror>> buildColMap(List<Mirror> mirrors, int numCols) {
    var rowMap = new HashMap<Integer, List<Mirror>>();

    for (var i = 0; i < numCols; i++) {
      int finalI = i;
      rowMap.put(i, mirrors.stream().filter(m -> m.x == finalI).toList());
    }

    return rowMap;
  }

  public static List<Region> parse(InputStream inputStream) {
    try (var scanner = new Scanner(inputStream)) {
      var regions = new ArrayList<Region>();

      var numX = 0;
      var regionMirrors = new ArrayList<Mirror>();
      var y = 0;

      while (scanner.hasNextLine()) {
        var line = scanner.nextLine();

        if (line.isBlank()) {
          regions.add(
              new Region(
                  regionMirrors,
                  buildRowMap(regionMirrors, y),
                  buildColMap(regionMirrors, numX),
                  numX,
                  y));

          regionMirrors = new ArrayList<>();
          y = 0;
          numX = 0;
        } else {
          numX = line.length();
          int finalY = y;
          regionMirrors.addAll(
              ParsingUtils.findIndexesOf(line, "#").map(x -> new Mirror(x, finalY)).toList());
          y++;
        }
      }

      // Get last Region
      regions.add(
          new Region(
              regionMirrors,
              buildRowMap(regionMirrors, y),
              buildColMap(regionMirrors, numX),
              numX,
              y));

      return regions;
    }
  }

  private static boolean isRowReflection(List<Mirror> a, List<Mirror> b) {
    if (a.size() == b.size()) {
      for (var i = 0; i < a.size(); i++) {
        if (a.get(i).x != b.get(i).x) {
          return false;
        }
      }

      return true;
    }

    return false;
  }

  private static boolean isColReflection(List<Mirror> a, List<Mirror> b) {
    if (a.size() == b.size()) {
      for (var i = 0; i < a.size(); i++) {
        if (a.get(i).y != b.get(i).y) {
          return false;
        }
      }

      return true;
    }

    return false;
  }

  public static Optional<Integer> findRowReflectionIndex(Region region) {
    // For now going to assume regions are defined as a single refection (rather than needing to
    // find the "largest")
    var reflectionIndex = Optional.<Integer>empty();

    for (var y = 0; y < region.numY - 1; y++) {
      var nextUp = y;
      var nextDown = y + 1;
      var a = region.mirrorRows.get(nextUp);
      var b = region.mirrorRows.get(nextDown);

      if (isRowReflection(a, b)) {
        //                log.info("Found initial Row Reflection %d %d".formatted(y, y+1));
        nextUp = nextUp - 1;
        nextDown = nextDown + 1;

        var found = true;

        while (nextUp >= 0 && nextDown < region.numY) {
          a = region.mirrorRows.get(nextUp);
          b = region.mirrorRows.get(nextDown);

          if (!isRowReflection(a, b)) {
            found = false;
            break;
          }

          nextUp = nextUp - 1;
          nextDown = nextDown + 1;
        }

        if (found) {
          // Found reflection that went to edge
          reflectionIndex = Optional.of(y);
          break;
        }
      }
    }

    return reflectionIndex;
  }

  public static Optional<Integer> findColumnReflectionIndex(Region region) {
    var reflectionIndex = Optional.<Integer>empty();

    for (var x = 0; x < region.numX - 1; x++) {
      var nextLeft = x;
      var nextRight = x + 1;
      var a = region.mirrorColumns.get(x);
      var b = region.mirrorColumns.get(x + 1);

      if (isColReflection(a, b)) {
        //                log.info("Found initial Col Reflection %d %d".formatted(x, x+1));
        nextLeft = nextLeft - 1;
        nextRight = nextRight + 1;

        var found = true;

        while (nextLeft >= 0 && nextRight < region.numX) {
          a = region.mirrorColumns.get(nextLeft);
          b = region.mirrorColumns.get(nextRight);

          if (!isColReflection(a, b)) {
            found = false;
            break;
          }

          nextLeft = nextLeft - 1;
          nextRight = nextRight + 1;
        }

        if (found) {
          // Found reflection that went to edge
          reflectionIndex = Optional.of(x);
          break;
        }
      }
    }

    return reflectionIndex;
  }

  public static Integer getRegionScore(Optional<Integer> rowIndex, Optional<Integer> colIndex) {
    if (rowIndex.isPresent() && colIndex.isPresent()) {
      throw new IllegalStateException("Found two reflections!");
    } else if (rowIndex.isEmpty() && colIndex.isEmpty()) {
      throw new IllegalStateException("Found no reflections");
    }

    return rowIndex
        .map(integer -> (integer + 1) * 100)
        .orElseGet(() -> (colIndex.orElseThrow(IllegalStateException::new) + 1));
  }
}
