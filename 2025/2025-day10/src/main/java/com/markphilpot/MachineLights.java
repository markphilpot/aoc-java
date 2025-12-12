package com.markphilpot;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;
import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

public class MachineLights {
  private static final Logger log = LogManager.getLogger(MachineLights.class);

  public record Button(List<Boolean> lights) {}

  private final List<Boolean> target;
  private final List<Button> buttons;
  private final List<Integer> joltage;

  public MachineLights(List<Boolean> target, List<Button> buttons, List<Integer> joltage) {
    this.target = target;
    this.buttons = buttons;
    this.joltage = joltage;
  }

  public int run() {
    var seenSet = new HashSet<List<Boolean>>();
    var i = 0;

    var lights = new ArrayList<Boolean>(target.size());
    IntStream.range(0, target.size()).forEach(x -> lights.add(false));

    seenSet.add(lights);

    while(true) {
      var toAdd = new HashSet<List<Boolean>>();
      for(var seen : seenSet) {
        for (var button : buttons) {
          var next = pressButton(seen, button);
          toAdd.add(next);
        }
      }
      seenSet.addAll(toAdd);

      i++;

      if(seenSet.contains(target)) {
        break;
      }
    }

    return i;
  }

  public int runJoltage() {
    var seenSet = new HashSet<List<Integer>>();
    var i = 0;

    var jolts = new ArrayList<Integer>(joltage.size());
    IntStream.range(0, joltage.size()).forEach(x -> jolts.add(0));

    seenSet.add(jolts);

    while(true) {
      var toAdd = new HashSet<List<Integer>>();
      for(var seen : seenSet) {
        for (var button : buttons) {
          var next = pressButtonJolts(seen, button);
          toAdd.add(next);
        }
      }
      seenSet.clear();
      seenSet.addAll(toAdd.stream().filter(x -> {
        for(int j = 0; j < x.size(); j++) {
          if(x.get(j) > joltage.get(j)) {
            return false;
          }
        }
        return true;
      }).toList());

      i++;

      if(seenSet.contains(joltage)) {
        break;
      }
    }

    log.info("Finished run");

    return i;
  }

  public List<Integer> pressButtonJolts(List<Integer> state, Button button) {
    var result = new ArrayList<Integer>(state.size());
    for(var i = 0; i < state.size(); i++) {
      result.add(state.get(i) + (button.lights.get(i) ? 1 : 0));
    }
    return result;
  }

  public List<Boolean> pressButton(List<Boolean> state, Button button) {
    // set lights equal to xor of the elements of lights with button
    var result = new ArrayList<Boolean>(state.size());
    for(var i = 0; i < state.size(); i++) {
      result.add(state.get(i) ^ button.lights.get(i));
    }
    return result;
  }

  @Override
  public String toString() {
    return "MachineLights{" +
            "target=" + target +
            ", buttons=" + buttons +
            ", joltage=" + joltage +
            '}';
  }

  public static List<MachineLights> parse(InputStream inputStream) {
    return ParsingUtils.streamToList(inputStream).stream().map(line -> {
      var scanner = new Scanner(line);

      var numBits = 0;
      List<Boolean> target = List.of();
      List<Button> buttons = new ArrayList<>();
      List<Integer> joltage = List.of();

      while(scanner.hasNext()) {
        var element = scanner.next();

        if(element.startsWith("[")) {
          target = ParsingUtils.lineToStream(element.substring(1, element.length() -1)).map(l -> l.equals("#")).toList();
          numBits = target.size();
        } else if(element.startsWith("(")) {
          var indexes = element.substring(1, element.length() -1).split(",");
          var button = new ArrayList<Boolean>(numBits);
          IntStream.range(0, numBits).forEach(x -> button.add(false));
          for(var index : indexes) {
            button.set(Integer.parseInt(index), true);
          }
          buttons.add(new Button(button));
        } else if(element.startsWith("{")) {
          joltage = Arrays.stream(element.substring(1, element.length() -1).split(",")).map(Integer::parseInt).toList();
        }
      }
      return new MachineLights(target, buttons, joltage);
    }).toList();
  }
}
