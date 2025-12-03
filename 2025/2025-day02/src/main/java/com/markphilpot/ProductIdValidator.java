package com.markphilpot;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

public class ProductIdValidator {
  private static final Logger log = LogManager.getLogger(ProductIdValidator.class);

  public record Range(long start, long end) {}

  public static long checkRange(Range range) {
    return LongStream.range(range.start, range.end+1).map(i -> {
      var asStr = Long.toString(i);
      var isEven = asStr.length() % 2 == 0;
      if(isEven) {
        var a = asStr.substring(0, asStr.length()/2);
        var b = asStr.substring(asStr.length()/2);
        return a.equals(b) ? i : 0L;
      } else {
        return 0L;
      }
    }).reduce(0L, Long::sum);
  }

  public static long checkRangeDeep(Range range) {
    return LongStream.range(range.start, range.end+1).map(i -> {
      var asStr = Long.toString(i);

      var invalid = new HashSet<Long>();

      for(int subStrLen = 1; subStrLen <= asStr.length()/2; subStrLen++) {
        var isDivisible = asStr.length() % subStrLen == 0;
        if(isDivisible) {
          var parts = divide(asStr, subStrLen);
          if(allPartsEqual(parts)) {
            invalid.add(i);
          }
        }
      }

      return invalid.stream().reduce(0L, Long::sum);
    }).reduce(0L, Long::sum);
  }

  public static boolean allPartsEqual(List<String> parts) {
    return parts.stream().allMatch(p -> p.equals(parts.get(0)));
  }

  public static List<String> divide(String input, int subStrLen) {
    return IntStream.range(0, input.length() / subStrLen)
        .mapToObj(i -> input.substring(i * subStrLen, (i + 1) * subStrLen))
        .toList();
  }

  public static List<Range> parse(InputStream inputStream) throws IOException {
    var input = ParsingUtils.streamToString(inputStream);
    var scanner = new Scanner(input).useDelimiter(",");

    return scanner.tokens().map(range -> {
      var parts = new Scanner(range).useDelimiter("-");
      return new Range(parts.nextLong(), parts.nextLong());
    }).toList();
  }
}
