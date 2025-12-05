package com.markphilpot;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

public class RollDiagramTest {
  private static final Logger log = LogManager.getLogger(RollDiagramTest.class);

  @Test
  public void testSamplePart1() {
    var inputStream = RollDiagramTest.class.getClassLoader().getResourceAsStream("sample.txt");

    var diagram = RollDiagram.parse(inputStream);

    var num = RollDiagram.numRollsFree(diagram);

    assertThat(num, is(13));
  }

  @Test
  public void testInputPart1() {
    var inputStream = RollDiagramTest.class.getClassLoader().getResourceAsStream("input.txt");

    var diagram = RollDiagram.parse(inputStream);

    var num = RollDiagram.numRollsFree(diagram);

    log.info(num);
  }

  @Test
  public void testSamplePart2() {
    var inputStream = RollDiagramTest.class.getClassLoader().getResourceAsStream("sample.txt");

    var diagram = RollDiagram.parse(inputStream);

    var total = 0;

//    log.info(diagram.toString());

    var found = RollDiagram.numRollsFree(diagram);
    while(found > 0) {
//      log.info(found);
      total += found;
      RollDiagram.removeFreeRolls(diagram);
//      log.info(diagram.toString());
      found = RollDiagram.numRollsFree(diagram);
    }

    assertThat(total, is(43));
  }

  @Test
  public void testInputPart2() {
    var inputStream = RollDiagramTest.class.getClassLoader().getResourceAsStream("input.txt");

    var diagram = RollDiagram.parse(inputStream);

    var total = 0;

//    log.info(diagram.toString());

    var found = RollDiagram.numRollsFree(diagram);
    while(found > 0) {
//      log.info(found);
      total += found;
      RollDiagram.removeFreeRolls(diagram);
//      log.info(diagram.toString());
      found = RollDiagram.numRollsFree(diagram);
    }

    log.info(total);
  }
}
