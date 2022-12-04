package com.nickbenn.adventofcode.day1;

import com.nickbenn.adventofcode.util.DataSource;
import com.nickbenn.adventofcode.util.Defaults;
import java.io.IOException;
import java.util.stream.Stream;

public class Solution {

  private final String inputFile;

  public Solution() {
    this(Defaults.INPUT_FILE);
  }

  public Solution(String inputFile) {
    this.inputFile = inputFile;
  }

  public static void main(String[] args) throws IOException {
    Solution solution = new Solution();
    System.out.println(solution.getMaxCalories(1));
    System.out.println(solution.getMaxCalories(3));
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
