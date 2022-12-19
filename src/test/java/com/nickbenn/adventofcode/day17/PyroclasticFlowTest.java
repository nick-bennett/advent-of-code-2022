package com.nickbenn.adventofcode.day17;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

class PyroclasticFlowTest {

  PyroclasticFlow flow;

  @BeforeEach
  void setUp() throws IOException {
    flow = new PyroclasticFlow();
  }

  @ParameterizedTest
  @CsvFileSource(resources = "cases.csv", numLinesToSkip = 1)
  void getHeight(long blocks, long expectedHeight) {
    assertEquals(expectedHeight, flow.getHeight(blocks));
  }

}