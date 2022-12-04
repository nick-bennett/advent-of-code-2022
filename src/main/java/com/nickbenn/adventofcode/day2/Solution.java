package com.nickbenn.adventofcode.day2;

import com.nickbenn.adventofcode.util.DataSource;
import com.nickbenn.adventofcode.util.Defaults;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class Solution {

  private static final Pattern STRATEGY_PARSER = Pattern.compile("^\\s*([A-C])\\s+([X-Z])\\s*$");

  private final String inputFile;

  public Solution() {
    this(Defaults.INPUT_FILE);
  }

  public Solution(String inputFile) {
    this.inputFile = inputFile;
  }

  public static void main(String[] args) throws IOException {
    Solution solution = new Solution();
    System.out.println(solution.getStrategyBasedValue());
    System.out.println(solution.getOutcomeBasedValue());
  }

  public int getStrategyBasedValue() throws IOException {
    try (Stream<String> lines = DataSource.simpleLines(inputFile, getClass())) {
      return lines
          .map(STRATEGY_PARSER::matcher)
          .filter(Matcher::matches)
          .mapToInt((matcher) -> Strategy.valueOf(matcher.group(2).charAt(0))
              .outcome(Strategy.valueOf(matcher.group(1).charAt(0))))
          .sum();
    }
  }

  public int getOutcomeBasedValue() throws IOException {
    try (Stream<String> lines = DataSource.simpleLines(inputFile, getClass())) {
      return lines
          .map(STRATEGY_PARSER::matcher)
          .filter(Matcher::matches)
          .mapToInt((matcher) -> Outcome.valueOf(matcher.group(2).charAt(0))
              .totalValue(Strategy.valueOf(matcher.group(1).charAt(0))))
          .sum();
    }
  }

}
