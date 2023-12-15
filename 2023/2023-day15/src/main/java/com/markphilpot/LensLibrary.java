package com.markphilpot;

import java.util.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LensLibrary {
  private static final Logger log = LogManager.getLogger(LensLibrary.class);

  public static int hash(String x) {
    var current = 0;
    var asciiCodes = x.chars().boxed().toList();

    for (var i : asciiCodes) {
      current += i;
      current *= 17;
      current = current % 256;
    }

    return current;
  }

  public static record Lens(String label, int focal) {}

  public static List<List<Lens>> init() {
    var boxes = new ArrayList<List<Lens>>(256);
    for (var i = 0; i < 256; i++) {
      boxes.add(new ArrayList<>());
    }
    return boxes;
  }

  public static void apply(List<List<Lens>> boxes, String seq) {
    var operations = Arrays.stream(seq.split(",")).toList();

    for (var op : operations) {
      if (op.endsWith("-")) {
        // Handle Remove
        var label = op.substring(0, op.length() - 1);
        var boxIndex = hash(label);
        var box = boxes.get(boxIndex);

        for (var i = 0; i < box.size(); i++) {
          var current = box.get(i);
          if (current.label.equals(label)) {
            box.remove(i);
            break;
          }
        }
      } else {
        // Handle Insert
        var el = op.split("=");
        var label = el[0];
        var focal = Integer.parseInt(el[1]);
        var boxIndex = hash(label);

        var box = boxes.get(boxIndex);

        var found = false;
        for (var i = 0; i < box.size(); i++) {
          var current = box.get(i);
          if (current.label.equals(label)) {
            box.set(i, new Lens(label, focal));
            found = true;
            break;
          }
        }

        if (!found) {
          box.add(new Lens(label, focal));
        }
      }

      // Status
      //            log.info("After %s".formatted(op));
      //            boxes.forEach(StreamUtils.forEachWithIndex((i, b) -> {
      //                if(!b.isEmpty()) {
      //                    log.info("Box %d: %s".formatted(i, b.toString()));
      //                }
      //            }));
    }
  }

  public static long getFocusingPower(List<List<Lens>> boxes) {
    return StreamUtils.zipWithIndex(boxes.stream())
        .flatMap(
            x -> {
              var boxNum = x.index() + 1;
              return StreamUtils.zipWithIndex(x.value().stream())
                  .map(
                      y -> {
                        var lensNum = y.index() + 1;
                        return boxNum * lensNum * y.value().focal;
                      });
            })
        .reduce(0L, Long::sum);
  }
}
