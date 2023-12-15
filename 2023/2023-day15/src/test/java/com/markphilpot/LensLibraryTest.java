package com.markphilpot;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import java.io.IOException;
import java.util.Arrays;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

public class LensLibraryTest {
  private static final Logger log = LogManager.getLogger(LensLibraryTest.class);

  @Test
  public void testSamplePart1() {
    var value = LensLibrary.hash("HASH");

    assertThat(value, is(52));

    var seq = "rn=1,cm-,qp=3,cm=2,qp-,pc=4,ot=9,ab=5,pc-,pc=6,ot=7";

    var total = Arrays.stream(seq.split(",")).map(LensLibrary::hash).reduce(0, Integer::sum);

    assertThat(total, is(1320));
  }

  @Test
  public void testInputPart1() throws IOException {
    var inputStream = LensLibraryTest.class.getClassLoader().getResourceAsStream("input.txt");

    var seq = new String(inputStream.readAllBytes());

    var total = Arrays.stream(seq.split(",")).map(LensLibrary::hash).reduce(0, Integer::sum);

    log.info(total);
  }

  @Test
  public void testSamplePart2() {
    var seq = "rn=1,cm-,qp=3,cm=2,qp-,pc=4,ot=9,ab=5,pc-,pc=6,ot=7";

    var boxes = LensLibrary.init();

    LensLibrary.apply(boxes, seq);

    var total = LensLibrary.getFocusingPower(boxes);

    assertThat(total, is(145L));
  }

  @Test
  public void testInputPart2() throws IOException {
    var inputStream = LensLibraryTest.class.getClassLoader().getResourceAsStream("input.txt");

    var seq = new String(inputStream.readAllBytes());

    var boxes = LensLibrary.init();
    LensLibrary.apply(boxes, seq);
    var total = LensLibrary.getFocusingPower(boxes);

    log.info(total);
  }
}
