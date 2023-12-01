package com.markphilpot;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

public class CalibrationTest {
  private static final Logger log = LogManager.getLogger(CalibrationTest.class);

  @Test
  public void testSamplePart1() {
    var inputStream = CalibrationTest.class.getClassLoader().getResourceAsStream("sample.txt");

    var inputs = Map.of(
            "1abc2", 12,
            "pqr3stu8vwx", 38,
            "a1b2c3d4e5f", 15,
            "treb7uchet", 77
    );

    inputs.forEach((line, result) -> {
      var value = Calibration.getCalibrationValue(LineParsing.lineToList(line));
      assertThat(value, is(result));
    });

    var total = inputs.entrySet().stream()
            .map(entry -> Calibration.getCalibrationValue(LineParsing.lineToList(entry.getKey())))
            .reduce(0, Integer::sum);

    assertThat(total, is(142));
  }

  @Test
  public void testInputPart1() {
    var inputStream = CalibrationTest.class.getClassLoader().getResourceAsStream("input.txt");

    var inputs = new ArrayList<String>();

    var scanner = new Scanner(inputStream).useDelimiter("\n");

    while(scanner.hasNext()) {
      inputs.add(scanner.next());
    }

    var total = inputs.stream()
            .map(entry -> Calibration.getCalibrationValue(LineParsing.lineToList(entry)))
            .reduce(0, Integer::sum);

    log.info(total);
//    assertThat(total, is(0));
  }

  @Test
  public void testSamplePart2() {
    var inputs = Map.of(
            "two1nine", 29,
            "eightwothree", 83,
            "abcone2threexyz", 13,
            "xtwone3four", 24,
            "4nineeightseven2", 42,
            "zoneight234", 14,
            "7pqrstsixteen", 76
    );

    inputs.forEach((line, result) -> {
      var value = Calibration.getCalibrationValueWithWords(line);
      assertThat(value, is(result));
    });

    var total = inputs.entrySet().stream()
            .map(entry -> Calibration.getCalibrationValueWithWords(entry.getKey()))
            .reduce(0, Integer::sum);

    assertThat(total, is(281));

    assertThat(Calibration.getCalibrationValueWithWords("4r"), is(44));
    assertThat(Calibration.getCalibrationValueWithWords("315twonehz"), is(31));
    assertThat(Calibration.getCalibrationValueWithWords("2six1"), is(21));
    assertThat(Calibration.getCalibrationValueWithWords("449three45three"), is(43));
  }

  @Test
  public void testInputPart2() {
    var inputStream = CalibrationTest.class.getClassLoader().getResourceAsStream("input.txt");

    var inputs = new ArrayList<String>();

    var scanner = new Scanner(inputStream).useDelimiter("\n");

    while(scanner.hasNext()) {
      inputs.add(scanner.next());
    }

    var total = inputs.stream()
            .map(Calibration::getCalibrationValueWithWords)
            .reduce(0, Integer::sum);

    log.info(total);
  }
}
