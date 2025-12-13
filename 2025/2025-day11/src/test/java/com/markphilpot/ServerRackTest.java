package com.markphilpot;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

public class ServerRackTest {
  private static final Logger log = LogManager.getLogger(ServerRackTest.class);

  @Test
  public void testSamplePart1() {
    var inputStream = ServerRackTest.class.getClassLoader().getResourceAsStream("sample.txt");

    var rack = ServerRack.parse(inputStream);
    var num = ServerRack.tracePaths(rack);

    assertThat(num, is(5));
  }

  @Test
  public void testInputPart1() {
    var inputStream = ServerRackTest.class.getClassLoader().getResourceAsStream("input.txt");

    var rack = ServerRack.parse(inputStream);
    var num = ServerRack.tracePaths(rack);

    log.info(num);
  }

  @Test
  public void testSamplePart2() {
    var inputStream = ServerRackTest.class.getClassLoader().getResourceAsStream("sample2.txt");

    var rack = ServerRack.parse(inputStream);
    var num = ServerRack.tracePathsTrack(rack);

    assertThat(num, is(2L));
  }

  @Test
  public void testInputPart2() {
    var inputStream = ServerRackTest.class.getClassLoader().getResourceAsStream("input.txt");

    var rack = ServerRack.parse(inputStream);
    var num = ServerRack.tracePathsTrack(rack);

    log.info(num);
  }
}
