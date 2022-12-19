package com.nickbenn.adventofcode.day18;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BoilingBouldersTest {

  BoilingBoulders boulders;

  @BeforeEach
  void setUp() throws IOException {
    boulders = new BoilingBoulders();
  }

  @Test
  void surfaceArea() {
    assertEquals(64, boulders.surfaceArea());
  }


  @Test
  void externalSurfaceArea() {
    assertEquals(58, boulders.externalSurfaceArea());
  }
}