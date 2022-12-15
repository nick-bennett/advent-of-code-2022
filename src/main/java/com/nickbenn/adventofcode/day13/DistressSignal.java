package com.nickbenn.adventofcode.day13;

import com.nickbenn.adventofcode.util.DataSource;
import com.nickbenn.adventofcode.util.Defaults;
import java.io.IOException;
import java.util.Arrays;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DistressSignal {

  private final String inputFile;

  public DistressSignal() throws IOException {
    this(Defaults.INPUT_FILE);
  }

  public DistressSignal(String inputFile) throws IOException {
    this.inputFile = inputFile;
  }

  public static void main(String[] args) throws IOException {
    System.out.println(new DistressSignal().getOrderedPairsIndexSum());
    System.out.println(new DistressSignal().getDividerIndexProduct());
  }

  public int getOrderedPairsIndexSum() throws IOException {
    try (
        Stream<String> pairs = new DataSource.Builder()
            .setInputFile(inputFile)
            .setContext(getClass())
            .build()
            .paragraphs()
    ) {
      int[] index = {0};
      return pairs
          .map((pair) -> pair
              .lines()
              .map(String::trim)
              .filter(Predicate.not(String::isEmpty))
              .map(Value::parse)
              .toArray(Value[]::new)
          )
          .mapToInt((pair) -> {
            index[0]++;
            return (pair[0].compareTo(pair[1]) < 0) ? index[0] : 0;
          })
          .sum();
    }
  }

  public int getDividerIndexProduct() throws IOException {
    try (Stream<String> lines = DataSource.simpleLines(inputFile, getClass())) {
      Value[] values = lines
          .map(Value::parse)
          .toArray(Value[]::new);
      Arrays.sort(values);
      Value marker1 = Value.parse("[[2]]");
      Value marker2 = Value.parse("[[6]]");
      return Arrays.binarySearch(values, marker1) * (Arrays.binarySearch(values, marker2) - 1);
    }
  }

}
