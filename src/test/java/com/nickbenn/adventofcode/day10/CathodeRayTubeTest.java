package com.nickbenn.adventofcode.day10;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Day 10: Cathode-Ray Tube")
class CathodeRayTubeTest {

  static final int EXPECTED_PRODUCT_SUM = 13140;
  static final String EXPECTED_SCAN = """
      ##..##..##..##..##..##..##..##..##..##..
      ###...###...###...###...###...###...###.
      ####....####....####....####....####....
      #####.....#####.....#####.....#####.....
      ######......######......######......####
      #######.......#######.......#######.....""";

  CathodeRayTube tube;

  @BeforeEach
  void setUp() throws IOException {
    tube = new CathodeRayTube();
  }

  @Test
  void getStrengthProductSum() throws IOException {
    assertEquals(EXPECTED_PRODUCT_SUM, tube.getStrengthProductSum());
  }

  @Test
  void getScan() throws IOException {
    assertEquals(EXPECTED_SCAN, tube.getScan());
  }
}