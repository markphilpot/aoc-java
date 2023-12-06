package com.markphilpot;

import java.io.InputStream;
import java.util.*;
import java.util.stream.LongStream;
import java.util.stream.Stream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Almanac {
  private static final Logger log = LogManager.getLogger(Almanac.class);

  public enum To {
    seed_to_soil,
    soil_to_fertilizer,
    fertilizer_to_water,
    water_to_light,
    light_to_temperature,
    temperature_to_humidity,
    humidity_to_location;
  }

  public record Transform(long dest, long src, long length) {
    public boolean isInSrcRange(long input) {
      return src <= input && src + length > input;
    }

    public boolean isInDestRange(long input) {
      return dest <= input && dest + length > input;
    }

    public long followToDest(long input) {
      var offset = input - src;
      return dest + offset;
    }

    public long followToSrc(long input) {
      var offset = input - dest;
      return src + offset;
    }
  }

  public record SeedRange(long init, long length) {
    public Stream<Long> stream() {
      return LongStream.range(init, init + length).boxed();
    }

    public boolean includes(long input) {
      return init <= input && init + length > input;
    }
  }

  public record RangeRecord(List<SeedRange> seeds, Map<To, List<Transform>> transforms) {
    public long followToDest(To key, long input) {
      return transforms.get(key).stream()
          .filter(t -> t.isInSrcRange(input))
          .findFirst()
          .map(t -> t.followToDest(input))
          .orElse(input);
    }

    public long followToSrc(To key, long input) {
      return transforms.get(key).stream()
          .filter(t -> t.isInDestRange(input))
          .findFirst()
          .map(t -> t.followToSrc(input))
          .orElse(input);
    }
  }

  public record Record(List<Long> seeds, Map<To, List<Transform>> transforms) {
    public long followToDest(To key, long input) {
      return transforms.get(key).stream()
          .filter(t -> t.isInSrcRange(input))
          .findFirst()
          .map(t -> t.followToDest(input))
          .orElse(input);
    }
  }

  public static Record parse(InputStream inputStream) {
    var seeds = new ArrayList<Long>();
    var maps = new HashMap<To, List<Transform>>();

    // State
    To currentKey = null;

    try (var scanner = new Scanner(inputStream)) {
      while (scanner.hasNextLine()) {
        var line = scanner.nextLine();

        if (line.isBlank()) {
          currentKey = null;
        } else if (line.startsWith("seeds:")) {
          var s = new Scanner(line);
          s.next();
          s.forEachRemaining(x -> seeds.add(Long.parseLong(x)));
          s.close();
        } else if (line.endsWith("map:")) {
          var els = line.split(" ");
          var key = els[0].replaceAll("-", "_");
          var toKey = To.valueOf(key);
          maps.put(toKey, new ArrayList<>());
          currentKey = toKey;
        } else {
          // Transform
          var s = new Scanner(line);
          maps.get(currentKey).add(new Transform(s.nextLong(), s.nextLong(), s.nextLong()));
        }
      }
    }

    return new Record(seeds, maps);
  }

  public static RangeRecord parseRange(InputStream inputStream) {
    var seeds = new ArrayList<SeedRange>();
    var maps = new HashMap<To, List<Transform>>();

    // State
    To currentKey = null;

    try (var scanner = new Scanner(inputStream)) {
      while (scanner.hasNextLine()) {
        var line = scanner.nextLine();

        if (line.isBlank()) {
          currentKey = null;
        } else if (line.startsWith("seeds:")) {
          var seedElements = new ArrayList<Long>();
          var s = new Scanner(line);
          s.next();
          s.forEachRemaining(x -> seedElements.add(Long.parseLong(x)));
          s.close();

          // No great way to iterate by two elements WISH
          for (var i = 0; i < seedElements.size(); i += 2) {
            seeds.add(new SeedRange(seedElements.get(i), seedElements.get(i + 1)));
          }

        } else if (line.endsWith("map:")) {
          var els = line.split(" ");
          var key = els[0].replaceAll("-", "_");
          var toKey = To.valueOf(key);
          maps.put(toKey, new ArrayList<>());
          currentKey = toKey;
        } else {
          // Transform
          var s = new Scanner(line);
          maps.get(currentKey).add(new Transform(s.nextLong(), s.nextLong(), s.nextLong()));
        }
      }
    }

    seeds.sort(Comparator.comparing(SeedRange::init));

    return new RangeRecord(seeds, maps);
  }

  public static long findLowestLocation(Record record) {
    var locations =
        record.seeds().stream()
            .map(
                s -> {
                  var x = s;
                  for (var key : To.values()) {
                    x = record.followToDest(key, x);
                  }
                  return x;
                })
            .toList();

    return locations.stream().min(Long::compareTo).orElseThrow(NoSuchElementException::new);
  }

  public static long findLowestLocation(RangeRecord record) {
    var size = record.seeds().stream().map(SeedRange::length).reduce(0L, Long::sum);

    log.info("Checking %d seeds in all (%d ranges)...".formatted(size, record.seeds().size()));

    long currentMin = Long.MAX_VALUE;

    for (var i = 0; i < record.seeds().size(); i++) {
      log.info("Checking range %d".formatted(i));
      var sr = record.seeds().get(i);

      var localMin =
          sr.stream()
              .parallel()
              .map(
                  s -> {
                    var x = s;
                    for (var key : To.values()) {
                      x = record.followToDest(key, x);
                    }
                    return x;
                  })
              .min(Long::compareTo)
              .orElseThrow();

      if (localMin < currentMin) {
        currentMin = localMin;
      }
    }

    return currentMin;
  }

  public static long findLowestLocationAlt(RangeRecord record) {
    // Let's try to max CPU for a bit
    var rangeStart = 0L;

    while (true) {
      log.info(rangeStart);
      var found =
          LongStream.range(rangeStart, rangeStart + 1_000_000)
              .boxed()
              .parallel()
              .map(
                  input -> {
                    var x = input;
                    for (var key : Arrays.stream(To.values()).toList().reversed()) {
                      x = record.followToSrc(key, x);
                    }
                    // Check if x is in a seed range
                    for (var sr : record.seeds()) {
                      if (sr.includes(x)) {
                        return input;
                      }
                    }
                    return null;
                  })
              .filter(Objects::nonNull)
              .sorted()
              .findFirst();

      if (found.isPresent()) {
        return found.get();
      }

      rangeStart += 1_000_000;
    }

    //    return LongStream.range(0L, Long.MAX_VALUE).boxed().map(input -> {
    //      if(input % 1_000_000 == 0) {
    //        log.info("Checking %d".formatted(input));
    //      }
    //      var x = input;
    //      for (var key : Arrays.stream(To.values()).toList().reversed()) {
    //        x = record.followToSrc(key, x);
    //      }
    //      // Check if x is in a seed range
    //      for(var sr : record.seeds()) {
    //        if(sr.includes(x)) {
    //          return input;
    //        }
    //      }
    //      return null;
    //    }).filter(Objects::nonNull).findFirst().orElseThrow();

    //    while(!found) {
    //      if(input % 1_000_000 == 0) {
    //        log.info("Checking %d".formatted(input));
    //      }
    //      var x = input;
    //      for (var key : Arrays.stream(To.values()).toList().reversed()) {
    //        x = record.followToSrc(key, x);
    //      }
    //
    //      // Check if x is in a seed range
    //      for(var sr : record.seeds()) {
    //        if(sr.includes(x)) {
    //          return input;
    //        }
    //      }
    //
    //      input++;
    //    }
    //
    //    return input;
  }
}
