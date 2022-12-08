package com.nickbenn.adventofcode.day5;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Day 5: Supply Stacks")
class SupplyStacksTest {

  SupplyStacks stacks;

  @BeforeEach
  void setUp() {
    stacks = new SupplyStacks();
  }

  @DisplayName("Part 1")
  @Test
  void getSingleMoveStackTops() throws IOException {
    assertEquals("CMZ", stacks.getSingleMoveStackTops());
  }

  @DisplayName("Part 2")
  @Test
  void getMultiMoveStackTops() throws IOException {
    assertEquals("MCD", stacks.getMultiMoveStackTops());
  }

}
