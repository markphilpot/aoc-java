package com.markphilpot;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class InstructionParser {
  private static final Logger log = LogManager.getLogger(InstructionParser.class);

  public static String mulRegex = "^mul\\(([\\d]{1,3}),([\\d]{1,3})\\)";
  public static String doRegex = "^do\\(\\)";
  public static String dontRegex = "^don't\\(\\)";

  public record Mul(int x, int y) {
    public int execute() {
      return x * y;
    }
  };

  public static List<Mul> parse(String input) {
    var pattern = Pattern.compile(mulRegex);

    var instructions = new ArrayList<Mul>();

    for(int i = 0; i < input.length(); i++) {
      var sub = input.substring(i);

      var matcher = pattern.matcher(sub);

      while(matcher.find()) {
        instructions.add(new Mul(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2))));
      }
    }

    return instructions;
  }

  public static List<Mul> parseExtended(String input) {
    var mulPattern = Pattern.compile(mulRegex);
    var startPattern = Pattern.compile(doRegex);
    var stopPattern = Pattern.compile(dontRegex);

    var capture = true;

    var instructions = new ArrayList<Mul>();

    for(int i = 0; i < input.length(); i++) {
      var sub = input.substring(i);

      var matcher = mulPattern.matcher(sub);

      if(matcher.find() & capture) {
        instructions.add(new Mul(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2))));
        continue;
      }

      // Check Dos & Don'ts
      matcher = startPattern.matcher(sub);

      if(matcher.find()) {
        capture = true;
        continue;
      }

      matcher = stopPattern.matcher(sub);

      if(matcher.find()) {
        capture = false;
        continue;
      }
    }

    return instructions;
  }
}
