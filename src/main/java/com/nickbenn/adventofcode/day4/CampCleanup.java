package com.nickbenn.adventofcode.day4;

import com.nickbenn.adventofcode.util.DataSource;
import com.nickbenn.adventofcode.util.Defaults;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class CampCleanup {

  private static final Pattern ASSIGNMENT_PARSER =
      Pattern.compile("^(\\d+)-(\\d+),(\\d+)-(\\d+)\\s*$");

  private final String inputFile;

  public CampCleanup() {
    this(Defaults.INPUT_FILE);
  }

  public CampCleanup(String inputFile) {
    this.inputFile = inputFile;
  }

  public static void main(String[] args) throws IOException {
    CampCleanup cleanup = new CampCleanup();
    System.out.println(cleanup.getFullRedundancyCount());
    System.out.println(cleanup.getPartialRedundancyCount());
  }

  public long getFullRedundancyCount() throws IOException {
    try (Stream<String> lines = DataSource.simpleLines(inputFile, getClass())) {
      return lines
          .map(ASSIGNMENT_PARSER::matcher)
          .filter(Matcher::matches)
          .map(this::groups)
          .filter(this::redundant)
          .count();
    }
  }

  public long getPartialRedundancyCount() throws IOException {
    try (Stream<String> lines = DataSource.simpleLines(inputFile, getClass())) {
      return lines
          .map(ASSIGNMENT_PARSER::matcher)
          .filter(Matcher::matches)
          .map(this::groups)
          .filter(this::overlapping)
          .count();
    }
  }

  private int[] groups(Matcher matcher) {
    int[] values = new int[matcher.groupCount()];
    for (int i = 0; i < values.length; i++) {
      values[i] = Integer.parseInt(matcher.group(i + 1));
    }
    return values;
  }

  private boolean redundant(int[] ranges) {
    return ranges[0] <= ranges[2] && ranges[1] >= ranges[3]
        || ranges[0] >= ranges[2] && ranges[1] <= ranges[3];
  }

  private boolean overlapping(int[] ranges) {
    return !(ranges[1] < ranges[2] || ranges[0] > ranges[3]);
  }

}
