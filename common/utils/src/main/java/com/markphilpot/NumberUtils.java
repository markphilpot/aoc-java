package com.markphilpot;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NumberUtils {
  private static final Logger log = LogManager.getLogger(NumberUtils.class);

  public static int product(int a, int b) {
    return a * b;
  }

  public static long product(long a, long b) {
    return a * b;
  }
}
