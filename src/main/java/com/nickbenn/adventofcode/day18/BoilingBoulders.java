package com.nickbenn.adventofcode.day18;

import com.nickbenn.adventofcode.util.DataSource;
import com.nickbenn.adventofcode.util.Defaults;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BoilingBoulders {

  private static final Pattern COORDINATE_DELIMITER = Pattern.compile("\\s*,\\s*");
  public static final int BLOCK_SIDES = 6;

  private final Set<Block> blocks;
  private final Block minBlock;
  private final Block maxBlock;

  public BoilingBoulders() throws IOException {
    this(Defaults.INPUT_FILE);
  }

  public BoilingBoulders(String inputFile) throws IOException {
    try (Stream<String> lines = DataSource.simpleLines(inputFile, getClass())) {
      int[] minCoordinates = new int[3];
      int[] maxCoordinates = new int[3];
      blocks = lines
          .map((line) ->
              COORDINATE_DELIMITER
                  .splitAsStream(line)
                  .mapToInt(Integer::parseInt)
                  .toArray()
          )
          .peek((coordinates) -> {
            for (int coordinateIndex = 0; coordinateIndex < 3; coordinateIndex++) {
              minCoordinates[coordinateIndex] =
                  Math.min(minCoordinates[coordinateIndex], coordinates[coordinateIndex]);
              maxCoordinates[coordinateIndex] =
                  Math.max(maxCoordinates[coordinateIndex], coordinates[coordinateIndex]);
            }
          })
          .map(Block::new)
          .collect(Collectors.toCollection(HashSet::new));
      minBlock = new Block(minCoordinates);
      maxBlock = new Block(maxCoordinates);
    }
  }

  public static void main(String[] args) throws IOException {
    BoilingBoulders boulders = new BoilingBoulders();
    System.out.println(boulders.surfaceArea());
    System.out.println(boulders.externalSurfaceArea());
  }

  public int surfaceArea() {
    return (int) blocks
        .stream()
        .mapToLong((block) -> block
            .neighbors()
            .filter(Predicate.not(blocks::contains))
            .count()
        )
        .sum();
  }

  public int externalSurfaceArea() {
    Set<Block> freeBlocks = new HashSet<>();
    Set<Block> enclosedBlocks = new HashSet<>();
    return (int) blocks
        .stream()
        .mapToLong((block) -> block
            .neighbors()
            .filter((neighbor) -> free(neighbor, freeBlocks, enclosedBlocks))
            .count()
        )
        .sum();
  }

  private boolean free(Block block, Set<Block> freeBlocks, Set<Block> enclosedBlocks) {
    boolean free = false;
    Set<Block> processedBlocks = new HashSet<>();
    Set<Block> queue = new HashSet<>(List.of(block));
    Set<Block> boundary = new HashSet<>();
    while (!queue.isEmpty()) {
      for (Block current : queue) {
        if (freeBlocks.contains(current)
            || current.x() < minBlock.x()
            || current.x() > maxBlock.x()
            || current.y() < minBlock.y()
            || current.y() > maxBlock.y()
            || current.z() < minBlock.z()
            || current.z() > maxBlock.z()) {
          free = true;
          break;
        } else if (!blocks.contains(current)
            && !enclosedBlocks.contains(current)
            && !processedBlocks.contains(current)) {
          current
              .neighbors()
              .filter((b) -> !blocks.contains(b)
                  && !enclosedBlocks.contains(b)
                  && !processedBlocks.contains(b))
              .forEach(boundary::add);
        }
      }
      processedBlocks.addAll(queue);
      queue.clear();
      if (free) {
        processedBlocks.addAll(boundary);
        freeBlocks.addAll(processedBlocks);
      } else {
        queue.addAll(boundary);
        boundary.clear();
      }
    }
    if (!free) {
      enclosedBlocks.addAll(processedBlocks);
    }
    return free;
  }

  private record Block(int x, int y, int z) {

    public Block(int... coordinates) {
      this(coordinates[0], coordinates[1], coordinates[2]);
    }

    public Block offset(Direction3D direction) {
      return new Block(x + direction.deltaX(), y + direction.deltaY(), z + direction.zeltaZ());
    }

    public Stream<Block> neighbors() {
      return Arrays.stream(Direction3D.values())
          .map(this::offset);
    }

  }

  private enum Direction3D {
    X_AXIS_UP(1, 0, 0),
    X_AXIS_DOWN(-1, 0, 0),
    Y_AXIS_UP(0, 1, 0),
    Y_AXIS_DOWN(0, -1, 0),
    Z_AXIS_UP(0, 0, 1),
    Z_AXIS_DOWN(0, 0, -1);

    private final int deltaX;
    private final int deltaY;
    private final int deltaZ;

    Direction3D(int deltaX, int deltaY, int deltaZ) {
      this.deltaX = deltaX;
      this.deltaY = deltaY;
      this.deltaZ = deltaZ;
    }

    public int deltaX() {
      return deltaX;
    }

    public int deltaY() {
      return deltaY;
    }

    public int zeltaZ() {
      return deltaZ;
    }

  }
}
