package com.nickbenn.adventofcode.day1;

import com.nickbenn.adventofcode.util.DataSource;
import com.nickbenn.adventofcode.util.Defaults;
import java.io.IOException;
import java.util.stream.Stream;

public class CalorieCounting {

  private final String inputFile;

  public CalorieCounting() {
    this(Defaults.INPUT_FILE);
  }

  public CalorieCounting(String inputFile) {
    this.inputFile = inputFile;
  }

  public static void main(String[] args) throws IOException {
    CalorieCounting calorieCounting = new CalorieCounting();
    System.out.println(calorieCounting.getMaxCalories(1));
    System.out.println(calorieCounting.getMaxCalories(3));
  }

  public int getMaxCalories(int count) throws IOException {
    try (
        Stream<Stream<String>> groups = new DataSource.Builder()
            .setInputFile(inputFile)
            .setContext(getClass())
            .build()
            .paragraphLines()
    ) {
      return -groups
          .mapToInt((group) ->
              group
                  .mapToInt(Integer::parseInt)
                  .reduce(0, (negSum, value) -> negSum - value)
          )
          .sorted()
          .limit(count)
          .sum();
    }
  }

}
