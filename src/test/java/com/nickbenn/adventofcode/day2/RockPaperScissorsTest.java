package com.nickbenn.adventofcode.day2;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Day 2: Rock-Paper-Scissors")
class RockPaperScissorsTest {

  RockPaperScissors rps;

  @BeforeEach
  void setUp() {
    rps = new RockPaperScissors();
  }

  @DisplayName("Part 1")
  @Test
  void getStrategyBasedValue() throws IOException {
    assertEquals(15, rps.getStrategyBasedValue());
  }

  @DisplayName("Part 2")
  @Test
  void getOutcomeBasedValue() throws IOException {
    assertEquals(12, rps.getOutcomeBasedValue());
  }

}