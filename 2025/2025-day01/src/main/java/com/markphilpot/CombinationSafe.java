package com.markphilpot;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CombinationSafe {
  private static final Logger log = LogManager.getLogger(CombinationSafe.class);

  public static enum Direction {
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
}
