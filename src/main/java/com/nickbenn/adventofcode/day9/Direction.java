package com.nickbenn.adventofcode.day9;

public enum Direction {

  UP('U', 0, 1),
  RIGHT('R', 1, 0),
  DOWN('D', 0, -1),
  LEFT('L', -1, 0);

  private final char initial;
  private final int deltaX;
  private final int deltaY;

  Direction(char initial, int deltaX, int deltaY) {
    this.initial = initial;
    this.deltaX = deltaX;
    this.deltaY = deltaY;
  }

  public static Direction valueOf(char initial) {
    Direction match = null;
    for (Direction d : values()) {
      if (d.initial == initial) {
        match = d;
        break;
      }
    }
    if (match == null) {
      throw new IllegalArgumentException();
    }
    return match;
  }

  public char getInitial() {
    return initial;
  }

  public int getDeltaX() {
    return deltaX;
  }

  public int getDeltaY() {
    return deltaY;
  }

}
