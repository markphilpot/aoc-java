package com.markphilpot;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class LocationListTest {
  private static final Logger log = LogManager.getLogger(LocationListTest.class);

  @Test
  public void testPart1Sample() {
    var sampleInputStream = LocationListTest.class.getClassLoader().getResourceAsStream("sample.txt");
    var lines = ParsingUtils.streamToList(sampleInputStream);

    var a = new ArrayList<Integer>();
    var b = new ArrayList<Integer>();

    for (String line : lines) {
      var parsed = ParsingUtils.readInts(line).toList();
      a.add(parsed.get(0));
      b.add(parsed.get(1));
    }

    a.sort(Integer::compareTo);
    b.sort(Integer::compareTo);

    var distances = StreamUtils.zip(a.stream(), b.stream(), (x, y) -> Math.abs(x - y)).toList();

    var sum = distances.stream().reduce(0, Integer::sum);

    assertThat(sum, is(11));
  }

  @Test
  public void testPart1() {
    var sampleInputStream = LocationListTest.class.getClassLoader().getResourceAsStream("input.txt");
    var lines = ParsingUtils.streamToList(sampleInputStream);

    var a = new ArrayList<Integer>();
    var b = new ArrayList<Integer>();

    for (String line : lines) {
      var parsed = ParsingUtils.readInts(line).toList();
      a.add(parsed.get(0));
      b.add(parsed.get(1));
    }

    a.sort(Integer::compareTo);
    b.sort(Integer::compareTo);

    var distances = StreamUtils.zip(a.stream(), b.stream(), (x, y) -> Math.abs(x - y)).toList();

    var sum = distances.stream().reduce(0, Integer::sum);

    log.info(sum);
  }

  @Test
  public void testPart2Sample() {
    var sampleInputStream = LocationListTest.class.getClassLoader().getResourceAsStream("sample.txt");
    var lines = ParsingUtils.streamToList(sampleInputStream);

    var a = new ArrayList<Integer>();
    var freqMap = new HashMap<Integer, AtomicInteger>();

    for (String line : lines) {
      var parsed = ParsingUtils.readInts(line).toList();
      a.add(parsed.get(0));

      var x = parsed.get(1);

      if(freqMap.containsKey(x)) {
        freqMap.get(x).incrementAndGet();
      } else {
        freqMap.put(x, new AtomicInteger(1));
      }
    }

    var zero = new AtomicInteger(0);
    var simScores = a.stream().map(x -> x * freqMap.getOrDefault(x, zero).get()).toList();

    var sum = simScores.stream().reduce(0, Integer::sum);

    assertThat(sum, is(31));
  }

  @Test
  public void testPart2() {
    var sampleInputStream = LocationListTest.class.getClassLoader().getResourceAsStream("input.txt");
    var lines = ParsingUtils.streamToList(sampleInputStream);

    var a = new ArrayList<Integer>();
    var freqMap = new HashMap<Integer, AtomicInteger>();

    for (String line : lines) {
      var parsed = ParsingUtils.readInts(line).toList();
      a.add(parsed.get(0));

      var x = parsed.get(1);

      if(freqMap.containsKey(x)) {
        freqMap.get(x).incrementAndGet();
      } else {
        freqMap.put(x, new AtomicInteger(1));
      }
    }

    var zero = new AtomicInteger(0);
    var simScores = a.stream().map(x -> x * freqMap.getOrDefault(x, zero).get()).toList();

    var sum = simScores.stream().reduce(0, Integer::sum);

    log.info(sum);
  }

}
