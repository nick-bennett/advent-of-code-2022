package com.nickbenn.adventofcode.day9;

import com.nickbenn.adventofcode.util.Direction;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public record Movement(Direction direction, int distance) {

  private static final Pattern MOVEMENT_PATTERN = Pattern.compile("^\\s*(\\S)\\s*([-+]?\\d+)\\s*$");
  private static final Map<Character, Direction> CHARACTERS_TO_DIRECTIONS = Map.of(
      'U', Direction.NORTH,
      'R', Direction.EAST,
      'D', Direction.SOUTH,
      'L', Direction.WEST
  );

  public static Movement parse(String input) {
    Matcher matcher = MOVEMENT_PATTERN.matcher(input);
    if (!matcher.matches()) {
      throw new IllegalArgumentException();
    }
    return new Movement(CHARACTERS_TO_DIRECTIONS.get(matcher.group(1).charAt(0)),
        Integer.parseInt(matcher.group(2)));
  }

}
