package com.markphilpot;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class HomeworkGrid {
  private static final Logger log = LogManager.getLogger(HomeworkGrid.class);

  public record HWG(String[][] grid, List<String> operators) {}

  public static long compute(HWG hwg) {
    var total = 0L;

    var currentOperatorIndex = 0;
    var currentValues = new ArrayList<Long>();

    // For each column
    for(var x = 0; x < hwg.grid.length + 1; x++) {
      if(x == hwg.grid.length || isColumnBlank(hwg.grid, x)) {
        var op = hwg.operators.get(currentOperatorIndex);
        var miniTotal = switch(op) {
          case "+" -> currentValues.stream().reduce(0L, Long::sum);
          case "*" -> currentValues.stream().reduce(1L, (a, b) -> a * b);
          default -> throw new IllegalStateException("Unexpected value: " + op);
        };
        total += miniTotal;
        currentOperatorIndex++;
        currentValues.clear();
      } else {
        currentValues.add(getColumnValue(hwg.grid, x));
      }
    }

    return total;
  }

  private static boolean isColumnBlank(String[][] grid, int column) {
    for(var y = 0; y < grid[column].length; y++) {
      if(!grid[column][y].isBlank()) {
        return false;
      }
    }
    return true;
  }

  private static long getColumnValue(String[][] grid, int column) {
    var sb = new StringBuilder();
    for(var y = 0; y < grid[column].length; y++) {
      sb.append(grid[column][y]);
    }
    return Long.parseLong(sb.toString().trim());
  }

  public static HWG parse(InputStream inputStream, int dim) {
    var lines = ParsingUtils.streamToList(inputStream);

    var maxLength = lines.stream().mapToInt(String::length).max().orElse(0);

    // By inspection
    var grid = new String[maxLength][dim];

    for (int i = 0; i < lines.size() - 1; i++) {
      var line = lines.get(i);
      for(int j = 0; j < maxLength; j++) {
        if(j >= line.length()) {
          grid[j][i] = "";
        } else {
          grid[j][i] = line.substring(j, j + 1);
        }
      }
    }

    var operators = new ArrayList<String>();

    try(var scanner = new Scanner(lines.getLast())) {
      while (scanner.hasNext()) {
        var op = scanner.next();
        operators.add(op);
      }
    }

    return new HWG(grid, operators);
  }
}
