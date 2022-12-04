package com.nickbenn.adventofcode.day4;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Day 4")
class SolutionTest {

  Solution solution;

  @BeforeEach
  void setUp() {
    solution = new Solution();
  }

  @DisplayName("Part 1")
  @Test
  void getFullRedundancyCount() throws IOException {
    assertEquals(2, solution.getFullRedundancyCount());
  }

  @DisplayName("Part 2")
  @Test
  void getPartialRedundancyCount() throws IOException {
    assertEquals(4, solution.getPartialRedundancyCount());
  }

}
