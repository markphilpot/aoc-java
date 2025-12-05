package com.markphilpot;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class BatteryBanks {
  private static final Logger log = LogManager.getLogger(BatteryBanks.class);

  public record Banks(List<String> banks) {}

  public static int findLargest(Banks banks) {
    var bats = new ArrayList<String>();

    var currentIndex = 0;

    for(int n = 0; n < 2; n++) {

      var currentMax = 0;
      var maxIndex = -1;

      var offset = 0;
      for(int i = currentIndex; i < banks.banks.size() - 1 + n; i++) {
        var digit = Integer.parseInt(banks.banks.get(i));
        if(offset == 0 || digit > currentMax) {
          currentMax = digit;
          maxIndex = offset;
        }
        offset++;
      }

      bats.add(Integer.toString(currentMax));
      currentIndex = currentIndex + maxIndex + 1;
    }

    var val = String.join("", bats);
//    log.info(val);
    return Integer.parseInt(String.join("", bats));
  }

  public static long findLargestN(Banks banks) {
    var bats = new ArrayList<String>();

    var currentIndex = 0;

    for(int n = 0; n < 12; n++) {

      var currentMax = 0;
      var maxIndex = -1;

      var offset = 0;
      for(int i = currentIndex; i < banks.banks.size() - 11 + n; i++) {
        var digit = Integer.parseInt(banks.banks.get(i));
        if(offset == 0 || digit > currentMax) {
          currentMax = digit;
          maxIndex = offset;
        }
        offset++;
      }

      bats.add(Integer.toString(currentMax));
      currentIndex = currentIndex + maxIndex + 1;
    }

    var val = String.join("", bats);
//    log.info(val);
    return Long.parseLong(String.join("", bats));
  }

  public static List<Banks> parse(InputStream inputStream) {
    return ParsingUtils.streamToList(inputStream).stream()
            .map(ParsingUtils::lineToList)
            .map(Banks::new).toList();
  }
}
