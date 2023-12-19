package com.markphilpot;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import java.io.IOException;
import java.util.stream.Collectors;

public class CrucibleTest {
  private static final Logger log = LogManager.getLogger(CrucibleTest.class);

  @Test
  public void testSamplePart1() throws IOException {
    var inputStream = CrucibleTest.class.getClassLoader().getResourceAsStream("sample.txt");

    var graph = Crucible.parse(inputStream);

    var startNode = graph.getMap().get(new Crucible.Point(0, 0));
    var endNode = graph.getMap().get(new Crucible.Point(graph.getxDim()-1, graph.getyDim()-1));
//    var endNode = graph.getMap().get(new Crucible.Point(8, 0));

//    log.info(startNode);
//    log.info(endNode);

    Crucible.dijkstra(startNode);

//    log.info(endNode);
    endNode.getShortestPath().forEach(n -> log.info("%s %d".formatted(n.getTile().p(), n.getTile().weight())));

    var total = endNode.getShortestPath().stream().map(n -> n.getTile().weight()).reduce(0, Integer::sum);

    // Sub start, add End
    total = total - startNode.getTile().weight() + endNode.getTile().weight();

    // Visualize
    var pathSet = endNode.getShortestPath().stream().map(n -> n.getTile().p()).collect(Collectors.toSet());
    var sb = new StringBuilder();
    sb.append("\n");
    for(var y = 0; y < graph.getyDim(); y++){
      for(var x = 0; x < graph.getxDim(); x++) {
//        sb.append("%03d ".formatted(graph.getMap().get(new Crucible.Point(x, y)).getDistance()));
        if(pathSet.contains(new Crucible.Point(x, y))) {
          sb.append("#");
        } else {
          sb.append(".");
        }
      }
      sb.append("\n");
    }
    log.info(sb.toString());

    assertThat(total, is(102));

//    assertThat(num, is(0));
  }

  @Test
  public void testInputPart1() throws IOException {
    var inputStream = CrucibleTest.class.getClassLoader().getResourceAsStream("input.txt");

    var graph = Crucible.parse(inputStream);

    var startNode = graph.getMap().get(new Crucible.Point(0, 0));
    var endNode = graph.getMap().get(new Crucible.Point(graph.getxDim()-1, graph.getyDim()-1));
    Crucible.dijkstra(startNode);

    var total = endNode.getShortestPath().stream().map(n -> n.getTile().weight()).reduce(0, Integer::sum);

    // Sub start, add End
    total = total - startNode.getTile().weight() + endNode.getTile().weight();

    // Visualize
    var pathSet = endNode.getShortestPath().stream().map(n -> n.getTile().p()).collect(Collectors.toSet());
    var sb = new StringBuilder();
    sb.append("\n");
    for(var y = 0; y < graph.getyDim(); y++){
      for(var x = 0; x < graph.getxDim(); x++) {
//        sb.append("%03d ".formatted(graph.getMap().get(new Crucible.Point(x, y)).getDistance()));
        if(pathSet.contains(new Crucible.Point(x, y))) {
          sb.append("#");
        } else {
          sb.append(".");
        }
      }
      sb.append("\n");
    }
    log.info(sb.toString());

    assertThat(total, is(not(984))); // Too high

    log.info(total);
  }

  @Test
  public void testSamplePart2() {
    var inputStream = CrucibleTest.class.getClassLoader().getResourceAsStream("sample.txt");

    var num = 0;

    assertThat(num, is(0));
  }

  @Test
  public void testInputPart2() {
    var inputStream = CrucibleTest.class.getClassLoader().getResourceAsStream("input.txt");

    var numRanges = 0;

    log.info(numRanges);
  }
}
