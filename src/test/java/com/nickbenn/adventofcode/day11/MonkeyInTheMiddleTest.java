package com.nickbenn.adventofcode.day11;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

@DisplayName("Day 11: Monkey in the Middle")
class MonkeyInTheMiddleTest {

  MonkeyInTheMiddle mitm;

  @BeforeEach
  void setUp() throws IOException {
    mitm = new MonkeyInTheMiddle();
  }

  @ParameterizedTest
  @CsvFileSource(resources = "cases.csv", numLinesToSkip = 1)
  void getTopProduct(int part, int rounds, int divisor, long expected) {
    assertEquals(expected, mitm.getTopProduct(rounds, divisor));
  }
}