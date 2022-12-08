package com.nickbenn.adventofcode.day1;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@DisplayName("Day 1: Calorie Counting")
class CalorieCountingTest {

  CalorieCounting calorieCounting;

  @BeforeEach
  void setUp() {
    calorieCounting = new CalorieCounting();
  }

  @DisplayName("Parts")
  @ParameterizedTest(name = "{0}")
  @CsvSource({"1, 1, 24000", "2, 3, 45000"})
  void getMaxCalories(int part, int count, int expected) throws IOException {
    assertEquals(expected, calorieCounting.getMaxCalories(count));
  }

}