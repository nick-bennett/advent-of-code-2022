package com.nickbenn.adventofcode.day12;

import com.nickbenn.adventofcode.util.DataSource;
import com.nickbenn.adventofcode.util.Defaults;
import com.nickbenn.adventofcode.util.Direction;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Deque;
import java.util.LinkedList;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
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

  public static class Grid {

    private static final char ORIGIN_CODE = 'S';
    private static final char DESTINATION_CODE = 'E';
    private static final char HEIGHT_OFFSET = 'a';
    private static final int ORIGIN_HEIGHT = 'a' - HEIGHT_OFFSET;
    private static final int DESTINATION_HEIGHT = 'z' - HEIGHT_OFFSET;
    private static final BiPredicate<Square, Square> STEP_UP_CONSTRAINT =
        (from, to) -> to.height <= from.height + 1;
    private static final BiPredicate<Square, Square> STEP_DOWN_CONSTRAINT =
        (from, to) -> from.height <= to.height + 1;

    private final int width;
    private final int height;
    private final Square[][] squares;
    private final Square origin;
    private final Square destination;

    public Grid(Stream<String> lines) {
      Square[] terminals = new Square[2];
      int[] dimensionCounters = new int[2];
      squares = lines
          .map((line) -> {
            int rowIndex = dimensionCounters[0]++;
            dimensionCounters[1] = 0;
            return line
                .chars()
                .mapToObj((code) -> {
                  int colIndex = dimensionCounters[1]++;
                  boolean origin = false;
                  boolean destination = false;
                  int height = switch (code) {
                    case ORIGIN_CODE -> {
                      origin = true;
                      yield ORIGIN_HEIGHT;
                    }
                    case DESTINATION_CODE -> {
                      destination = true;
                      yield DESTINATION_HEIGHT;
                    }
                    default -> code - HEIGHT_OFFSET;
                  };
                  Square square = new Square(rowIndex, colIndex, height);
                  if (origin) {
                    terminals[0] = square;
                  } else if (destination) {
                    terminals[1] = square;
                  }
                  return square;
                })
                .toArray(Square[]::new);
          })
          .toArray(Square[][]::new);
      width = squares[0].length;
      height = squares.length;
      origin = terminals[0];
      destination = terminals[1];
    }

    public int getMinPathLength() {
      prepareFlood(STEP_UP_CONSTRAINT);
      return fillFromUntil(origin, (square) -> square == destination);
    }

    public int getMinHikeLength() {
      prepareFlood(STEP_DOWN_CONSTRAINT);
      return fillFromUntil(destination, (square) -> square.getHeight() == 0);
    }

    private void prepareFlood(BiPredicate<Square, Square> stepConstraint) {
      for (Square[] row : squares) {
        for (Square square : row) {
          square.setFlooded(false);
          square.setupNeighbors(stepConstraint);
        }
      }
    }

    private int fillFromUntil(Square start, Predicate<Square> stoppingCondition) {
      Deque<Square> queue = new LinkedList<>();
      int distance = -1;
      addToFloodQueue(start, queue);
      processUntilEmpty:
      while (!queue.isEmpty()) {
        Square tail = queue.peekLast();
        Square square = null;
        distance++;
        do {
          square = queue.pollFirst();
          if (stoppingCondition.test(square)) {
            break processUntilEmpty;
          }
          //noinspection DataFlowIssue
          for (Square neighbor : square.getFloodableNeighbors()) {
            addToFloodQueue(neighbor, queue);
          }
        } while (square != tail);
      }
      return distance;
    }

    private void addToFloodQueue(Square square, Deque<Square> queue) {
      square.setFlooded(true);
      queue.add(square);
    }

    public class Square {

      private final int row;
      private final int column;
      private final int height;

      private boolean flooded;
      private Collection<Square> neighbors;

      private Square(int row, int column, int height) {
        this.row = row;
        this.column = column;
        this.height = height;
      }

      public int getRow() {
        return row;
      }

      public int getColumn() {
        return column;
      }

      public int getHeight() {
        return height;
      }

      public boolean isFlooded() {
        return flooded;
      }

      public void setFlooded(boolean flooded) {
        this.flooded = flooded;
      }

      public Collection<Square> getFloodableNeighbors() {
        return neighbors
            .stream()
            .filter(Predicate.not(Square::isFlooded))
            .toList();
      }

      private void setupNeighbors(BiPredicate<Square, Square> stepConstraint) {
        neighbors = Arrays.stream(Direction.values())
            .map((dir) -> new int[]{row + dir.rowIncrement(), column + dir.columnIncrement()})
            .filter((coords) -> coords[0] >= 0 && coords[0] < Grid.this.height
                && coords[1] >= 0 && coords[1] < width)
            .map((coords) -> squares[coords[0]][coords[1]])
            .filter((neighbor) -> stepConstraint.test(this, neighbor))
            .toList();
      }

    }

  }

}
