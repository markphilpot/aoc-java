package com.markphilpot;

import org.apache.commons.lang3.StringUtils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PresentTetris {

  public record Shape(int index, int size) {}
  public record UnderTree(int x, int y, List<Integer> numPresents) {}
  public record Manifest(List<Shape> shapes, List<UnderTree> underTrees) {}

  public static boolean check(UnderTree ut, List<Shape> shapes) {
    var size = ut.x * ut.y;
    var needed = 0;
    for(var i = 0; i < ut.numPresents.size(); i++) {
      needed += ut.numPresents.get(i) * shapes.get(i).size;
    }
    return size >= needed;
  }

  public static Manifest parse(InputStream inputStream) {
    var lines = ParsingUtils.streamToList(inputStream);

    var shapes = new ArrayList<Shape>();
    var underTrees = new ArrayList<UnderTree>();

    var groups = ParsingUtils.splitByEmptyLines(lines);

    for(var i = 0; i < groups.size() - 1; i++) {
      var size = groups.get(i).stream().map(l -> StringUtils.countMatches(l, "#")).reduce(0, Integer::sum);
      shapes.add(new Shape(i, size));
    }

    var underTreeGroup = groups.getLast();

    for(var line : underTreeGroup) {
      var parts = line.split(":");
      var dim = parts[0].split("x");
      var numPresents = new ArrayList<Integer>();
      try(var scanner = new Scanner(parts[1])) {
        while(scanner.hasNextInt()) {
          numPresents.add(scanner.nextInt());
        }
      }
      underTrees.add(new UnderTree(Integer.parseInt(dim[0]), Integer.parseInt(dim[1]), numPresents));
    }

    return new Manifest(shapes, underTrees);
  }
}
