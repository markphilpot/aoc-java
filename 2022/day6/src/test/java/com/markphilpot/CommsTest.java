package com.markphilpot;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

public class CommsTest {
  private static final Logger log = LogManager.getLogger(CommsTest.class);

  @Test
  public void testSample() {
    var samples =
        Map.of(
            "mjqjpqmgbljsphdztnvjfqwrcgsmlb", 7,
            "bvwbjplbgvbhsrlpgdmjqwftvncz", 5,
            "nppdvjthqldpwncqszvftbrmjlhg", 6,
            "nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg", 10,
            "zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw", 11);

    samples.forEach(
        (input, marker) -> {
          assertThat(Comms.findMarker(input, 4), is(marker));
        });
  }

  @Test
  public void testInputPart1() throws IOException {
    var inputStream = CommsTest.class.getClassLoader().getResourceAsStream("input.txt");

    var input = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

    var marker = Comms.findMarker(input, 4);

    log.info(marker);

    assertThat(marker, is(1723));
  }

  @Test
  public void testSamplePart2() {
    var samples =
        Map.of(
            "mjqjpqmgbljsphdztnvjfqwrcgsmlb", 19,
            "bvwbjplbgvbhsrlpgdmjqwftvncz", 23,
            "nppdvjthqldpwncqszvftbrmjlhg", 23,
            "nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg", 29,
            "zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw", 26);

    samples.forEach(
        (input, marker) -> {
          assertThat(Comms.findMarker(input, 14), is(marker));
        });
  }

  @Test
  public void testInputPart2() throws IOException {
    var inputStream = CommsTest.class.getClassLoader().getResourceAsStream("input.txt");

    var input = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

    var marker = Comms.findMarker(input, 14);

    log.info(marker);

    assertThat(marker, is(3708));
  }
}
