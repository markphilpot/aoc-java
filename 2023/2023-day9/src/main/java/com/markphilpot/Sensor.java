package com.markphilpot;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Sensor {
  private static final Logger log = LogManager.getLogger(Sensor.class);

  public static List<List<Long>> parse(InputStream inputStream) {
    try (var scanner = new Scanner(inputStream)) {
      var records = new ArrayList<List<Long>>();
      while (scanner.hasNextLine()) {
        var line = scanner.nextLine();
        records.add(ParsingUtils.readLongs(line).toList());
      }
      return records;
    }
  }

  public static Long findHistoryValue(List<Long> record) {
    var extrapolation = new ArrayList<List<Long>>();
    extrapolation.add(record);
    List<Long> next;

    do {
      next =
          extrapolation.getLast().stream().collect(new WindowCollector<>(2)).stream()
              .map(w -> w.getLast() - w.getFirst())
              .toList();

      extrapolation.add(next);
    } while (!next.stream().allMatch(x -> x.equals(0L)));

    return extrapolation.reversed().stream()
        .collect(StreamUtils.foldLeft(0L, (prev, list) -> prev + list.getLast()));
  }

  public static List<Long> findHistory(List<List<Long>> records) {
    return records.stream().map(Sensor::findHistoryValue).toList();
  }
}
