package com.nickbenn.adventofcode.day17;

import com.nickbenn.adventofcode.util.DataSource;
import com.nickbenn.adventofcode.util.Defaults;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PyroclasticFlow {

  private static final long PART_1_ROCKS = 2022;
  private static final long PART_2_ROCKS = 1_000_000_000_000L;

  private static final int PUSH_LEFT = '<';
  private static final int BUFFER_HEIGHT = 3;
  private static final int CHAMBER_WIDTH = 7;

  private final int[] pushes;
  private final long modulus;
  private final Shape[] shapes;
  private final List<Integer> chamber;
  private final List<Long> heights;

  public PyroclasticFlow() throws IOException {
    this(Defaults.INPUT_FILE);
  }

  public PyroclasticFlow(String inputFile) throws IOException {
    try (Stream<String> lines = DataSource.simpleLines(inputFile, getClass())) {
      pushes = lines
          .collect(Collectors.joining())
          .chars()
          .map((c) -> (c == PUSH_LEFT ? 1 : -1))
          .toArray();
      shapes = Shape.values();
      modulus = (long) pushes.length * shapes.length;
      chamber = new LinkedList<>();
      heights = new LinkedList<>();
    }
  }

  public static void main(String[] args) throws IOException {
    PyroclasticFlow flow = new PyroclasticFlow();
    System.out.println(flow.getHeight(PART_1_ROCKS));
    System.out.println(flow.getHeight(PART_2_ROCKS));
  }

  public long getHeight(long numBlocks) {
    chamber.clear();
    heights.clear();
    long cutHeight = 0;
    int pushIndex = 0;
    long totalHeight = 0;
    for (long i = 0; i < numBlocks; i++) {
      if (i % modulus == 0) {
        totalHeight = extrapolateHeight(numBlocks, i, cutHeight + height(), pushIndex);
        if (totalHeight >= 0) {
          break;
        }
        if (i * 10 % modulus == 0 && collapse(5 * (int) modulus)) {
          cutHeight += 5 * modulus;
        }
      }
      pushIndex = dropInPlace(shapes[(int) (i % shapes.length)], pushIndex);
      totalHeight = cutHeight + height();
    }
    return totalHeight;
  }

  private void resetBuffer() {
    int emptyRowCount = tailLength();
    if (emptyRowCount < BUFFER_HEIGHT) {
      for (int i = emptyRowCount; i < BUFFER_HEIGHT; i++) {
        chamber.add(0);
      }
    } else if (emptyRowCount > BUFFER_HEIGHT) {
      int size = chamber.size();
      chamber
          .subList(size - (emptyRowCount - BUFFER_HEIGHT), size)
          .clear();
    }
  }

  private long extrapolateHeight(long numBlocks, long blockCount, long height, int pushIndex) {
    long totalHeight = -1;
    long cutHeight = height - height();
    heights.add(height);
    if (heights.size() % 2 == 1) {
      int cycleLength = cycleLength(1);
      if (cycleLength > 2) {
        long cycleHeight = height - heights.get(heights.size() - 1 - cycleLength);
        long cycles = (numBlocks - blockCount) / (cycleLength * modulus);
        long partialCycleLength = (numBlocks - blockCount) % (cycleLength * modulus);
        for (int i = 0; i < partialCycleLength; i++) {
          pushIndex = dropInPlace(shapes[(int) ((blockCount + i) % shapes.length)], pushIndex);
        }
        totalHeight = (cutHeight + height()) + cycles * cycleHeight;
      }
    }
    return totalHeight;
  }

  private boolean collapse(int margin) {
    boolean collapsed = false;
    if (chamber.size() >= 2 * margin) {
      chamber.subList(0, margin).clear();
      collapsed = true;
    }
    return collapsed;
  }

  private int dropInPlace(long blockCount, int pushIndex, int blocksToDrop) {
    for (int i = 0; i < blocksToDrop; i++) {
      Shape shape = shapes[(int) ((blockCount + i) % shapes.length)];
      pushIndex = dropInPlace(shape, pushIndex);
    }
    return pushIndex;
  }

  private int dropInPlace(Shape shape, int pushIndex) {
    int leftShift = shape.initialLeftShift();
    resetBuffer();
    int shapeBaseRow = chamber.size();
    List<Integer> subchamber = chamber.subList(shapeBaseRow, shapeBaseRow);
    shape.drop(subchamber, leftShift);
    while (true) {
      int push = pushes[pushIndex];
      pushIndex = (pushIndex + 1) % pushes.length;
      subchamber = chamber.subList(shapeBaseRow, chamber.size());
      if (shape.shift(subchamber, leftShift, push, CHAMBER_WIDTH)) {
        leftShift += push;
      }
      if (shapeBaseRow > 0
          && shape.drop(chamber.subList(shapeBaseRow - 1, chamber.size()), leftShift)) {
        shapeBaseRow--;
      } else {
        break;
      }
    }
    return pushIndex;
  }

  private int cycleLength(int confidenceFactor) {
    int cycleLength = 0;
    int size = heights.size();
    int maxCycleLength = size / (confidenceFactor + 1);
    if (size > confidenceFactor) {
      ListIterator<Long> testIterator = heights.listIterator(size);
      long rootNext = testIterator.previous();
      long rootPrevious = testIterator.previous();
      long rootDifference = rootNext - rootPrevious;
      long testNext = rootPrevious;
      findFirstMatch:
      for (int testCycleLength = 1; testCycleLength < maxCycleLength; testCycleLength++) {
        long testPrevious = testIterator.previous();
        long testDifference = testNext - testPrevious;
        testNext = testPrevious;
        if (testDifference == rootDifference) {
          testIterator.next();
          testNext = testIterator.next();
          testIterator.previous();
          for (int i = 0; i < confidenceFactor; i++) {
            ListIterator<Long> baseIterator = heights.listIterator(size);
            long baseNext = baseIterator.previous();
            for (int j = 0; j < testCycleLength; j++) {
              long basePrevious = baseIterator.previous();
              long baseDifference = baseNext - basePrevious;
              baseNext = basePrevious;
              testPrevious = testIterator.previous();
              testDifference = testNext - testPrevious;
              testNext = testPrevious;
              if (testDifference != baseDifference) {
                testIterator.next();
                testNext = testIterator.next();
                testIterator.previous();
                continue findFirstMatch;
              }
            }
          }
          cycleLength = testCycleLength;
          break;
        }
      }
    }
    return cycleLength;
  }

  private int height() {
    return chamber.size() - tailLength();
  }

  private int tailLength() {
    int emptyRowCount = 0;
    ListIterator<Integer> rowIterator = chamber.listIterator(chamber.size());
    while (rowIterator.hasPrevious() && rowIterator.previous() == 0) {
      emptyRowCount++;
    }
    return emptyRowCount;
  }

}
