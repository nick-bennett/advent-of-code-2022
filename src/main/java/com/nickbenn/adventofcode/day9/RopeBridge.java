package com.nickbenn.adventofcode.day9;

import com.nickbenn.adventofcode.util.DataSource;
import com.nickbenn.adventofcode.util.Defaults;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

public class RopeBridge {

  private final String inputFile;

  public RopeBridge() throws IOException {
    this(Defaults.INPUT_FILE);
  }

  public RopeBridge(String inputFile) throws IOException {
    this.inputFile = inputFile;
  }

  public static void main(String[] args) throws IOException {
    RopeBridge bridge = new RopeBridge();
    System.out.println(bridge.countVisited(2));
    System.out.println(bridge.countVisited(10));
  }

  public int countVisited(int knotCount) throws IOException {
    try (Stream<String> lines = DataSource.simpleLines(inputFile, getClass())) {
      Point[] rope = Stream.generate(Point::new)
          .limit(knotCount)
          .toArray(Point[]::new);
      Set<Point> visited = new HashSet<>();
      visited.add(rope[knotCount - 1]);
      lines
          .map(Movement::parse)
          .forEach((movement) -> {
            Direction direction = movement.direction();
            for (int step = 0; step < movement.distance(); step++) {
              rope[0] = rope[0].move(direction);
              for (int knot = 1; knot < knotCount; knot++) {
                rope[knot] = rope[knot].follow(rope[knot - 1]);
              }
              visited.add(rope[knotCount - 1]);
            }
          });
      return visited.size();
    }
  }

}
