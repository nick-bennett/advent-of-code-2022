package com.nickbenn.adventofcode.day12;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Day 11: Hill Climbing Algorithm")
class HillClimbingAlgorithmTest {

  HillClimbingAlgorithm climber;

  @BeforeEach
  void setUp() throws IOException {
    climber = new HillClimbingAlgorithm();
  }

  @DisplayName("Part 1")
  @Test
  void getMinPathLength() {
    assertEquals(31, climber.getMinPathLength());
  }

  @DisplayName("Part 2")
  @Test
  void getMinHikeLength() {
    assertEquals(29, climber.getMinHikeLength());
  }

}