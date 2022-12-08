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

public class RucksackReorganization {

  private static final int LOWER_CASE_OFFSET = 1 - 'a';
  private static final int UPPER_CASE_OFFSET = 27 - 'A';
  private static final int GROUP_SIZE = 3;

  private final String inputFile;

  public RucksackReorganization() {
    this(Defaults.INPUT_FILE);
  }

  public RucksackReorganization(String inputFile) {
    this.inputFile = inputFile;
  }

  public static void main(String[] args) throws IOException {
    RucksackReorganization reorg = new RucksackReorganization();
    System.out.println(reorg.getMisplacedPrioritySum());
    System.out.println(reorg.getBadgePrioritySum());
  }

  public int getMisplacedPrioritySum() throws IOException {
    try (Stream<String> lines = DataSource.simpleLines(inputFile, getClass())) {
      return lines
          .mapToInt(this::getMisplacedItemPriority)
          .sum();
    }
  }

  public int getBadgePrioritySum() throws IOException {
    try (
        Stream<Stream<String>> chunks = new DataSource.Builder()
            .setInputFile(inputFile)
            .setContext(getClass())
            .build()
            .chunkedLines(GROUP_SIZE)
    ) {
      return chunks
          .mapToInt(this::getBadgePriority)
          .sum();
    }
  }

  private int getMisplacedItemPriority(String line) {
    int rucksackLength = line.length() / 2;
    String left = line.substring(0, rucksackLength);
    String right = line.substring(rucksackLength);
    return priority(intersection(List.of(left, right)).get(0));
  }

  private int getBadgePriority(Stream<String> chunk) {
    return priority(intersection(chunk.collect(Collectors.toList())).get(0));
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
