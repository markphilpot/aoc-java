package com.markphilpot;

public class Geometry {

  public record Point2(double x, double y) {
    public double distance(Point2 o) {
      return Math.sqrt(Math.pow(o.x - x, 2) + Math.pow(o.y - y, 2));
    }
  }
  public record Point3(double x, double y, double z) {
    public double distance(Point3 o) {
      return Math.sqrt(Math.pow(o.x - x, 2) + Math.pow(o.y - y, 2) + Math.pow(o.z - z, 2));
    }
  }

}
