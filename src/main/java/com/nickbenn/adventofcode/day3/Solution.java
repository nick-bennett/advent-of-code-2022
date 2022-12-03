package com.nickbenn.adventofcode.day3;

import com.nickbenn.adventofcode.util.DataSource;
import com.nickbenn.adventofcode.util.Defaults;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Solution {

  private static final int LOWER_CASE_OFFSET = 1 - 'a';
  private static final int UPPER_CASE_OFFSET = 27 - 'A';
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
    System.out.println(solution.getBadgePrioritySum());
  }

  public int getMisplacedPrioritySum() throws IOException {
    try (Stream<String> lines = new DataSource(getClass(), inputFile).lines()) {
      return lines
          .mapToInt(this::getMisplacedItemPriority)
          .sum();
    }
  }

  public int getBadgePrioritySum() throws IOException {
    try (Stream<String> lines = new DataSource(getClass(), inputFile).lines()) {
      Collection<String> pending = new LinkedList<>();
      return lines
          .mapToInt((line) -> getBadgePriority(line, pending))
          .sum();
    }
  }

  private int getMisplacedItemPriority(String line) {
    int rucksackLength = line.length() / 2;
    String left = line.substring(0, rucksackLength);
    String right = line.substring(rucksackLength);
    return priority(intersection(List.of(left, right)).get(0));
  }

  private int getBadgePriority(String line, Collection<String> pending) {
    int priority = 0;
    pending.add(line);
    if (pending.size() == GROUP_SIZE) {
      priority = priority(intersection(pending).get(0));
      pending.clear();
    }
    return priority;
  }

  private int priority(int ch) {
    return ch + (Character.isLowerCase(ch) ? LOWER_CASE_OFFSET : UPPER_CASE_OFFSET);
  }

  private List<Integer> intersection(Collection<String> lines) {
    Set<Integer> intersection;
    Iterator<String> iterator = lines.iterator();
    if (iterator.hasNext()) {
      intersection = charSet(iterator.next(), true);
      iterator.forEachRemaining((line) -> intersection.retainAll(charSet(line, false)));
    } else {
      intersection = Set.of();
    }
    return new LinkedList<>(intersection);
  }

  private Set<Integer> charSet(String source, boolean guaranteedMutable) {
    Stream<Integer> chars = source.chars().boxed();
    return guaranteedMutable
        ? chars.collect(Collectors.toCollection(HashSet::new))
        : chars.collect(Collectors.toSet());
  }

}
