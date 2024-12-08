package com.markphilpot;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

public class WordSearchTest {
  private static final Logger log = LogManager.getLogger(WordSearchTest.class);

  @Test
  public void testSamplePart1Small() {
    var inputStream = WordSearchTest.class.getClassLoader().getResourceAsStream("small.txt");

    var num = XmasSearch.findAll(ParsingUtils.streamToGrid(inputStream));

    assertThat(num, is(4));
  }

  @Test
  public void testSamplePart1() {
    var inputStream = WordSearchTest.class.getClassLoader().getResourceAsStream("sample.txt");

    var num = XmasSearch.findAll(ParsingUtils.streamToGrid(inputStream));

    assertThat(num, is(18));
  }

  @Test
  public void testInputPart1() {
    var inputStream = WordSearchTest.class.getClassLoader().getResourceAsStream("input.txt");

    var num = XmasSearch.findAll(ParsingUtils.streamToGrid(inputStream));

    log.info(num);
  }

  @Test
  public void testSamplePart2() {
    var inputStream = WordSearchTest.class.getClassLoader().getResourceAsStream("sample.txt");

    var num = MasSearch.findAll(ParsingUtils.streamToGrid(inputStream));

    assertThat(num, is(9));
  }

  @Test
  public void testInputPart2() {
    var inputStream = WordSearchTest.class.getClassLoader().getResourceAsStream("input.txt");

    var num = MasSearch.findAll(ParsingUtils.streamToGrid(inputStream));

    log.info(num);
  }
}
