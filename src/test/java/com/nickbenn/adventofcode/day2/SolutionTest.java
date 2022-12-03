package com.nickbenn.adventofcode.day2;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Day 2")
class SolutionTest {

  Solution solution;

  @BeforeEach
  void setUp() {
    solution = new Solution();
  }

  @DisplayName("Part 1")
  @Test
  void getStrategyBasedValue() throws IOException {
    assertEquals(15, solution.getStrategyBasedValue());
  }

  @DisplayName("Part 2")
  @Test
  void getOutcomeBasedValue() throws IOException {
    assertEquals(12, solution.getOutcomeBasedValue());
  }

}