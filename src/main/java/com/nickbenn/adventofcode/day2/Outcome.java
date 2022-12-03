package com.nickbenn.adventofcode.day2;

import java.util.Map;

enum Outcome {

  LOSE(0),
  DRAW(3),
  WIN(6);

  private final int value;

  private static final Map<Outcome, Map<Strategy, Strategy>> REQUIREMENTS = Map.of(
      LOSE, Map.of(
          Strategy.ROCK, Strategy.SCISSORS,
          Strategy.PAPER, Strategy.ROCK,
          Strategy.SCISSORS, Strategy.PAPER
      ),
      DRAW, Map.of(
          Strategy.ROCK, Strategy.ROCK,
          Strategy.PAPER, Strategy.PAPER,
          Strategy.SCISSORS, Strategy.SCISSORS
      ),
      WIN, Map.of(
          Strategy.ROCK, Strategy.PAPER,
          Strategy.PAPER, Strategy.SCISSORS,
          Strategy.SCISSORS, Strategy.ROCK
      )
  );

  Outcome(int value) {
    this.value = value;
  }

  public int totalValue(Strategy opponentStrategy) {
    return value + requiredStrategy(opponentStrategy).getValue();
  }

  public Strategy requiredStrategy(Strategy opponentStrategy) {
    return REQUIREMENTS.get(this).get(opponentStrategy);
  }

  public static Outcome valueOf(char code) {
    return switch (code) {
      case 'X' -> LOSE;
      case 'Y' -> DRAW;
      case 'Z' -> WIN;
      default -> null;
    };
  }

}
