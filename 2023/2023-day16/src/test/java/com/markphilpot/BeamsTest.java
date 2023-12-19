package com.markphilpot;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import java.io.IOException;
import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class BeamsTest {
  private static final Logger log = LogManager.getLogger(BeamsTest.class);

  @Test
  public void testSamplePart1() throws IOException {
    var inputStream = BeamsTest.class.getClassLoader().getResourceAsStream("sample.txt");

    var grid = Beams.parse(inputStream);
    var energized = Beams.followBeam(grid, new Beams.Point(0,0), Beams.Direction.W, new ArrayList<>());
    var total = energized.stream().distinct().count();

    assertThat(total, is(46L));
  }

  @Test
  public void testInputPart1() throws IOException {
    var inputStream = BeamsTest.class.getClassLoader().getResourceAsStream("input.txt");

    var grid = Beams.parse(inputStream);
    var energized = Beams.followBeam(grid, new Beams.Point(0,0), Beams.Direction.W, new ArrayList<>());
    var total = energized.stream().distinct().count();

    log.info(total);
  }

  @Test
  public void testSamplePart2() throws IOException {
    var inputStream = BeamsTest.class.getClassLoader().getResourceAsStream("sample.txt");

    var grid = Beams.parse(inputStream);
    var energized = Beams.followBeam(grid, new Beams.Point(3,0), Beams.Direction.N, new ArrayList<>());
    var total = energized.stream().distinct().count();

    assertThat(total, is(51L));
  }

  @Test
  public void testInputPart2() throws IOException {
    var inputStream = BeamsTest.class.getClassLoader().getResourceAsStream("input.txt");

    var grid = Beams.parse(inputStream);

    var enterN = IntStream.range(0, grid.xDim()).boxed().map(x -> new Beams.Point(x, 0)).toList();
    var enterS = IntStream.range(0, grid.xDim()).boxed().map(x -> new Beams.Point(x, grid.yDim()-1)).toList();
    var enterW = IntStream.range(0, grid.yDim()).boxed().map(y -> new Beams.Point(0, y)).toList();
    var enterE = IntStream.range(0, grid.yDim()).boxed().map(y -> new Beams.Point(grid.xDim() -1, y)).toList();

    var maxN = enterN.stream().map(p -> {
      Beams.cache.clear();
      return Beams.followBeam(grid, p, Beams.Direction.N, new ArrayList<>()).stream().distinct().count();
    }).toList();
    var maxS = enterS.stream().map(p -> {
      Beams.cache.clear();
      return Beams.followBeam(grid, p, Beams.Direction.S, new ArrayList<>()).stream().distinct().count();
    }).toList();
    var maxW = enterW.stream().map(p -> {
      Beams.cache.clear();
      return Beams.followBeam(grid, p, Beams.Direction.W, new ArrayList<>()).stream().distinct().count();
    }).toList();
    var maxE = enterE.stream().map(p -> {
      Beams.cache.clear();
      return Beams.followBeam(grid, p, Beams.Direction.E, new ArrayList<>()).stream().distinct().count();
    }).toList();

    var maxes = List.of(maxN, maxS, maxW, maxE);

    maxes.forEach(log::info);

    var max = maxes.stream().flatMap(Collection::stream).max(Comparator.comparingLong(Long::valueOf));

    log.info(max);
  }
}
