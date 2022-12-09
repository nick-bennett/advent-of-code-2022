package com.nickbenn.adventofcode.day9;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

@DisplayName("Day 9: Rope Bridge")
class RopeBridgeTest {

  @ParameterizedTest(name = "[{index}] moves={0}, knots={1}, expected={2}")
  @CsvFileSource(resources = "cases.csv", numLinesToSkip = 1)
  void countVisited(String inputFile, int knots, int expected) throws IOException {
    RopeBridge bridge = new RopeBridge(inputFile);
    assertEquals(expected, bridge.countVisited(knots));
  }

}
