package com.markphilpot;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import java.util.Arrays;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

public class SchematicTest {
  private static final Logger log = LogManager.getLogger(SchematicTest.class);

  @Test
  public void testSplit() {
    log.info(Arrays.stream("467..114..".split("\\.")).toList());
  }

  @Test
  public void regexTest() {
    assertThat(Schematic.isSymbol("1"), is(false));
    assertThat(Schematic.isSymbol("."), is(false));
    assertThat(Schematic.isSymbol("#"), is(true));
    assertThat(Schematic.isSymbol("&"), is(true));
  }

  @Test
  public void testSamplePart1() {
    var inputStream = SchematicTest.class.getClassLoader().getResourceAsStream("sample.txt");

    var schematic = Schematic.parse(inputStream);

    var total = Schematic.findPartNumberSum(schematic);

    assertThat(total, is(4361));
  }

  @Test
  public void testInputPart1() {
    var inputStream = SchematicTest.class.getClassLoader().getResourceAsStream("input.txt");

    var schematic = Schematic.parse(inputStream);

    var total = Schematic.findPartNumberSum(schematic);

    assertThat(total, is(not(533421)));

    log.info(total);
  }

  @Test
  public void testSamplePart2() {
    var inputStream = SchematicTest.class.getClassLoader().getResourceAsStream("sample.txt");

    var schematic = Schematic.parse(inputStream);

    var total = Schematic.findGearRatios(schematic);

    assertThat(total, is(467835));
  }

  @Test
  public void testInputPart2() {
    var inputStream = SchematicTest.class.getClassLoader().getResourceAsStream("input.txt");

    var schematic = Schematic.parse(inputStream);

    var total = Schematic.findGearRatios(schematic);

    log.info(total);
  }
}
