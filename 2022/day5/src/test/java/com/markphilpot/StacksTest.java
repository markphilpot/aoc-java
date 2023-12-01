package com.markphilpot;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import java.util.Arrays;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

public class StacksTest {
  private static final Logger log = LogManager.getLogger(StacksTest.class);

  @Test
  public void testColumnParsing() {
    var line = " 1   2   3 ";
    var result = Arrays.stream(line.trim().split(" ")).filter(x -> !x.isBlank()).toList();
    assertThat(result.size(), is(3));
  }

  @Test
  public void testSamplePart1() {
    var inputStream = StacksTest.class.getClassLoader().getResourceAsStream("sample.txt");

    var stacks = Stacks.parse(inputStream);

    log.info(stacks);

    stacks.run();

    log.info(stacks);

    var top = String.join("", stacks.peek());

    assertThat(top, is("CMZ"));
  }

  @Test
  public void testInputPart1() {
    var inputStream = StacksTest.class.getClassLoader().getResourceAsStream("input.txt");

    var stacks = Stacks.parse(inputStream);

    stacks.run();

    var top = String.join("", stacks.peek());

    log.info(top);

    assertThat(top, is("SHMSDGZVC"));
  }

  @Test
  public void testSamplePart2() {
    var inputStream = StacksTest.class.getClassLoader().getResourceAsStream("sample.txt");

    var stacks = Stacks.parse(inputStream);

    log.info(stacks);

    stacks.runBatch();

    log.info(stacks);

    var top = String.join("", stacks.peek());

    assertThat(top, is("MCD"));
  }

  @Test
  public void testInputPart2() {
    var inputStream = StacksTest.class.getClassLoader().getResourceAsStream("input.txt");

    var stacks = Stacks.parse(inputStream);

    stacks.runBatch();

    var top = String.join("", stacks.peek());

    log.info(top);

    assertThat(top, is("VRZGHDFBQ"));
  }
}
