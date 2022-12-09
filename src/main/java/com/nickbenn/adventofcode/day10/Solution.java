package com.nickbenn.adventofcode.day10;

import com.nickbenn.adventofcode.util.Defaults;
import java.io.IOException;

public class Solution {

  private final String inputFile;

  public Solution() throws IOException {
    this(Defaults.INPUT_FILE);
  }

  public Solution(String inputFile) throws IOException {
    this.inputFile = inputFile;
  }

  public static void main(String[] args) throws IOException {
    Solution bridge = new Solution();
    System.out.println(/* Invoke solution method for part 1. */);
    System.out.println(/* Invoke solution method for part 2. */);
  }

}
