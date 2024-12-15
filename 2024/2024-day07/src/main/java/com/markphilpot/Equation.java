package com.markphilpot;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public record Equation(Long result, List<Long> operands) {
  private static final Logger log = LogManager.getLogger(Equation.class);

  public static Equation parse(String line) {
    var els = line.split(": ");
    var result = Long.parseLong(els[0]);
    var operands = ParsingUtils.readLongs(els[1]).toList();
    return new Equation(result, operands);
  }

  public boolean isValid() {
    var operators = new ArrayList<List<String>>();
    helper(operands.size()-1, new ArrayList<>(), operators);

//    log.info(operators);

    for(var opSet : operators) {
      var x = operands.getFirst();
      for (int i = 1; i < operands.size(); i++) {
        var next = operands.get(i);
        var op = opSet.get(i-1);

        x = switch (op) {
          case "+" -> x + next;
          case "*" -> x * next;
          default -> throw new RuntimeException("Invalid operator");
        };
      }

//      log.info("%s %s".formatted(result, x));
      if(result.equals(x)) {
        return true;
      }
    }

    return false;
  }

  public void helper(int size, List<String> current, List<List<String>> result) {
    if(current.size() == size) {
      result.add(new ArrayList<>(current));
      return;
    }

    current.add("+");
    helper(size, current, result);
    current.remove(current.size() -1);

    current.add("*");
    helper(size, current, result);
    current.remove(current.size() -1);
  }
}
