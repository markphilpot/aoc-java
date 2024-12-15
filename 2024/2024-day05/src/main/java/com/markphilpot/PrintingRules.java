package com.markphilpot;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;
import java.util.*;

public class PrintingRules {
  private static final Logger log = LogManager.getLogger(PrintingRules.class);

  public record OrderRule(int first, int second) {};
  public record OrderRun(List<Integer> run) {
    public Integer getMid() {
      return run.get(run.size()/2);
    }
  };
  public record OrderPackage(List<OrderRule> rules, List<OrderRun> printRuns) {}

  public static OrderPackage parse(InputStream input) {
    var lines = ParsingUtils.streamToList(input);

    var rules = new ArrayList<OrderRule>();
    var toPrint = new ArrayList<OrderRun>();
    var rulesDone = false;

    for(var line : lines) {
      if(line.isEmpty()) {
        rulesDone = true;
      } else if(!rulesDone) {
        var elements = line.split("\\|");
        rules.add(new OrderRule(Integer.parseInt(elements[0]), Integer.parseInt(elements[1])));
      } else {
        var elements = line.split(",");
        toPrint.add(new OrderRun(Arrays.stream(elements).map(Integer::parseInt).toList()));
      }
    }

    return new OrderPackage(rules, toPrint);
  }

  public static List<OrderRun> findValidRuns(OrderPackage pkg) {
    var valid = new ArrayList<OrderRun>();

    for(OrderRun run : pkg.printRuns) {
      var applicableRules = getApplicableRules(pkg.rules, run);

      if(isValid(applicableRules, run)) {
        valid.add(run);
      }
    }

    return valid;
  }

  public static List<OrderRun> findInvalidRuns(OrderPackage pkg) {
    var notValid = new ArrayList<OrderRun>();

    for(OrderRun run : pkg.printRuns) {
      var applicableRules = getApplicableRules(pkg.rules, run);

      if(!isValid(applicableRules, run)) {
        notValid.add(run);
      }
    }

    return notValid;
  }

  public static List<OrderRule> getApplicableRules(List<OrderRule> rules, OrderRun run) {
    var hash = new HashSet<>(run.run);
    return rules.stream().filter(rule -> hash.contains(rule.first) && hash.contains(rule.second)).toList();
  }

  private record OrderCheck(Integer val, Set<Integer> before, Set<Integer> after) implements Comparable<OrderCheck> {
    @Override
    public int compareTo(OrderCheck oc) {
      if(before.contains(oc.val)) {
        return 1;
      } else if(after.contains(oc.val)) {
        return -1;
      } else {
        return 0;
      }
    }
  };

  private static boolean isValid(List<OrderRule> rules, OrderRun run) {
    var checks = new HashMap<Integer, OrderCheck>();

    for(int i=0; i < run.run.size(); i++) {
      var val = -1;
      var before = new HashSet<Integer>();
      var after = new HashSet<Integer>();
      for(int j=0; j < run.run.size(); j++) {
        if(j<i) {
          before.add(run.run.get(j));
        } else if(j>i) {
          after.add(run.run.get(j));
        } else {
          val = run.run.get(j);
        }
      }

      checks.put(val, new OrderCheck(val, before, after));
    }

    var rulesPassed = true;

    for(var rule : rules) {
      var check = checks.get(rule.first);

      if(!check.after.contains(rule.second)) {
        rulesPassed = false;
      }
    }

    return rulesPassed;
  }
}
