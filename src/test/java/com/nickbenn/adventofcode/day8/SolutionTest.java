package com.nickbenn.adventofcode.day8;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Day 8")
class SolutionTest {

  Solution solution;

  @BeforeEach
  void setUp() throws IOException {
    solution = new Solution();
  }

  @DisplayName("Part 1")
  @Test
  void getVisibleTrees() throws IOException {
    assertEquals(21, solution.getVisibleTrees());
  }

  @DisplayName("Part 2")
  @Test
  void getMaxScenicScore() throws IOException {
    assertEquals(8, solution.getMaxScenicScore());
  }

}