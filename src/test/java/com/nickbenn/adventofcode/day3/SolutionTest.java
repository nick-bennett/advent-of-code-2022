package com.nickbenn.adventofcode.day3;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Day 3")
class SolutionTest {

  Solution solution;

  @BeforeEach
  void setUp() {
    solution = new Solution();
  }

  @DisplayName("Part 1")
  @Test
  void getMisplacedPrioritySum() throws IOException {
    assertEquals(157, solution.getMisplacedPrioritySum());
  }

  @DisplayName("Part 2")
  @Test
  void getBadgePrioritySum() throws IOException {
    assertEquals(70, solution.getBadgePrioritySum());
  }

}
