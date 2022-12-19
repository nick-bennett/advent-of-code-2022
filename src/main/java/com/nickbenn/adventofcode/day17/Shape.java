package com.nickbenn.adventofcode.day17;

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

enum Shape {

  HORIZONTAL_BAR(1, 4, new int[]{
      0b1111
  }),
  CROSS(2, 3, new int[]{
      0b010,
      0b111,
      0b010
  }),
  BOTTOM_RIGHT_ANGLE(2, 3, new int[]{
      0b001,
      0b001,
      0b111
  }),
  VERTICAL_BAR(4, 1, new int[]{
      0b1,
      0b1,
      0b1,
      0b1
  }),
  BLOCK(3, 2, new int[]{
      0b11,
      0b11
  });

  private final int initialLeftShift;
  private final int width;
  private final int[] lines;

  Shape(int initialLeftShift, int width, int[] lines) {
    this.initialLeftShift = initialLeftShift;
    this.width = width;
    this.lines = lines;
  }

  public boolean drop(List<Integer> subchamber, int leftShift) {
    boolean dropped = false;
    ListIterator<Integer> rowIterator = subchamber.listIterator();
    if (canDrop(rowIterator, leftShift)) {
      int previousLine = 0;
      rowIterator = subchamber.listIterator();
      Iterator<Integer> lineIterator = new BottomUpIterator();
      while (lineIterator.hasNext()) {
        if (!rowIterator.hasNext()) {
          rowIterator.add(0);
          rowIterator.previous();
        }
        int destination = rowIterator.next() & ~previousLine;
        int currentLine = lineIterator.next() << leftShift;
        rowIterator.set(destination | currentLine);
        previousLine = currentLine;
      }
      if (rowIterator.hasNext()) {
        rowIterator.set(rowIterator.next() & ~previousLine);
      }
      dropped = true;
    }
    return dropped;
  }

  public boolean shift(List<Integer> subchamber,
      int leftShift, int shiftIncrement, int chamberWidth) {
    boolean shifted = false;
    ListIterator<Integer> rowIterator = subchamber.listIterator();
    if (canShift(rowIterator, leftShift, shiftIncrement, chamberWidth)) {
      rowIterator = subchamber.listIterator();
      Iterator<Integer> lineIterator = new BottomUpIterator();
      while (lineIterator.hasNext()) {
        int currentLine = shifted(lineIterator.next(), leftShift);
        int destination = rowIterator.next() & ~currentLine;
        rowIterator.set(destination | shifted(currentLine, shiftIncrement));
      }
      shifted = true;
    }
    return shifted;
  }

  public int initialLeftShift() {
    return initialLeftShift;
  }

  private boolean canDrop(Iterator<Integer> rowIterator, int leftShift) {
    int previousLine = 0;
    boolean canDrop = true;
    Iterator<Integer> lineIterator = new BottomUpIterator();
    while (canDrop && lineIterator.hasNext()) {
      int destination = (rowIterator.hasNext() ? rowIterator.next() : 0) & ~previousLine;
      int currentLine = shifted(lineIterator.next(), leftShift);
      canDrop = ((destination & ~currentLine) == destination);
      previousLine = currentLine;
    }
    return canDrop;
  }

  private boolean canShift(Iterator<Integer> rowIterator,
      int leftShift, int shiftIncrement, int chamberWidth) {
    boolean canShift = false;
    int highestBit = leftShift + width + shiftIncrement;
    int lowestBit = leftShift + shiftIncrement;
    if (highestBit <= chamberWidth && lowestBit >= 0) {
      canShift = true;
      Iterator<Integer> lineIterator = new BottomUpIterator();
      while (canShift && rowIterator.hasNext() && lineIterator.hasNext()) {
        int currentLine = shifted(lineIterator.next(), leftShift);
        int destination = rowIterator.next() & ~currentLine;
        canShift = ((destination & ~(shifted(currentLine, shiftIncrement))) == destination);
      }
    }
    return canShift;
  }

  private static int shifted(int source, int shift) {
    return (shift >= 0) ? (source << shift) : (source >>> -shift);
  }

  private class BottomUpIterator implements Iterator<Integer> {

    int currentRow = lines.length;

    @Override
    public boolean hasNext() {
      return currentRow > 0;
    }

    @Override
    public Integer next() {
      return lines[--currentRow];
    }

  }

}
