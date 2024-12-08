package com.markphilpot;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class ReportCheck {
  private static final Logger log = LogManager.getLogger(ReportCheck.class);

  public static boolean isSafe(List<Integer> input) {
    return isSafeIncreasing(input) || isSafeIncreasing(input.reversed());
  }

  public static boolean isSafeIncreasing(List<Integer> input) {
    Integer lastVal = null;

    for(Integer val : input) {
      if(lastVal == null) {
        lastVal = val;
      } else if(val > lastVal && val <= lastVal + 3) {
        lastVal = val;
      } else {
        return false;
      }
    }

    return true;
  }
  public static boolean isSafeDampening(List<Integer> input) {
    var safe = isSafeIncreasingDampening(input) ||
            isSafeIncreasingDampening(input.reversed());

    if(safe) {
      return true;
    }

    // Brute Force
    for(int i = 0; i < input.size(); i++) {
      var candidate = new ArrayList<>(input);
      candidate.remove(i);
      safe = isSafe(candidate) ||
              isSafe(candidate.reversed());
      if(safe) {
        return true;
      }
    }

    return false;
  }

  public static boolean isSafeIncreasingDampening(List<Integer> input) {
    Integer lastVal = null;
    boolean removed = false;

    for(int i = 0; i < input.size(); i++) {
      var val = input.get(i);

      if(lastVal == null) {
        lastVal = val;
      } else if(val > lastVal && val <= lastVal + 3) {
        lastVal = val;
      } else if(!removed) {
        removed = true;
      } else {
        return false;
      }
    }

    return true;
  }
}
