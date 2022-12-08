package com.nickbenn.adventofcode.day7;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Day 7")
class SolutionTest {

  Solution solution;

  @BeforeEach
  void setUp() throws IOException {
    solution = new Solution();
  }

  @DisplayName("Part 1")
  @Test
  void getSumUnderThreshold() {
    assertEquals(95437, solution.getSumUnderThreshold());
  }

  @DisplayName("Part 2")
  @Test
  void getRemovalCandidate() {
    assertEquals(24933642, solution.getRemovalCandidate());
  }

}