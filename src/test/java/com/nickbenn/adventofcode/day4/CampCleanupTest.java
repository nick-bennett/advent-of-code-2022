package com.nickbenn.adventofcode.day4;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Day 4")
class CampCleanupTest {

  CampCleanup cleanup;

  @BeforeEach
  void setUp() {
    cleanup = new CampCleanup();
  }

  @DisplayName("Part 1")
  @Test
  void getFullRedundancyCount() throws IOException {
    assertEquals(2, cleanup.getFullRedundancyCount());
  }

  @DisplayName("Part 2")
  @Test
  void getPartialRedundancyCount() throws IOException {
    assertEquals(4, cleanup.getPartialRedundancyCount());
  }

}
