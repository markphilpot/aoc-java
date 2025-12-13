package com.markphilpot;

import java.io.InputStream;
import java.util.*;
import java.util.stream.Stream;

public class ServerRack {

  public record Wiring(String device, List<String> outputs) {}

  public record PathDetails(Boolean dac, Boolean fft) {}

  public static int tracePaths(Map<String, Wiring> wiring) {
    var start = wiring.get("you");

    return trace(start, wiring);
  }

  private static int trace(Wiring deviceWiring, Map<String, Wiring> wiring) {
    if(deviceWiring.outputs().size() == 1 && deviceWiring.outputs().getFirst().equals("out")) return 1;

    return deviceWiring.outputs().stream().mapToInt(output -> trace(wiring.get(output), wiring)).sum();
  }

  public static long tracePathsTrack(Map<String, Wiring> wiring) {
    var start = wiring.get("svr");

    return trace(start, wiring, new PathDetails(false, false));
  }

  public record Key(PathDetails pathDetails, String device) {};

  private static Map<Key, Long> cache = new HashMap<>();

  private static long trace(Wiring deviceWiring, Map<String, Wiring> wiring, PathDetails pathDetails) {
    if(deviceWiring.outputs().size() == 1 && deviceWiring.outputs().getFirst().equals("out")) {
      return pathDetails.dac && pathDetails.fft ? 1 : 0;
    }

    PathDetails next;
    if(deviceWiring.device.equals("dac")) {
      next = new PathDetails(true, pathDetails.fft);
    } else if(deviceWiring.device.equals("fft")) {
      next = new PathDetails(pathDetails.dac, true);
    } else {
      next = pathDetails;
    }

    return deviceWiring.outputs().stream().map(output -> {
      if(cache.containsKey(new Key(next, output))) {
        return cache.get(new Key(next, output));
      } else {
        var result = trace(wiring.get(output), wiring, next);
        cache.put(new Key(next, output), result);
        return result;
      }
    }).reduce(0L, Long::sum);
  }

  public static Map<String, Wiring> parse(InputStream inputStream) {
    var deviceWirings = ParsingUtils.streamToList(inputStream).stream().map(line -> {
      var parts = line.split(":");
      var device = parts[0];
      var outputs = ParsingUtils.lineToStreamByWhitespace(parts[1]).toList();
      return new Wiring(device, outputs);
    }).toList();

    var result = new HashMap<String, Wiring>();

    deviceWirings.forEach(wiring -> result.put(wiring.device(), wiring));

    return result;
  }
}
