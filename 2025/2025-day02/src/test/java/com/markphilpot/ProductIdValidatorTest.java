package com.markphilpot;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import java.io.IOException;

public class ProductIdValidatorTest {
  private static final Logger log = LogManager.getLogger(ProductIdValidatorTest.class);

  @Test
  public void testSamplePart1() throws IOException {
    var inputStream = ProductIdValidatorTest.class.getClassLoader().getResourceAsStream("sample.txt");

    var ranges = ProductIdValidator.parse(inputStream);

    var num = ranges.stream().map(ProductIdValidator::checkRange).reduce(0L, Long::sum);

    assertThat(num, is(1227775554L));
  }

  @Test
  public void testInputPart1() throws IOException {
    var inputStream = ProductIdValidatorTest.class.getClassLoader().getResourceAsStream("input.txt");

    var ranges = ProductIdValidator.parse(inputStream);

    var num = ranges.stream().map(ProductIdValidator::checkRange).reduce(0L, Long::sum);

    log.info(num);
  }

  @Test
  public void testSamplePart2() throws IOException {
    var inputStream = ProductIdValidatorTest.class.getClassLoader().getResourceAsStream("sample.txt");

    var ranges = ProductIdValidator.parse(inputStream);

    var num = ranges.stream().map(ProductIdValidator::checkRangeDeep).reduce(0L, Long::sum);

    assertThat(num, is(4174379265L));
  }

  @Test
  public void testInputPart2() throws IOException {
    var inputStream = ProductIdValidatorTest.class.getClassLoader().getResourceAsStream("input.txt");

    var ranges = ProductIdValidator.parse(inputStream);

    var num = ranges.stream().map(ProductIdValidator::checkRangeDeep).reduce(0L, Long::sum);

    log.info(num);
  }
}
