package com.markphilpot;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;
import java.util.List;
import java.util.Scanner;

public class CombinationSafe {
  private static final Logger log = LogManager.getLogger(CombinationSafe.class);

  public enum Direction {
    L, R
  }

  public record Entry(Direction direction, int index) {}

  private int current;
  private final int range;

  public CombinationSafe() {
    this.range = 100;
    this.current = 50;
  }

  public void apply(Direction direction, int distance) {
    switch (direction) {
      case L: {
        var x = current - (distance % range);
        current = x < 0 ? x + range : x;
        break;
      }
      case R: {
        var x = current + (distance % range);
        current = x > range -1 ? x % range : x;
        break;
      }
    }
  }

  public boolean isZero() {
    return current == 0;
  }

  public void reset() {
    current = 50;
  }

  public int getCurrent() {
    return current;
  }

  public static List<Entry> parse(InputStream inputStream) {
    return ParsingUtils.streamToList(inputStream).stream().map(line -> {
      var dir = line.charAt(0) == 'L' ? CombinationSafe.Direction.L : CombinationSafe.Direction.R;
      var dist = Integer.parseInt(line.substring(1));
      return new CombinationSafe.Entry(dir, dist);
    }).toList();
  }

  /**
   * Slightly cleaner alternative using Scanner...
   *
   * Converts an input stream of encoded entries into a list of {@code CombinationSafe.Entry} objects.
   * Each line in the input stream is expected to contain a direction (either "L" or "R") followed
   * by a numerical distance, e.g., "L10" or "R5".
   *
   * @param inputStream the input stream containing the encoded entries as text lines
   * @return a list of {@code CombinationSafe.Entry} objects parsed from the input stream
   */
  public static List<Entry> parseAlt(InputStream inputStream) {
    return ParsingUtils.streamToList(inputStream).stream().map(line -> {
      var s = new Scanner(line);
      var dir = Direction.valueOf(s.findInLine("[LR]"));
      var dist = Integer.parseInt(s.findInLine("\\d*"));
      return new CombinationSafe.Entry(dir, dist);
    }).toList();
  }
}
