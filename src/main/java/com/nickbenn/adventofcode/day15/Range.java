package com.nickbenn.adventofcode.day15;

import java.util.Comparator;
import org.jetbrains.annotations.NotNull;

record Range(int from, int to) implements Comparable<Range> {

  public static final Range EMPTY_RANGE = new Range(Integer.MIN_VALUE, Integer.MIN_VALUE);

  private static final Comparator<Range> NATURAL_ORDER =
      Comparator.comparing(Range::from).thenComparing(Range::to);

  public boolean contains(int value) {
    return value >= from && value < to;
  }

  public boolean isContiguous(Range other) {
    return to >= other.from && from <= other.to;
  }

  public Range extend(Range other) {
    return isEmpty()
        ? other
        : other.isEmpty()
            ? this
            : new Range(Math.min(from, other.from), Math.max(to, other.to));
  }

  public boolean isEmpty() {
    return to <= from;
  }

  public int size() {
    return to - from;
  }

  @Override
  public int compareTo(@NotNull Range other) {
    return NATURAL_ORDER.compare(this, other);
  }

}
