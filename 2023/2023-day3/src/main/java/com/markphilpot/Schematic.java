package com.markphilpot;

import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Schematic {
  private static final Logger log = LogManager.getLogger(Schematic.class);

  public static List<String> parse(InputStream inputStream) {
    return Parsing.streamToList(inputStream);
  }

  public static boolean isSymbol(String x) {
    return x.matches("[^\\d.]");
  }

  public static boolean isDigit(String x) {
    return x.matches("\\d");
  }

  public record Pair(int x, int y) {}

  public record Symbol(String s, Pair loc) {}

  public record PartNumberElement(String c, Pair loc) {}

  public record PartNumber(Integer num, List<Pair> pairs) {}

  public record LineInventory(List<PartNumber> partNumbers, List<Symbol> symbolCoordinates) {}

  public static List<Pair> getAdjacentIndexes(int x, int y, int maxX, int maxY) {
    var targets =
        List.of(
            new Pair(x - 1, y - 1),
            new Pair(x, y - 1),
            new Pair(x + 1, y - 1),
            new Pair(x - 1, y),
            new Pair(x + 1, y),
            new Pair(x - 1, y + 1),
            new Pair(x, y + 1),
            new Pair(x + 1, y + 1));

    return targets.stream()
        .filter(p -> p.x() >= 0 && p.y() >= 0 && p.x() < maxX && p.y() < maxY)
        .toList();
  }

  private static PartNumber flushElements(List<PartNumberElement> elements) {
    return new PartNumber(
        Integer.parseInt(elements.stream().map(PartNumberElement::c).collect(Collectors.joining())),
        elements.stream().map(PartNumberElement::loc).toList());
  }

  private static LineInventory inventoryLine(String line, int y) {
    var asList = Parsing.lineToList(line);

    var symbolCoordinates = new ArrayList<Symbol>();
    var partNumbers = new ArrayList<PartNumber>();

    var currentPartNumber = new ArrayList<PartNumberElement>();
    for (var x = 0; x < asList.size(); x++) {
      var m = asList.get(x);
      if (isSymbol(m)) {
        symbolCoordinates.add(new Symbol(m, new Pair(x, y)));
        if (!currentPartNumber.isEmpty()) {
          partNumbers.add(flushElements(currentPartNumber));
          currentPartNumber = new ArrayList<>();
        }
      } else if (isDigit(m)) {
        currentPartNumber.add(new PartNumberElement(m, new Pair(x, y)));
      } else {
        if (!currentPartNumber.isEmpty()) {
          partNumbers.add(flushElements(currentPartNumber));
          currentPartNumber = new ArrayList<>();
        }
      }
    }

    // Flush if number ends in the last column
    if (!currentPartNumber.isEmpty()) {
      partNumbers.add(flushElements(currentPartNumber));
    }

    return new LineInventory(partNumbers, symbolCoordinates);
  }

  public static int findPartNumberSum(List<String> schematic) {
    var schematicList = schematic.stream().map(Parsing::lineToList).toList();
    var numCols = schematicList.size();
    var numRows = schematicList.get(0).size();

    var inventory = new ArrayList<LineInventory>();

    for (var y = 0; y < schematic.size(); y++) {
      inventory.add(inventoryLine(schematic.get(y), y));
    }

    // Build lookup table
    var lookupTable = new HashMap<Pair, PartNumber>();

    inventory.forEach(
        lineInventory -> {
          lineInventory.partNumbers.forEach(
              pn -> {
                pn.pairs()
                    .forEach(
                        p -> {
                          lookupTable.put(p, pn);
                        });
              });
        });

    var symbols =
        inventory.stream()
            .flatMap(lineInventory -> lineInventory.symbolCoordinates().stream())
            .toList();

    return symbols.stream()
        .map(
            s -> {
              var targets = getAdjacentIndexes(s.loc().x(), s.loc().y(), numRows, numCols);
              var components =
                  targets.stream()
                      .map(lookupTable::get)
                      .filter(Objects::nonNull)
                      .distinct()
                      .toList();
              return components.stream().map(PartNumber::num).reduce(0, Integer::sum);
            })
        .reduce(0, Integer::sum);
  }

  public static int findGearRatios(List<String> schematic) {
    var schematicList = schematic.stream().map(Parsing::lineToList).toList();
    var numCols = schematicList.size();
    var numRows = schematicList.get(0).size();

    var inventory = new ArrayList<LineInventory>();

    for (var y = 0; y < schematic.size(); y++) {
      inventory.add(inventoryLine(schematic.get(y), y));
    }

    // Build lookup table
    var lookupTable = new HashMap<Pair, PartNumber>();

    inventory.forEach(
        lineInventory -> {
          lineInventory.partNumbers.forEach(
              pn -> {
                pn.pairs()
                    .forEach(
                        p -> {
                          lookupTable.put(p, pn);
                        });
              });
        });

    var symbols =
        inventory.stream()
            .flatMap(lineInventory -> lineInventory.symbolCoordinates().stream())
            .filter(symbol -> symbol.s().equals("*")) // Only look at gears
            .toList();

    return symbols.stream()
        .map(
            s -> {
              var targets = getAdjacentIndexes(s.loc().x(), s.loc().y(), numRows, numCols);
              var components =
                  targets.stream()
                      .map(lookupTable::get)
                      .filter(Objects::nonNull)
                      .distinct()
                      .toList();

              if (components.size() == 2) {
                return components.stream().map(PartNumber::num).reduce(1, (acc, el) -> acc * el);
              }

              return 0;
            })
        .reduce(0, Integer::sum);
  }
}
