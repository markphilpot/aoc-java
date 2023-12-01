package com.markphilpot;

import java.util.List;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RucksackGroup {
  private static final Logger log = LogManager.getLogger(RucksackGroup.class);
  public static String priority = "_abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
  private final List<String> first;
  private final List<String> second;
  private final List<String> third;

  public RucksackGroup(String first, String second, String third) {
    this.first = first.chars().mapToObj(x -> (char) x).map(Object::toString).toList();
    this.second = second.chars().mapToObj(x -> (char) x).map(Object::toString).toList();
    this.third = third.chars().mapToObj(x -> (char) x).map(Object::toString).toList();
  }

  public int getPriority() {
    return CollectionUtils.intersection(CollectionUtils.intersection(first, second), third).stream()
        .distinct()
        .map(x -> priority.indexOf(x))
        .reduce(0, Integer::sum);
  }
}
