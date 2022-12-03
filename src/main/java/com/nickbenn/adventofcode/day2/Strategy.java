package com.nickbenn.adventofcode.day2;

import java.util.Map;

enum Strategy {

  ROCK(1),
  PAPER(2),
  SCISSORS(3);

  private static final Map<Strategy, Map<Strategy, Integer>> VALUES = Map.of(
      ROCK, Map.of(
          ROCK, 3,
          PAPER, 0,
          SCISSORS, 6
      ),
      PAPER, Map.of(
          ROCK, 6,
          PAPER, 3,
          SCISSORS, 0
      ),
      SCISSORS, Map.of(
          ROCK, 0,
          PAPER, 6,
          SCISSORS, 3
      )
  );

  private final int value;

  Strategy(int value) {
    this.value = value;
  }

  public int outcome(Strategy opponentStrategy) {
    return VALUES.get(this).get(opponentStrategy) + value;
  }

  public static Strategy valueOf(char code) {
    return switch (code) {
      case 'A', 'X' -> ROCK;
      case 'B', 'Y' -> PAPER;
      case 'C', 'Z' -> SCISSORS;
      default -> null;
    };
  }

  public int getValue() {
    return value;
  }

}
