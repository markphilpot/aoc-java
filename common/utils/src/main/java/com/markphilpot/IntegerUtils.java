package com.markphilpot;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class IntegerUtils {
  private static final Logger log = LogManager.getLogger(IntegerUtils.class);

  public static int product(int a, int b) {
    return a * b;
  }
}
