package com.nickbenn.adventofcode.day15;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BeaconExclusionZoneTest {

  BeaconExclusionZone zone;

  @BeforeEach
  void setUp() throws IOException {
    zone = new BeaconExclusionZone();
  }

  @Test
  void countExcluded() {
    assertEquals(26, zone.countExcluded(10));
  }

  @Test
  void findMissingTuningFrequency() {
    assertEquals(56000011, zone.findMissingTuningFrequency(0, 21));
  }

}