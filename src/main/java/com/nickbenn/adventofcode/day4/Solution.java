package com.nickbenn.adventofcode.day4;

import com.nickbenn.adventofcode.util.Defaults;
import java.io.IOException;

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
  }

}
