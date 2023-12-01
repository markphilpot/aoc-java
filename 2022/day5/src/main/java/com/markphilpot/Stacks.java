package com.markphilpot;

import java.io.InputStream;
import java.util.*;
import java.util.regex.MatchResult;
import java.util.stream.IntStream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Stacks {
  private static final Logger log = LogManager.getLogger(Stacks.class);

  public record Move(int num, int from, int to) {}

  private List<Stack<String>> stacks;
  private List<Move> moves;

  public Stacks(List<Stack<String>> stacks, List<Move> moves) {
    this.stacks = stacks;
    this.moves = moves;
  }

  public void run() {
    moves.forEach(this::move);
  }

  public void runBatch() {
    moves.forEach(this::moveBatch);
  }

  private void move(Move move) {
    IntStream.range(0, move.num)
        .forEach(
            x -> {
              var el = stacks.get(move.from - 1).pop();
              stacks.get(move.to - 1).push(el);
            });
  }

  private void moveBatch(Move move) {
    var tmp = new Stack<String>();

    IntStream.range(0, move.num)
        .forEach(
            x -> {
              var el = stacks.get(move.from - 1).pop();
              tmp.push(el);
            });

    IntStream.range(0, tmp.size())
        .forEach(
            x -> {
              stacks.get(move.to - 1).push(tmp.pop());
            });
  }

  public List<String> peek() {
    return stacks.stream().map(Stack::peek).toList();
  }

  public static Stacks parse(InputStream inputStream) {
    try (var scanner = new Scanner(inputStream).useDelimiter("\n")) {

      var initialState = new ArrayList<String>();
      var moves = new ArrayList<String>();

      var stateComplete = false;

      while (scanner.hasNext()) {
        var line = scanner.next();

        if (line.isEmpty()) {
          stateComplete = true;
        } else if (!stateComplete) {
          initialState.add(line);
        } else {
          moves.add(line);
        }
      }

      var columnDef = initialState.get(initialState.size() - 1);
      initialState.remove(initialState.size() - 1);

      var cols = Arrays.stream(columnDef.trim().split(" ")).filter(x -> !x.isBlank()).toList();
      var num = cols.size();

      List<Stack<String>> columns = new ArrayList<>(num);
      IntStream.range(0, num).forEach(x -> columns.add(new Stack<>()));

      Collections.reverse(initialState); // Need java 21 to get List.reverse *sigh*
      initialState.forEach(
          line -> {
            var elements = LineParsing.lineToList(line);

            IntStream.range(0, num)
                .forEach(
                    x -> {
                      var index = x * 4 + 1;
                      if (index > elements.size()) return;

                      var state = elements.get(index);
                      if (!state.isBlank()) {
                        columns.get(x).push(state);
                      }
                    });
          });

      var moveList =
          moves.stream()
              .map(
                  line -> {
                    var s = new Scanner(line).useDelimiter("move (\\d+) from (\\d+) to (\\d+)");
                    s.findInLine("move (\\d+) from (\\d+) to (\\d+)");
                    MatchResult result = s.match();
                    return new Move(
                        Integer.parseInt(result.group(1)),
                        Integer.parseInt(result.group(2)),
                        Integer.parseInt(result.group(3)));
                  })
              .toList();

      return new Stacks(columns, moveList);
    }
  }

  @Override
  public String toString() {
    return "Stacks{" + "stacks=" + stacks + ", moves=" + moves + '}';
  }
}
