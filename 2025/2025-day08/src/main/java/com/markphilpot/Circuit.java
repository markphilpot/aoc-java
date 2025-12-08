package com.markphilpot;

import java.util.HashSet;
import java.util.Set;

public class Circuit implements Comparable<Circuit> {
  private final Set<Geometry.Point3> points;

  public Circuit(Set<Geometry.Point3> points) {
    this.points = new HashSet<>(points);
  }

  public Set<Geometry.Point3> getPoints() {
    return points;
  }

  public int size() {
    return points.size();
  }

  @Override
  public int compareTo(Circuit o) {
    return points.size() - o.points.size();
  }

  public static Circuit combine(Circuit a, Circuit b) {
    var combinedPoints = new HashSet<Geometry.Point3>();
    combinedPoints.addAll(a.points);
    combinedPoints.addAll(b.points);
    return new Circuit(combinedPoints);
  }

  @Override
  public String toString() {
    return "Circuit{" +
            "points=" + points +
            '}';
  }
}
