package com.nickbenn.adventofcode.day3;

import com.nickbenn.adventofcode.util.DataSource;
import com.nickbenn.adventofcode.util.Defaults;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collector;
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
          .mapToInt((line) -> {
            int rucksackLength = line.length() / 2;
            String left = line.substring(0, rucksackLength);
            String right = line.substring(rucksackLength);
            return priority(getMatch(left, right).get(0));
          })
          .sum();
    }
  }

  public int getBadgePrioritySum() throws IOException {
    try (Stream<String> lines = new DataSource(getClass(), inputFile).lines()) {
      List<String> pending = new LinkedList<>();
      AtomicInteger priority = new AtomicInteger();
      lines
          .forEachOrdered((line) -> {
            pending.add(line);
            if (pending.size() == GROUP_SIZE) {
              priority.addAndGet(priority(getMatch(pending.toArray(new String[0])).get(0)));
              pending.clear();
            }
          });
      return priority.get();
    }
  }

  private List<Integer> getMatch(String... lines) {
    return Stream.of(lines)
        .collect(
            Collector.of(
                Intersection::new,
                (Intersection intersection, String line) ->
                    intersection.intersect(line.chars().boxed().collect(Collectors.toSet())),
                (intersection1, intersection2) ->
                    new Intersection(intersection1.getContent(), intersection2.getContent()),
                (intersection) -> new LinkedList<>(intersection.getContent())
            )
        );
  }

  private int priority(int ch) {
    return ch + (Character.isLowerCase(ch) ? LOWER_CASE_OFFSET : UPPER_CASE_OFFSET);
  }

  private static class Intersection {

    private Set<Integer> content;

    @SafeVarargs
    public Intersection(Collection<Integer>... universes) {
      for (Collection<Integer> universe : universes) {
        intersect(universe);
      }
    }

    public void intersect(Collection<Integer> retain) {
      if (content == null) {
        content = new HashSet<>(retain);
      } else {
        content.retainAll(retain);
      }
    }

    public Set<Integer> getContent() {
      return content;
    }

  }

}
