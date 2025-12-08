package com.markphilpot;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

public class JunctionBoxesTest {
  private static final Logger log = LogManager.getLogger(JunctionBoxesTest.class);

  @Test
  public void testSamplePart1() {
    var inputStream = JunctionBoxesTest.class.getClassLoader().getResourceAsStream("sample.txt");

    var points = JunctionBoxes.parse(inputStream);

    var circuits = new ArrayList<Circuit>();
    for (Geometry.Point3 p : points) {
      circuits.add(new Circuit(Set.of(p)));
    }

    var distances = JunctionBoxes.findDistances(points);

    distances.sort(Comparator.comparingDouble(JunctionBoxes.BoxDistance::distance));

    for(int i = 0; i < 10; i++) {
      var bd = distances.get(i);

      var ca = circuits.stream().filter(c -> c.getPoints().contains(bd.a())).findFirst().orElseThrow(IllegalStateException::new);
      var cb = circuits.stream().filter(c -> c.getPoints().contains(bd.b())).findFirst().orElseThrow(IllegalStateException::new);

      circuits.remove(ca);
      circuits.remove(cb);

      circuits.add(Circuit.combine(ca, cb));
    }

    circuits.sort(Comparator.comparingInt(Circuit::size).reversed());

    // take top 3 from circuits
    var num = circuits.stream().limit(3)
            .mapToInt(Circuit::size).reduce(1, (a, b) -> a * b);

    assertThat(num, is(40));
  }

  @Test
  public void testInputPart1() {
    var inputStream = JunctionBoxesTest.class.getClassLoader().getResourceAsStream("input.txt");

    var points = JunctionBoxes.parse(inputStream);

    var circuits = new ArrayList<Circuit>();
    for (Geometry.Point3 p : points) {
      circuits.add(new Circuit(Set.of(p)));
    }

    var distances = JunctionBoxes.findDistances(points);

    distances.sort(Comparator.comparingDouble(JunctionBoxes.BoxDistance::distance));

    for(int i = 0; i < 1000; i++) {
      var bd = distances.get(i);

      var ca = circuits.stream().filter(c -> c.getPoints().contains(bd.a())).findFirst().orElseThrow(IllegalStateException::new);
      var cb = circuits.stream().filter(c -> c.getPoints().contains(bd.b())).findFirst().orElseThrow(IllegalStateException::new);

      circuits.remove(ca);
      circuits.remove(cb);

      circuits.add(Circuit.combine(ca, cb));
    }

    circuits.sort(Comparator.comparingInt(Circuit::size).reversed());

    // take top 3 from circuits
    var num = circuits.stream().limit(3)
            .mapToInt(Circuit::size).reduce(1, (a, b) -> a * b);

    log.info(num);
  }

  @Test
  public void testSamplePart2() {
    var inputStream = JunctionBoxesTest.class.getClassLoader().getResourceAsStream("sample.txt");

    var points = JunctionBoxes.parse(inputStream);

    var circuits = new ArrayList<Circuit>();
    for (Geometry.Point3 p : points) {
      circuits.add(new Circuit(Set.of(p)));
    }

    var distances = JunctionBoxes.findDistances(points);

    distances.sort(Comparator.comparingDouble(JunctionBoxes.BoxDistance::distance));

    var x1 = 0.0;
    var x2 = 0.0;

    while(circuits.size() > 1) {
      var bd = distances.removeFirst();

      var ca = circuits.stream().filter(c -> c.getPoints().contains(bd.a())).findFirst().orElseThrow(IllegalStateException::new);
      var cb = circuits.stream().filter(c -> c.getPoints().contains(bd.b())).findFirst().orElseThrow(IllegalStateException::new);

      circuits.remove(ca);
      circuits.remove(cb);

      circuits.add(Circuit.combine(ca, cb));

      if(circuits.size() == 1) {
        x1 = bd.a().x();
        x2 = bd.b().x();
      }
    }

    assertThat(x1 * x2, is(25272.0));
  }

  @Test
  public void testInputPart2() {
    var inputStream = JunctionBoxesTest.class.getClassLoader().getResourceAsStream("input.txt");

    var points = JunctionBoxes.parse(inputStream);

    var circuits = new ArrayList<Circuit>();
    for (Geometry.Point3 p : points) {
      circuits.add(new Circuit(Set.of(p)));
    }

    var distances = JunctionBoxes.findDistances(points);

    distances.sort(Comparator.comparingDouble(JunctionBoxes.BoxDistance::distance));

    var x1 = 0.0;
    var x2 = 0.0;

    while(circuits.size() > 1) {
      var bd = distances.removeFirst();

      var ca = circuits.stream().filter(c -> c.getPoints().contains(bd.a())).findFirst().orElseThrow(IllegalStateException::new);
      var cb = circuits.stream().filter(c -> c.getPoints().contains(bd.b())).findFirst().orElseThrow(IllegalStateException::new);

      circuits.remove(ca);
      circuits.remove(cb);

      circuits.add(Circuit.combine(ca, cb));

      if(circuits.size() == 1) {
        x1 = bd.a().x();
        x2 = bd.b().x();
      }
    }

    var num = x1 * x2;

    log.info((long) num);
  }
}
