package com.nickbenn.adventofcode.day13;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DistressSignalTest {

  DistressSignal signal;

  @BeforeEach
  void setUp() throws IOException {
    signal = new DistressSignal();
  }

  @Test
  void getOrderedPairsIndexSum() throws IOException {
    assertEquals(13, signal.getOrderedPairsIndexSum());
  }

  @Test
  void getDividerIndexProduct() throws IOException {
    assertEquals(140, signal.getDividerIndexProduct());
  }
}