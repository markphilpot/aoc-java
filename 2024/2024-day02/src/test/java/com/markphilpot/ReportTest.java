package com.markphilpot;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import java.util.List;

public class ReportTest {
  private static final Logger log = LogManager.getLogger(ReportTest.class);

  @Test
  public void testSamplePart1() {
    var inputStream = ReportTest.class.getClassLoader().getResourceAsStream("sample.txt");

    var reports = ParsingUtils.streamToList(inputStream).stream().map(line -> ParsingUtils.readInts(line).toList()).toList();

    var num = reports.stream().map(list -> ReportCheck.isSafe(list) ? 1 : 0).reduce(0, Integer::sum);

    assertThat(num, is(2));
  }

  @Test
  public void testInputPart1() {
    var inputStream = ReportTest.class.getClassLoader().getResourceAsStream("input.txt");

    var reports = ParsingUtils.streamToList(inputStream).stream().map(line -> ParsingUtils.readInts(line).toList()).toList();

    var num = reports.stream().map(list -> ReportCheck.isSafe(list) ? 1 : 0).reduce(0, Integer::sum);

    log.info(num);
  }

  @Test
  public void testSamplePart2() {
    var inputStream = ReportTest.class.getClassLoader().getResourceAsStream("sample.txt");

    assertThat(ReportCheck.isSafeDampening(List.of(1,3,2,4,5)), is(true));

    var oneOff = List.of(1, 4, 2, 3, 5);
    assertThat(ReportCheck.isSafeDampening(oneOff), is(true));

    var reports = ParsingUtils.streamToList(inputStream).stream().map(line -> ParsingUtils.readInts(line).toList()).toList();

    var num = reports.stream().map(list -> ReportCheck.isSafeDampening(list) ? 1 : 0).reduce(0, Integer::sum);

    assertThat(num, is(4));
  }

  @Test
  public void testInputPart2() {
    var inputStream = ReportTest.class.getClassLoader().getResourceAsStream("input.txt");

    var reports = ParsingUtils.streamToList(inputStream).stream().map(line -> ParsingUtils.readInts(line).toList()).toList();

    var num = reports.stream().map(list -> {
      if(ReportCheck.isSafeDampening(list)) {
        return 1;
      } else {
//        log.info(list);
        return 0;
      }
    }).reduce(0, Integer::sum);

    log.info(num);
  }
}
