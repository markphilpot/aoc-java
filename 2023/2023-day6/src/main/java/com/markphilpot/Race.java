package com.markphilpot;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Race {
  private static final Logger log = LogManager.getLogger(Race.class);

  public record Record(long time, long distance) {}

  public static List<Record> parse(InputStream inputStream) {
    try (var scanner = new Scanner(inputStream)) {
      var timeLine = scanner.nextLine();
      var distanceLine = scanner.nextLine();

      var times =
          Arrays.stream(timeLine.split(":")[1].trim().split(" "))
              .map(String::trim)
              .filter(s -> !s.isEmpty())
              .map(Long::parseLong)
              .toList();

      var distances =
          Arrays.stream(distanceLine.split(":")[1].trim().split(" "))
              .map(String::trim)
              .filter(s -> !s.isEmpty())
              .map(Long::parseLong)
              .toList();

      return StreamUtils.zip(times.stream(), distances.stream(), Record::new).toList();
    }
  }

  public static List<Record> parseSingleRace(InputStream inputStream) {
    try (var scanner = new Scanner(inputStream)) {
      var timeLine = scanner.nextLine();
      var distanceLine = scanner.nextLine();

      var time =
          Arrays.stream(timeLine.split(":")[1].trim().split(" "))
              .map(String::trim)
              .filter(s -> !s.isEmpty())
              .collect(Collectors.joining());

      var distance =
          Arrays.stream(distanceLine.split(":")[1].trim().split(" "))
              .map(String::trim)
              .filter(s -> !s.isEmpty())
              .collect(Collectors.joining());

      return List.of(new Record(Long.parseLong(time), Long.parseLong(distance)));
    }
  }

  public static long findNumWinningScenarios(Record record) {
    return LongStream.range(0, record.time())
        .boxed()
        .map(
            buttonHeldDuration -> {
              var timeLeftToTravel = record.time() - buttonHeldDuration;
              var acceleration = buttonHeldDuration; // mm/s
              var distanceTraveled = timeLeftToTravel * acceleration;
              return distanceTraveled;
            })
        .filter(dist -> dist > record.distance())
        .count();
  }
}
