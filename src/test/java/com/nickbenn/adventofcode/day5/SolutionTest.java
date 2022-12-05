package com.nickbenn.adventofcode.day5;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Day 5")
class SolutionTest {

  Solution solution;

  @BeforeEach
  void setUp() {
    solution = new Solution();
  }

  @DisplayName("Part 1")
  @Test
  void getSingleMoveStackTops() throws IOException {
    assertEquals("CMZ", solution.getSingleMoveStackTops());
  }

  @DisplayName("Part 2")
  @Test
  void getMultiMoveStackTops() throws IOException {
    assertEquals("MCD", solution.getMultiMoveStackTops());
  }

}
