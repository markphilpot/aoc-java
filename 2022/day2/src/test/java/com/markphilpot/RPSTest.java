package com.markphilpot;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class RPSTest {
    private static final Logger log = LogManager.getLogger(RPSTest.class);

    @Test
    public void testSamplePart1() {
        var inputStream = RPSTest.class.getClassLoader().getResourceAsStream("sample.txt");

        var encoding = Map.of(
                "A", Game.Move.ROCK,
                "B", Game.Move.PAPER,
                "C", Game.Move.SCISSORS,
                "X", Game.Move.ROCK,
                "Y", Game.Move.PAPER,
                "Z", Game.Move.SCISSORS
        );

        var scanner = new Scanner(inputStream).useDelimiter("\n");

        List<Game> games = new ArrayList<>();

        while(scanner.hasNext()) {
            var line = scanner.next();
            var elements = line.split(" ");
            games.add(new Game(encoding.get(elements[0]), encoding.get(elements[1])));
        }

        var totalScore = games.stream().map(Game::getScore).reduce(0, Integer::sum);

        assertThat(totalScore, is(15));
    }

    @Test
    public void testInputPart1() {
        var inputStream = RPSTest.class.getClassLoader().getResourceAsStream("input.txt");

        var encoding = Map.of(
                "A", Game.Move.ROCK,
                "B", Game.Move.PAPER,
                "C", Game.Move.SCISSORS,
                "X", Game.Move.ROCK,
                "Y", Game.Move.PAPER,
                "Z", Game.Move.SCISSORS
        );

        var scanner = new Scanner(inputStream).useDelimiter("\n");

        List<Game> games = new ArrayList<>();

        while(scanner.hasNext()) {
            var line = scanner.next();
            var elements = line.split(" ");
            games.add(new Game(encoding.get(elements[0]), encoding.get(elements[1])));
        }

        var totalScore = games.stream().map(Game::getScore).reduce(0, Integer::sum);

//        assertThat(totalScore, is(15));
        log.info(totalScore);
    }

    @Test
    public void testSamplePart2() {
        var inputStream = RPSTest.class.getClassLoader().getResourceAsStream("sample.txt");

        var encoding = Map.of(
                "A", Game.Move.ROCK,
                "B", Game.Move.PAPER,
                "C", Game.Move.SCISSORS,
                "X", Game.Outcome.LOSE,
                "Y", Game.Outcome.DRAW,
                "Z", Game.Outcome.WIN
        );

        var scanner = new Scanner(inputStream).useDelimiter("\n");

        List<Game> games = new ArrayList<>();

        while(scanner.hasNext()) {
            var line = scanner.next();
            var elements = line.split(" ");
            games.add(new Game((Game.Move) encoding.get(elements[0]), (Game.Outcome) encoding.get(elements[1])));
        }

        var totalScore = games.stream().map(Game::getScore).reduce(0, Integer::sum);

        assertThat(totalScore, is(12));
    }

    @Test
    public void testInputPart2() {
        var inputStream = RPSTest.class.getClassLoader().getResourceAsStream("input.txt");

        var encoding = Map.of(
                "A", Game.Move.ROCK,
                "B", Game.Move.PAPER,
                "C", Game.Move.SCISSORS,
                "X", Game.Outcome.LOSE,
                "Y", Game.Outcome.DRAW,
                "Z", Game.Outcome.WIN
        );

        var scanner = new Scanner(inputStream).useDelimiter("\n");

        List<Game> games = new ArrayList<>();

        while(scanner.hasNext()) {
            var line = scanner.next();
            var elements = line.split(" ");
            games.add(new Game((Game.Move) encoding.get(elements[0]), (Game.Outcome) encoding.get(elements[1])));
        }

        var totalScore = games.stream().map(Game::getScore).reduce(0, Integer::sum);

//        assertThat(totalScore, is(12));
        log.info(totalScore);
    }
}
