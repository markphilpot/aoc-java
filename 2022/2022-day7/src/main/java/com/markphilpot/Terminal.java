package com.markphilpot;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.MatchResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Terminal {
  private static final Logger log = LogManager.getLogger(Terminal.class);

  public static List<String> readStream(InputStream inputStream) {
    try (var scanner = new Scanner(inputStream).useDelimiter("\n")) {
      var lines = new ArrayList<String>();
      while (scanner.hasNext()) {
        lines.add(scanner.next());
      }
      return lines;
    }
  }

  public static void init(List<String> commands, Filesystem.Directory root) {
    Filesystem.Directory wd = root;

    for (var i = 0; i < commands.size(); i++) {
      var cmd = commands.get(i);

      if (cmd.startsWith("$")) {
        switch (cmd) {
          case "$ cd /":
            // Special root case
            break;
          case "$ ls":
            // get output for cd
            break;
          case "$ cd ..":
            // Go up
            wd = wd.getParent();
            break;
          default:
            {
              var s = new Scanner(cmd);
              s.findInLine("\\$ cd (\\w+)");
              MatchResult result = s.match();
              var targetDir = result.group(1);

              wd = wd.getDir(targetDir);
            }
        }
      } else {
        // output from ls
        var el = cmd.split(" ");
        if (el[0].equals("dir")) {
          wd.addDir(new Filesystem.Directory(el[1], wd));
        } else {
          wd.addFile(new Filesystem.File(el[1], Integer.parseInt(el[0])));
        }
      }
    }
  }
}
