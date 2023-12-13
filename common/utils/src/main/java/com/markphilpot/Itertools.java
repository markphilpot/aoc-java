package com.markphilpot;

import java.util.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Itertools {
  private static final Logger log = LogManager.getLogger(Itertools.class);

  // https://stackoverflow.com/a/63434068
  public static <T> List<Collection<T>> product(Collection<T> a, int r) {
    List<Collection<T>> result = Collections.nCopies(1, Collections.emptyList());
    for (Collection<T> pool : Collections.nCopies(r, new LinkedHashSet<>(a))) {
      List<Collection<T>> temp = new ArrayList<>();
      for (Collection<T> x : result) {
        for (T y : pool) {
          Collection<T> z = new ArrayList<>(x);
          z.add(y);
          temp.add(z);
        }
      }
      result = temp;
    }
    return result;
  }
}
