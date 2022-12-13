package com.nickbenn.adventofcode.day12;

import com.nickbenn.adventofcode.util.DataSource;
import com.nickbenn.adventofcode.util.Defaults;
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
      Deque<Square> queue = new LinkedList<>();
      int distance = 0;
      addToFloodQueue(origin, queue, distance);
      while (!queue.isEmpty()) {
        Square square = queue.removeFirst();
        distance = square.getDistance();
        if (square == destination) {
          break;
        }
        for (Square neighbor : square.getFloodableNeighbors()) {
          addToFloodQueue(neighbor, queue, distance + 1);
        }
      }
      return distance;
    }

    public int getMinHikeLength() {
      prepareFlood(STEP_DOWN_CONSTRAINT);
      Deque<Square> queue = new LinkedList<>();
      int distance = 0;
      addToFloodQueue(destination, queue, distance);
      while (!queue.isEmpty()) {
        Square square = queue.removeFirst();
        distance = square.getDistance();
        if (square.getHeight() == 0) {
          break;
        }
        for (Square neighbor : square.getFloodableNeighbors()) {
          addToFloodQueue(neighbor, queue, distance + 1);
        }
      }
      return distance;
    }

    private void prepareFlood(BiPredicate<Square, Square> stepConstraint) {
      for (Square[] row : squares) {
        for (Square square : row) {
          square.setDistance(Integer.MAX_VALUE);
          square.setFlooded(false);
          square.setupNeighbors(stepConstraint);
        }
      }
    }

    private void addToFloodQueue(Square square, Deque<Square> queue, int length) {
      square.setFlooded(true);
      square.setDistance(length);
      queue.add(square);
    }

    public class Square {

      private final int row;
      private final int column;
      private final int height;

      private boolean flooded;
      private int distance;
      private Square previous;
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

      public int getDistance() {
        return distance;
      }

      public void setDistance(int distance) {
        this.distance = distance;
      }

      public Square getPrevious() {
        return previous;
      }

      public void setPrevious(Square previous) {
        this.previous = previous;
      }

      public Collection<Square> getFloodableNeighbors() {
        return neighbors
            .stream()
            .filter(Predicate.not(Square::isFlooded))
            .toList();
      }

      private void setupNeighbors(BiPredicate<Square, Square> stepConstraint) {
        neighbors = Arrays.stream(Direction.values())
            .map((dir) -> new int[]{row + dir.getRowOffset(), column + dir.getColumnOffset()})
            .filter((coords) -> coords[0] >= 0 && coords[0] < Grid.this.height
                && coords[1] >= 0 && coords[1] < width)
            .map((coords) -> squares[coords[0]][coords[1]])
            .filter((neighbor) -> stepConstraint.test(this, neighbor))
            .toList();
      }

    }

    private enum Direction {

      NORTH(-1, 0),
      EAST(0, 1),
      SOUTH(1, 0),
      WEST(0, -1);

      private final int rowOffset;
      private final int columnOffset;

      Direction(int rowOffset, int columnOffset) {
        this.rowOffset = rowOffset;
        this.columnOffset = columnOffset;
      }

      public int getRowOffset() {
        return rowOffset;
      }

      public int getColumnOffset() {
        return columnOffset;
      }

    }

  }

}
