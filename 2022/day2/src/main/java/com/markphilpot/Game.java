package com.markphilpot;

public class Game {
  public enum Move {
    ROCK,
    PAPER,
    SCISSORS;
  }

  public enum Outcome {
    WIN,
    LOSE,
    DRAW;
  }

  private Move opponent;
  private Move self;

  public Game(Move opponent, Move self) {
    this.opponent = opponent;
    this.self = self;
  }

  public Game(Move opponent, Outcome outcome) {
    this.opponent = opponent;
    this.self = getSelfForOutcome(outcome);
  }

  private Move getSelfForOutcome(Outcome outcome) {
    return switch (outcome) {
      case WIN -> switch (opponent) {
        case ROCK -> Move.PAPER;
        case PAPER -> Move.SCISSORS;
        case SCISSORS -> Move.ROCK;
      };
      case LOSE -> switch (opponent) {
        case ROCK -> Move.SCISSORS;
        case PAPER -> Move.ROCK;
        case SCISSORS -> Move.PAPER;
      };
      case DRAW -> switch (opponent) {
        case ROCK -> Move.ROCK;
        case PAPER -> Move.PAPER;
        case SCISSORS -> Move.SCISSORS;
      };
    };
  }

  public Move getOpponent() {
    return opponent;
  }

  public Move getSelf() {
    return self;
  }

  public int getScore() {
    return getScoreOutcome() + getScoreSelf();
  }

  public int getScoreOutcome() {
    return switch (self) {
      case ROCK -> switch (opponent) {
        case ROCK -> 3;
        case PAPER -> 0;
        case SCISSORS -> 6;
      };
      case PAPER -> switch (opponent) {
        case ROCK -> 6;
        case PAPER -> 3;
        case SCISSORS -> 0;
      };
      case SCISSORS -> switch (opponent) {
        case ROCK -> 0;
        case PAPER -> 6;
        case SCISSORS -> 3;
      };
    };
  }

  public int getScoreSelf() {
    return switch (self) {
      case ROCK -> 1;
      case PAPER -> 2;
      case SCISSORS -> 3;
    };
  }

  @Override
  public String toString() {
    return "Game{" + "opponent=" + opponent + ", self=" + self + '}';
  }
}
