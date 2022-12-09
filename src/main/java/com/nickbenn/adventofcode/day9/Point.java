package com.nickbenn.adventofcode.day9;

import java.util.Comparator;
import org.jetbrains.annotations.NotNull;

public record Point(int x, int y) implements Comparable<Point> {

  private static final Comparator<Point> NATURAL_ORDER =
      Comparator.comparing(Point::x).thenComparing(Point::y);

  public Point() {
    this(0, 0);
  }

  public Point move(Direction direction) {
    return new Point(x + direction.getDeltaX(), y + direction.getDeltaY());
  }

  public Point follow(Point point) {
    Point next;
    int deltaX = point.x - x;
    int deltaY = point.y - y;
    if (Math.round(Math.hypot(deltaX, deltaY)) >= 2) {
      int followX = (int) Math.signum(deltaX);
      int followY = (int) Math.signum(deltaY);
      next = new Point(x + followX, y + followY);
    } else {
      next = this;
    }
    return next;
  }

  @Override
  public int compareTo(@NotNull Point other) {
    return NATURAL_ORDER.compare(this, other);
  }

}
