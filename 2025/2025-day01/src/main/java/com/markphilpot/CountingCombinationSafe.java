package com.markphilpot;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CountingCombinationSafe {
  private static final Logger log = LogManager.getLogger(CountingCombinationSafe.class);

  private int current;
  private final int range;

  private int numZeros;

  public CountingCombinationSafe() {
    this.range = 100;
    this.current = 50;
    this.numZeros = 0;
  }

  public void apply(CombinationSafe.Direction direction, int distance) {
    var passingClicks = distance / range;
    switch (direction) {
      case L: {
        var x = current - (distance % range);
        if(x < 0 && current != 0) {
          // Passed zero
          numZeros++;
        }
        current = x < 0 ? x + range : x;
        break;
      }
      case R: {
        var x = current + (distance % range);
        if(x > range) {
          numZeros++;
        }
        current = x > range - 1 ? x % range : x;
        break;
      }
    }
    if(current == 0) {
      numZeros++;
    }
    numZeros += passingClicks;
  }

  public int getNumZeros() {
    return numZeros;
  }
}
