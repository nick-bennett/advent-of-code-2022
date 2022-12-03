package com.nickbenn.adventofcode.day3;

import com.nickbenn.adventofcode.util.DataSource;
import com.nickbenn.adventofcode.util.Defaults;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Solution {

  private static final char LOWER_CASE_BASE = 'a';
  private static final int LOWER_CASE_BASE_PRIORITY = 1;
  private static final char UPPER_CASE_BASE = 'A';
  private static final int UPPER_CASE_BASE_PRIORITY = 27;
  private static final int GROUP_SIZE = 3;

  private final String inputFile;

  public Solution() {
    this(Defaults.INPUT_FILE);
  }

  public Solution(String inputFile) {
    this.inputFile = inputFile;
  }

  public static void main(String[] args) throws IOException {
    Solution solution = new Solution();
    System.out.println(solution.getMisplacedPrioritySum());
    System.out.println(solution.getBadgeSum());
  }

  public int getMisplacedPrioritySum() throws IOException {
    try (Stream<String> lines = new DataSource(getClass(), inputFile).lines()) {
      return lines
          .mapToInt((line) -> {
            int rucksackLength = line.length() / 2;
            String left = line.substring(0, rucksackLength);
            String right = line.substring(rucksackLength);
            int match = getMatch(left, right).get(0);
            return Character.isLowerCase(match)
                ? (match - LOWER_CASE_BASE + LOWER_CASE_BASE_PRIORITY)
                : (match - UPPER_CASE_BASE + UPPER_CASE_BASE_PRIORITY);
          })
          .sum();
    }
  }

  public int getBadgeSum() throws IOException {
    try (Stream<String> lines = new DataSource(getClass(), inputFile).lines()) {
      List<String> pending = new LinkedList<>();
      AtomicInteger priority = new AtomicInteger();
      lines
          .forEachOrdered((line) -> {
            pending.add(line);
            if (pending.size() == GROUP_SIZE) {
              int badge = getMatch(pending.toArray(new String[0])).get(0);
              priority.addAndGet(
                  Character.isLowerCase(badge)
                      ? (badge - LOWER_CASE_BASE + LOWER_CASE_BASE_PRIORITY)
                      : (badge - UPPER_CASE_BASE + UPPER_CASE_BASE_PRIORITY)
              );
              pending.clear();
            }
          });
      return priority.get();
    }
  }

  private List<Integer> getMatch(String... lines) {
    List<Integer> result;
    if (lines.length > 0) {
      Set<Integer> intersection = lines[0]
          .chars()
          .boxed()
          .collect(Collectors.toCollection(HashSet::new));
      for (int i = 1; i < lines.length; i++) {
        Set<Integer> next = lines[i]
            .chars()
            .boxed()
            .collect(Collectors.toSet());
        intersection.retainAll(next);
      }
      result = new LinkedList<>(intersection);
    } else {
      result = new LinkedList<>();
    }
    return result;
  }

}
