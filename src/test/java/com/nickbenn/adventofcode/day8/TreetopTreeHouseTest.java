package com.nickbenn.adventofcode.day8;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Day 8: Treetop Tree House")
class TreetopTreeHouseTest {

  TreetopTreeHouse treetop;

  @BeforeEach
  void setUp() throws IOException {
    treetop = new TreetopTreeHouse();
  }

  @DisplayName("Part 1")
  @Test
  void getVisibleTrees() throws IOException {
    assertEquals(21, treetop.getVisibleTrees());
  }

  @DisplayName("Part 2")
  @Test
  void getMaxScenicScore() throws IOException {
    assertEquals(8, treetop.getMaxScenicScore());
  }

}
