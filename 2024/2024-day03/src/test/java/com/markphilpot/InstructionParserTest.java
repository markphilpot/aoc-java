package com.markphilpot;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import java.io.IOException;

public class InstructionParserTest {
  private static final Logger log = LogManager.getLogger(InstructionParserTest.class);

  @Test
  public void testSamplePart1() throws IOException {
    var inputStream = InstructionParserTest.class.getClassLoader().getResourceAsStream("sample.txt");

    var instructions = InstructionParser.parse(ParsingUtils.streamToString(inputStream));
    var num = instructions.stream().map(InstructionParser.Mul::execute).reduce(0, Integer::sum);

    assertThat(num, is(161));
  }

  @Test
  public void testInputPart1() throws IOException {
    var inputStream = InstructionParserTest.class.getClassLoader().getResourceAsStream("input.txt");

    var instructions = InstructionParser.parse(ParsingUtils.streamToString(inputStream));
    var num = instructions.stream().map(InstructionParser.Mul::execute).reduce(0, Integer::sum);

    log.info(num);
  }

  @Test
  public void testSamplePart2() throws IOException {
    var inputStream = InstructionParserTest.class.getClassLoader().getResourceAsStream("sample2.txt");

    var instructions = InstructionParser.parseExtended(ParsingUtils.streamToString(inputStream));
    var num = instructions.stream().map(InstructionParser.Mul::execute).reduce(0, Integer::sum);

    assertThat(num, is(48));
  }

  @Test
  public void testInputPart2() throws IOException {
    var inputStream = InstructionParserTest.class.getClassLoader().getResourceAsStream("input.txt");

    var instructions = InstructionParser.parseExtended(ParsingUtils.streamToString(inputStream));
    var num = instructions.stream().map(InstructionParser.Mul::execute).reduce(0, Integer::sum);

    log.info(num);
  }
}
