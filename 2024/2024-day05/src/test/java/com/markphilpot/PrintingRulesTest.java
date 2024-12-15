package com.markphilpot;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import java.util.Collections;

public class PrintingRulesTest {
  private static final Logger log = LogManager.getLogger(PrintingRulesTest.class);

  @Test
  public void testSamplePart1() {
    var inputStream = PrintingRulesTest.class.getClassLoader().getResourceAsStream("sample.txt");

    var pkg = PrintingRules.parse(inputStream);
    var validRuns = PrintingRules.findValidRuns(pkg);
    var num = validRuns.stream().map(PrintingRules.OrderRun::getMid).reduce(0, Integer::sum);

    assertThat(num, is(143));
  }

  @Test
  public void testInputPart1() {
    var inputStream = PrintingRulesTest.class.getClassLoader().getResourceAsStream("input.txt");

    var pkg = PrintingRules.parse(inputStream);
    var validRuns = PrintingRules.findValidRuns(pkg);
    var num = validRuns.stream().map(PrintingRules.OrderRun::getMid).reduce(0, Integer::sum);

    log.info(num);
  }

  @Test
  public void testSamplePart2() {
    var inputStream = PrintingRulesTest.class.getClassLoader().getResourceAsStream("sample.txt");

    var pkg = PrintingRules.parse(inputStream);
    var invalidRuns = PrintingRules.findInvalidRuns(pkg);

    // Fix invalid ones
    

    var num = 0;


    assertThat(num, is(123));
  }

  @Test
  public void testInputPart2() {
    var inputStream = PrintingRulesTest.class.getClassLoader().getResourceAsStream("input.txt");

    var num = 0;

    log.info(num);
  }
}
