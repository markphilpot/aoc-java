package com.markphilpot;

import java.util.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Rucksack {
  private static final Logger log = LogManager.getLogger(Rucksack.class);
  public static String priority = "_abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

  private String contents;

  private List<String> first;
  private List<String> second;

  public Rucksack(String contents) {
    this.contents = contents;

    var firstHalf = contents.substring(0, contents.length() / 2);
    var secondHalf = contents.substring(contents.length() / 2);

    first = firstHalf.chars().mapToObj(x -> (char) x).map(Object::toString).toList();
    second = secondHalf.chars().mapToObj(x -> (char) x).map(Object::toString).toList();
  }

  public int getPriority() {
    return CollectionUtils.intersection(first, second).stream()
        .distinct()
        .map(x -> priority.indexOf(x))
        .reduce(0, Integer::sum);
  }
}
