package com.markphilpot;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class StringUtils {
  private static final Logger log = LogManager.getLogger(StringUtils.class);

  public static <T extends Object> List<T> reverse(List<T> input) {
    var rList = new ArrayList<T>();
    new LinkedList<>(input).descendingIterator().forEachRemaining(rList::add);
    return rList;
  }
}
