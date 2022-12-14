package com.nickbenn.adventofcode.day12;

import com.nickbenn.adventofcode.util.DataSource;
import com.nickbenn.adventofcode.util.Defaults;
import java.io.IOException;
import java.util.stream.Stream;

public class HillClimbingAlgorithm {

  private final Grid grid;

  public HillClimbingAlgorithm() throws IOException {
    this(Defaults.INPUT_FILE);
  }

  public HillClimbingAlgorithm(String inputFile) throws IOException {
    try (Stream<String> rows = DataSource.simpleLines(inputFile, getClass())) {
      grid = new Grid(rows);
    }
  }

  public static void main(String[] args) throws IOException {
    HillClimbingAlgorithm climber = new HillClimbingAlgorithm();
    System.out.println(climber.getMinPathLength());
    System.out.println(climber.getMinHikeLength());
  }

  public int getMinPathLength() {
    return grid.getMinPathLength();
  }

  public int getMinHikeLength() {
    return grid.getMinHikeLength();
  }

}
