package com.nickbenn.adventofcode.day16;

import com.nickbenn.adventofcode.util.DataSource;
import com.nickbenn.adventofcode.util.Defaults;
import java.io.IOException;
import java.util.stream.Stream;

public class Solution {

  public Solution() throws IOException {
    this(Defaults.INPUT_FILE);
  }

  public Solution(String inputFile) throws IOException {
    try (Stream<String> lines = DataSource.simpleLines(inputFile, getClass())) {
    }
  }

  public static void main(String[] args) throws IOException {
    Solution solution = new Solution();
    System.out.println(/* TODO Invoke solution method for part 1. */);
    System.out.println(/* TODO Invoke solution method for part 2. */);
  }

}
