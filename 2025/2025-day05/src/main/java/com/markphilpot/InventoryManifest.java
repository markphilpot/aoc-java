package com.markphilpot;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class InventoryManifest {
  private static final Logger log = LogManager.getLogger(InventoryManifest.class);

  public record Range(long min, long max) {}

  public record Manifest(List<Range> ranges, List<Long> items) {}

  public static long numFreshIds(Manifest manifest) {
    var og = mergeRanges(manifest.ranges);
    var prevSize = og.size();

    var mergedRanges = mergeRanges(og);

    while(prevSize != mergedRanges.size()) {
      log.info("Compacting...");
      prevSize = mergedRanges.size();
      mergedRanges = mergeRanges(mergedRanges);
    }

    return mergedRanges.stream().mapToLong(InventoryManifest::numInRange).sum();
  }

  private static List<Range> mergeRanges(List<Range> ranges) {
    var mergedRanges = new ArrayList<Range>();

    for(var range : ranges) {
      if(mergedRanges.isEmpty()) {
        mergedRanges.add(range);
      } else {
        var merged = false;
        for(var mergedRange : mergedRanges) {
          if(rangesIntersect(range, mergedRange)) {
            mergedRanges.set(mergedRanges.indexOf(mergedRange), mergeRanges(range, mergedRange));
            merged = true;
            break;
          }
        }

        if(!merged) {
          mergedRanges.add(range);
        }
      }
    }
    return mergedRanges;
  }

  public static boolean rangesIntersect(Range a, Range b) {
    return (a.max >= b.min && b.max >= a.min);
  }

  private static Range mergeRanges(Range a, Range b) {
    return new Range(Math.min(a.min, b.min), Math.max(a.max, b.max));
  }

  private static long numInRange(Range range) {
    return range.max - range.min + 1;
  }

  public static int numItemsAvailable(Manifest manifest) {
    var num = 0;

    for(var item : manifest.items) {
      var found = manifest.ranges.stream().anyMatch(range -> isInRange(item, range));

      if(found) {
        num++;
      }
    }

    return num;
  }

  private static boolean isInRange(long value, Range range) {
    return range.min <= value && value <= range.max;
  }

  public static Manifest parse(InputStream input) {
    try (var scanner = new Scanner(input)) {
      var ranges = new ArrayList<Range>();
      var items = new ArrayList<Long>();

      var rangeRegion = true;

      while(scanner.hasNextLine()) {
        var line = scanner.nextLine();

        if(line.isEmpty()) {
          rangeRegion = false;
          continue;
        }

        if(rangeRegion) {
          var range = line.split("-");
          ranges.add(new Range(Long.parseLong(range[0]), Long.parseLong(range[1])));
        } else {
          items.add(Long.parseLong(line));
        }
      }

      return new Manifest(ranges, items);
    }
  }
}
