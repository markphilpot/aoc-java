package com.markphilpot;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import com.microsoft.z3.Solver;

public class MachineLightsZ3 {

  public static int solve(InputStream inputStream) {
    var total = 0;

    var lines = ParsingUtils.streamToList(inputStream);

    for(var line : lines) {
      var parts = List.of(line.split(" "));
      var lights = parts.getFirst();
      var target = parts.getLast();
      var buttons = parts.subList(1, parts.size() - 1);

      var joltages = Arrays.stream(target.substring(0, target.length() - 1).split(",")).map(Integer::parseInt).toList();
    }

    return total;
  }
}
