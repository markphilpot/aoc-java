package com.markphilpot;

import com.markphilpot.Geometry.Point3;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

public class JunctionBoxes {
  
  public record BoxDistance(Point3 a, Point3 b, double distance) {}

  public static List<BoxDistance> findDistances(List<Point3> points) {
    var distances = new ArrayList<BoxDistance>();

    for(int i = 0; i < points.size(); i++) {
      for(int j = i + 1; j < points.size(); j++) {
        var a = points.get(i);
        var b = points.get(j);
        var distance = a.distance(b);
        distances.add(new BoxDistance(a, b, distance));
      }
    }

    return distances;
  }

  public static List<Point3> parse(InputStream inputStream) {
    return ParsingUtils.streamToList(inputStream).stream().map(line -> {
      var parts = line.split(",");
      return new Point3(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
    }).toList();
  }
}
