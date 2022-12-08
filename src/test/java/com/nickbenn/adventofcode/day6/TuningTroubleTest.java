package com.nickbenn.adventofcode.day6;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

@DisplayName("Day 6: Tuning Trouble")
class TuningTroubleTest {

  TuningTrouble tuning;

  @BeforeEach
  void setUp() {
    tuning = new TuningTrouble();
  }

  @DisplayName("Part 1")
  @ParameterizedTest(name = "[{index}] input=\"{0}\", expected={1}")
  @CsvFileSource(resources = "input.csv", numLinesToSkip = 1)
  void afterMarker_startOfPacket(String input, int expectedSop, int expectedSom) {
    assertEquals(expectedSop, tuning.afterMarker(input, TuningTrouble.PACKET_MARKER_LENGTH));
  }

  @DisplayName("Part 2")
  @ParameterizedTest(name = "[{index}] input=\"{0}\", expected={2}")
  @CsvFileSource(resources = "input.csv", numLinesToSkip = 1)
  void afterMarker_startOfMessage(String input, int expectedSop, int expectedSom) {
    assertEquals(expectedSom, tuning.afterMarker(input, TuningTrouble.MESSAGE_MARKER_LENGTH));
  }

}
