package com.markphilpot;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.IntStream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Telescope {
  private static final Logger log = LogManager.getLogger(Telescope.class);

  public record Galaxy(int id, long x, long y) {}

  public record Pair(Galaxy a, Galaxy b, long numSteps) {}

  public record Image(List<Galaxy> galaxies, long numRows, long numCols) {}

  public static Image parse(InputStream inputStream) {
    try (var scanner = new Scanner(inputStream)) {
      int row = 0;
      int col = 0;
      var galaxyId = 1;
      var galaxies = new ArrayList<Galaxy>();

      while (scanner.hasNextLine()) {
        var line = scanner.nextLine();

        col = line.length();

        var galaxyIndexes =
            IntStream.iterate(line.indexOf("#"), i -> i >= 0, i -> line.indexOf("#", i + 1))
                .boxed()
                .toList();

        for (var index : galaxyIndexes) {
          galaxies.add(new Galaxy(galaxyId++, index, row));
        }

        row++;
      }

      return new Image(galaxies, row, col);
    }
  }

  public static Image handleExpansion(Image init, long scaleFactor) {
    var galaxies = init.galaxies;

    var finalNumRows = init.numRows;
    var finalNumCols = init.numCols;

    // Scale factor of 1 is "identity" (i.e. don't add rows)
    var increment = scaleFactor - 1;

    // row expansion
    for (var y = init.numRows - 1; y >= 0; y--) {
      var gBefore = new ArrayList<Galaxy>();
      var gThis = new ArrayList<Galaxy>();
      var gAfter = new ArrayList<Galaxy>();

      for (var galaxy : galaxies) {
        if (galaxy.y < y) {
          gBefore.add(galaxy);
        } else if (galaxy.y == y) {
          gThis.add(galaxy);
        } else {
          gAfter.add(galaxy);
        }
      }

      // Rewrite galaxies
      galaxies = new ArrayList<>(gBefore);
      boolean expand;

      if (gThis.isEmpty()) {
        log.info("Expanding row %d".formatted(y));
        expand = true;
        finalNumRows += increment;
      } else {
        expand = false;
        galaxies.addAll(gThis);
      }

      galaxies.addAll(
          gAfter.stream().map(g -> new Galaxy(g.id, g.x, g.y + (expand ? increment : 0))).toList());
    }

    // col expansion
    for (var x = init.numCols - 1; x >= 0; x--) {
      var gBefore = new ArrayList<Galaxy>();
      var gThis = new ArrayList<Galaxy>();
      var gAfter = new ArrayList<Galaxy>();

      for (var galaxy : galaxies) {
        if (galaxy.x < x) {
          gBefore.add(galaxy);
        } else if (galaxy.x == x) {
          gThis.add(galaxy);
        } else {
          gAfter.add(galaxy);
        }
      }

      // Rewrite galaxies
      galaxies = new ArrayList<>(gBefore);
      boolean expand;

      if (gThis.isEmpty()) {
        log.info("Expanding col %d".formatted(x));
        expand = true;
        finalNumCols += increment;
      } else {
        expand = false;
        galaxies.addAll(gThis);
      }

      galaxies.addAll(
          gAfter.stream().map(g -> new Galaxy(g.id, g.x + (expand ? increment : 0), g.y)).toList());
    }

    galaxies.sort(Comparator.comparingInt(a -> a.id));

    return new Image(galaxies, finalNumRows, finalNumCols);
  }

  public static long findDistance(Galaxy a, Galaxy b) {
    return Math.abs(a.x - b.x) + Math.abs(a.y - b.y);
  }

  public static List<Pair> findDistances(Image image) {
    var pairs = new ArrayList<Pair>();

    for (var i = 0; i < image.galaxies.size(); i++) {
      var a = image.galaxies.get(i);

      for (var j = i; j < image.galaxies.size(); j++) {
        var b = image.galaxies.get(j);

        if (!a.equals(b)) {
          pairs.add(new Pair(a, b, findDistance(a, b)));
        }
      }
    }

    log.info("Found %d pairs from %d galaxies...".formatted(pairs.size(), image.galaxies.size()));

    return pairs;
  }
}
