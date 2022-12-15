package com.nickbenn.adventofcode.day14;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RegolithReservoirTest {

  RegolithReservoir reservoir;

  @BeforeEach
  void setUp() throws IOException {
    reservoir = new RegolithReservoir();
    System.out.println();
  }

  @Test
  void dropSandIntoAbyss() {
    assertEquals(24, reservoir.dropSandIntoAbyss());
  }

  @Test
  void dropSandUntilBlocked() {
    assertEquals(93, reservoir.dropSandUntilBlocked());
  }
}