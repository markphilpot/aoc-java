package com.markphilpot;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Homework {
  private static final Logger log = LogManager.getLogger(Homework.class);

  public record Column(List<Long> values, String op) {
    public long compute() {
      if(op.equals("+")) {
        // Addition
        return values.stream().mapToLong(Long::longValue).sum();
      } else {
        // Multiplication
        return values.stream().mapToLong(Long::longValue).reduce(1L, (a, b) -> a * b);
      }
    }
  }

  public static List<Column> parse(InputStream inputStream) {
    var lines = ParsingUtils.streamToList(inputStream);

    var result = new ArrayList<Column>();
    var columns = new ArrayList<List<Long>>();

    for(var line : lines) {
      if(line.startsWith("+") || line.startsWith("*")) {
        try(var scanner = new Scanner(line)) {
          var index = 0;
          while (scanner.hasNext()) {
            var op = scanner.next();
            result.add(new Column(columns.get(index), op));
            index++;
          }
        }
      } else if(columns.isEmpty()) {
        var values = new ArrayList<Long>();
        try(var scanner = new Scanner(line)) {
          while (scanner.hasNextLong()) {
            values.add(scanner.nextLong());
          }
        }
        for(var value: values) {
          var list = new ArrayList<Long>();
          list.add(value);
          columns.add(list);
        }
      } else {
        try(var scanner = new Scanner(line)) {
          var index = 0;
          while (scanner.hasNextLong()) {
            columns.get(index).add(scanner.nextLong());
            index++;
          }
        }
      }
    }

    return result;
  }
}
