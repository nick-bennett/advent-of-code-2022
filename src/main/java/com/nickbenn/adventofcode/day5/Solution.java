package com.nickbenn.adventofcode.day5;

import com.nickbenn.adventofcode.util.DataSource;
import com.nickbenn.adventofcode.util.Defaults;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class Solution {

  private static final Pattern ARRANGEMENT_LINE_PATTERN = Pattern.compile("^.*\\[.].*$");
  private static final Pattern CRATE_PATTERN = Pattern.compile("\\s?(?:\\[(.)]|\\s{3})");
  private static final Pattern MOVEMENT_PATTERN = Pattern.compile("^move\\D+(\\d+)\\D+(\\d+)\\D+(\\d+).*$");

  private final String inputFile;
  private final List<List<Character>> piles;
  private final List<Move> moves;

  public Solution() {
    this(Defaults.INPUT_FILE);
  }

  public Solution(String inputFile) {
    this.inputFile = inputFile;
    piles = new ArrayList<>();
    moves = new LinkedList<>();
  }

  public static void main(String[] args) throws IOException {
    System.out.println(new Solution().getSingleMoveStackTops());
    System.out.println(new Solution().getMultiMoveStackTops());
  }

  public String getSingleMoveStackTops() throws IOException {
    try (Stream<String> lines = source()) {
      lines.forEach(this::processLine);
    }
    processSingleMoves();
    return getStackTops();
  }

  public String getMultiMoveStackTops() throws IOException {
    try (Stream<String> lines = source()) {
      lines.forEach(this::processLine);
    }
    processMultiMoves();
    return getStackTops();
  }

  private Stream<String> source() throws IOException {
    return new DataSource.Builder()
        .setInputFile(inputFile)
        .setContext(getClass())
        .setTrimmed(false)
        .setStripped(false)
        .build()
        .lines();
  }

  private void processLine(String line) {
    Matcher matcher;
    if (ARRANGEMENT_LINE_PATTERN.matcher(line).matches()) {
      parseArrangement(line);
    } else if ((matcher = MOVEMENT_PATTERN.matcher(line)).matches()) {
      parseMove(matcher);
    }
  }

  private void parseArrangement(String line) {
    int position = 0;
    int pile = -1;
    for (
        Matcher crateMatcher = CRATE_PATTERN.matcher(line);
        crateMatcher.find(position);
        position = crateMatcher.end()
    ) {
      pile++;
      while (piles.size() <= pile) {
        piles.add(new LinkedList<>());
      }
      if (crateMatcher.group(1) != null) {
        piles.get(pile).add(crateMatcher.group(1).charAt(0));
      }
    }
  }

  private void parseMove(Matcher matcher) {
    moves.add(
        new Move(
            Integer.parseInt(matcher.group(1)),
            Integer.parseInt(matcher.group(2)) - 1,
            Integer.parseInt(matcher.group(3)) - 1
        )
    );
  }

  private void processSingleMoves() {
    for (Move move : moves) {
      int quantity = move.quantity();
      List<Character> fromPile = piles.get(move.fromPile());
      List<Character> toPile = piles.get(move.toPile());
      for (int i = 0; i < quantity; i++) {
        toPile.add(0, fromPile.remove(0));
      }
    }
  }

  private void processMultiMoves() {
    for (Move move : moves) {
      int quantity = move.quantity();
      List<Character> fromPile = piles.get(move.fromPile());
      List<Character> toPile = piles.get(move.toPile());
      List<Character> buffer = fromPile.subList(0, quantity);
      toPile.addAll(0, buffer);
      buffer.clear();
    }
  }

  private String getStackTops() {
    int[] chars = piles
        .stream()
        .mapToInt((pile) -> pile.isEmpty() ? ' ' : pile.get(0))
        .toArray();
    return new String(chars, 0, chars.length);
  }

  private record Move(int quantity, int fromPile, int toPile) {}

}
