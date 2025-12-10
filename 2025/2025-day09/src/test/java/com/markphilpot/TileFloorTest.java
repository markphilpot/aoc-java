package com.markphilpot;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import com.markphilpot.Geometry.Point2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

public class TileFloorTest {
  private static final Logger log = LogManager.getLogger(TileFloorTest.class);

  @Test
  public void testSamplePart1() {
    var inputStream = TileFloorTest.class.getClassLoader().getResourceAsStream("sample.txt");

    assertThat(TileFloor.findArea(new Point2(2, 5), new Point2(9, 7)), is(24));

    var points = TileFloor.parse(inputStream);

    var num = TileFloor.findLargestArea(points);

    assertThat(num, is(50L));
  }

  @Test
  public void testInputPart1() {
    var inputStream = TileFloorTest.class.getClassLoader().getResourceAsStream("input.txt");

    var points = TileFloor.parse(inputStream);

    var num = TileFloor.findLargestArea(points);

    // not 2147483647
    log.info(num);
  }

  @Test
  public void testSamplePart2() {
    var inputStream = TileFloorTest.class.getClassLoader().getResourceAsStream("sample.txt");

    var points = TileFloor.parse(inputStream);

    var num = TileFloor.findLargestAreaInside(points);

    assertThat(num, is(24L));
  }

  @Test
  public void testInputPart2() {
    var inputStream = TileFloorTest.class.getClassLoader().getResourceAsStream("input.txt");

    var points = TileFloor.parse(inputStream);

    var num = TileFloor.findLargestAreaInside(points);

    log.info(num);
  }
}
