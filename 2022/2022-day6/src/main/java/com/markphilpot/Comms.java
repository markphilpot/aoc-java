package com.markphilpot;

import java.util.ArrayDeque;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Comms {
  private static final Logger log = LogManager.getLogger(Comms.class);

  public static Integer findMarker(String input, int size) {
    var inputList = ParsingUtils.lineToList(input);
    var deque = new ArrayDeque<String>(size);

    for (var i = 0; i < inputList.size(); i++) {
      if (i != 0 && deque.size() == size) {
        deque.removeLast();
      }
      deque.push(inputList.get(i));

      if (deque.size() == size) {
        var check = deque.stream().distinct().toList();
        if (check.size() == size) {
          return i + 1;
        }
      }
    }

    return null;
  }
}
