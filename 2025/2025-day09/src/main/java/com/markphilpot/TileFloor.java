package com.markphilpot;

import com.markphilpot.Geometry.Point2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class TileFloor {
  private static final Logger log = LogManager.getLogger(TileFloor.class);

  public record Line(Point2 start, Point2 end) {}
  public record Box(Point2 a, Point2 b, long area) {};

  public static long findLargestAreaInside(List<Point2> points) {
    var lines = new ArrayList<Line>();

    for(int i = 0; i < points.size(); i++) {
      var firstIndex = i;
      var lastIndex = (firstIndex+1) % points.size(); // Wrap
      lines.add(new Line(points.get(firstIndex), points.get(lastIndex)));
    }

    var boxes = findAllAreas(points);
    boxes.sort((a, b) -> Long.compare(b.area, a.area));

    Box largestBox = null;

    for(var box : boxes) {
      var all = lines.stream().allMatch(line -> {
        var leftOf = Math.max(box.a.x(), box.b.x()) <= Math.min(line.start.x(), line.end.x());
        var rightOf = Math.min(box.a.x(), box.b.x()) >= Math.max(line.start.x(), line.end.x());
        var above = Math.max(box.a.y(), box.b.y()) <= Math.min(line.start.y(), line.end.y());
        var below = Math.min(box.a.y(), box.b.y()) >= Math.max(line.start.y(), line.end.y());

        return leftOf || rightOf || above || below;
      });

      if(all) {
        largestBox = box;
        break;
      }
    }

    return largestBox.area;
  }

  public static List<Box> findAllAreas(List<Point2> points) {
    var boxes = new ArrayList<Box>();

    for(int i = 0; i < points.size(); i++) {
      for(int j = i+1; j < points.size(); j++) {
        var area = findArea(points.get(i), points.get(j));
        boxes.add(new Box(points.get(i), points.get(j), area));
      }
    }
    return boxes;
  }

  public static long findLargestArea(List<Point2> points) {
    var largestArea = 0L;
    for(int i = 0; i < points.size(); i++) {
      for(int j = i+1; j < points.size(); j++) {
        var area = findArea(points.get(i), points.get(j));
        if(area > largestArea) {
          largestArea = area;
        }
      }
    }
    return largestArea;
  }

  public static long findArea(Point2 a, Point2 b) {
    var width = Math.abs(a.x() - b.x()) + 1;
    var height = Math.abs(a.y() - b.y()) + 1;
    return (long) (width * height);
  }

  public static List<Point2> parse(InputStream inputStream) {
    return ParsingUtils.streamToList(inputStream).stream().map(line -> {
      var parts = line.split(",");
      return new Point2(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
    }).toList();
  }
}
