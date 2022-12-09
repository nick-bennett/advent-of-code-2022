package com.nickbenn.adventofcode.day9;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public record Movement(Direction direction, int distance) {

  private static final Pattern MOVEMENT_PATTERN = Pattern.compile("^\\s*(\\S)\\s*([-+]?\\d+)\\s*$");

  public static Movement parse(String input) {
    Matcher matcher = MOVEMENT_PATTERN.matcher(input);
    if (!matcher.matches()) {
      throw new IllegalArgumentException();
    }
    return new Movement(
        Direction.valueOf(matcher.group(1).charAt(0)), Integer.parseInt(matcher.group(2)));
  }

}
