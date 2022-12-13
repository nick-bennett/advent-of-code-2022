package com.nickbenn.adventofcode.day13;

import com.nickbenn.adventofcode.util.DataSource;
import com.nickbenn.adventofcode.util.Defaults;
import java.io.IOException;
import java.util.stream.Stream;

public class Solution {

  public Solution() throws IOException {
    this(Defaults.INPUT_FILE);
  }

  public Solution(String inputFile) throws IOException {
    try (Stream<String> rows = DataSource.simpleLines(inputFile, getClass())) {
    }
  }

  public static void main(String[] args) throws IOException {
    Solution solution = new Solution();
    System.out.println(/* Invoke part 1 solution method. */);
    System.out.println(/* Invoke part 1 solution method. */);
  }

}
