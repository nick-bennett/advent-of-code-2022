package com.nickbenn.adventofcode.day3;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Day 3: Rucksack Reorganization")
class RucksackReorganizationTest {

  RucksackReorganization reorg;

  @BeforeEach
  void setUp() {
    reorg = new RucksackReorganization();
  }

  @DisplayName("Part 1")
  @Test
  void getMisplacedPrioritySum() throws IOException {
    assertEquals(157, reorg.getMisplacedPrioritySum());
  }

  @DisplayName("Part 2")
  @Test
  void getBadgePrioritySum() throws IOException {
    assertEquals(70, reorg.getBadgePrioritySum());
  }

}
