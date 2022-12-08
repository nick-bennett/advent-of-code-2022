package com.nickbenn.adventofcode.day7;

import com.nickbenn.adventofcode.day7.Entry.Directory;
import com.nickbenn.adventofcode.util.DataSource;
import com.nickbenn.adventofcode.util.Defaults;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class NoSpace {

  private static final Pattern COMMAND_PATTERN = Pattern.compile("^\\$\\s+(?:ls|cd\\s+(\\S+)).*$");
  private static final String ROOT_DIRECTORY = "/";
  private static final String PARENT_DIRECTORY = "..";
  private static final int THRESHOLD = 100_000;
  private static final int TOTAL_CAPACITY = 70_000_000;
  private static final int UPDATE_SPACE_NEEDED = 30_000_000;

  private final Entry.Directory root;

  private Entry.Directory current;

  public NoSpace() throws IOException {
    this(Defaults.INPUT_FILE);
  }

  public NoSpace(String inputFile) throws IOException {
    try (Stream<String> lines = DataSource.simpleLines(inputFile, getClass())) {
      root = Entry.makeRoot();
      lines.forEachOrdered(this::parse);
    }
  }

  public static void main(String[] args) throws IOException {
    NoSpace noSpace = new NoSpace();
    System.out.println(noSpace.getSumUnderThreshold());
    System.out.println(noSpace.getRemovalCandidate());
  }

  public int getSumUnderThreshold() {
    return root
        .flatTree()
        .mapToInt(Directory::size)
        .filter((size) -> size <= THRESHOLD)
        .sum();
  }

  public int getRemovalCandidate() {
    int spaceAvailable = TOTAL_CAPACITY - root.size();
    int additionalSpaceNeeded = UPDATE_SPACE_NEEDED - spaceAvailable;
    return root
        .flatTree()
        .mapToInt(Directory::size)
        .filter((size) -> size >= additionalSpaceNeeded)
        .min()
        .orElseThrow();
  }

  private void parse( String line) {
    Matcher matcher = COMMAND_PATTERN.matcher(line);
    if (matcher.matches()) {
      current = execute(current, matcher);
    } else {
      current.parse(line);
    }
  }

  private Directory execute(Directory context, Matcher matcher) {
    String target = matcher.group(1);
    return (target != null)
        ? switch (target) {
          case ROOT_DIRECTORY -> root;
          case PARENT_DIRECTORY -> context.parent();
          default -> context.getSubdirectory(target);
        }
        : context;
  }

}
