package com.nickbenn.adventofcode.day12;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HillClimbingAlgorithmTest {

  HillClimbingAlgorithm climber;

  @BeforeEach
  void setUp() throws IOException {
    climber = new HillClimbingAlgorithm();
  }

  @Test
  void getMinPathLength() {
    assertEquals(31, climber.getMinPathLength());
  }

  @Test
  void getMinHikeLength() {
    assertEquals(29, climber.getMinHikeLength());
  }

}