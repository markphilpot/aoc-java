package com.markphilpot;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import com.markphilpot.InventoryManifest.Range;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

public class InventoryManifestTest {
  private static final Logger log = LogManager.getLogger(InventoryManifestTest.class);

  @Test
  public void testSamplePart1() {
    var inputStream = InventoryManifestTest.class.getClassLoader().getResourceAsStream("sample.txt");

    var manifest = InventoryManifest.parse(inputStream);

    var num = InventoryManifest.numItemsAvailable(manifest);

    assertThat(num, is(3));
  }

  @Test
  public void testInputPart1() {
    var inputStream = InventoryManifestTest.class.getClassLoader().getResourceAsStream("input.txt");

    var manifest = InventoryManifest.parse(inputStream);

    var num = InventoryManifest.numItemsAvailable(manifest);

    log.info(num);
  }

  @Test
  public void testIntersection() {
    assertThat(InventoryManifest.rangesIntersect(new Range(1,4), new Range(6,10)), is(false));
    assertThat(InventoryManifest.rangesIntersect(new Range(6,10), new Range(1,4)), is(false));
    assertThat(InventoryManifest.rangesIntersect(new Range(1,7), new Range(6,10)), is(true));
    assertThat(InventoryManifest.rangesIntersect(new Range(1,12), new Range(6,10)), is(true));
    assertThat(InventoryManifest.rangesIntersect(new Range(2,4), new Range(4,10)), is(true));
    assertThat(InventoryManifest.rangesIntersect(new Range(2,4), new Range(3,10)), is(true));
    assertThat(InventoryManifest.rangesIntersect(new Range(2,4), new Range(2,10)), is(true));
  }

  @Test
  public void testSamplePart2() {
    var inputStream = InventoryManifestTest.class.getClassLoader().getResourceAsStream("sample.txt");

    var manifest = InventoryManifest.parse(inputStream);

    var num = InventoryManifest.numFreshIds(manifest);

    assertThat(num, is(14L));
  }

  @Test
  public void testInputPart2() {
    var inputStream = InventoryManifestTest.class.getClassLoader().getResourceAsStream("input.txt");

    var manifest = InventoryManifest.parse(inputStream);

    var num = InventoryManifest.numFreshIds(manifest);

    // 338348170606125 is too high...
    log.info(num);
  }
}
