package com.nickbenn.adventofcode.day9;

public record Point(int x, int y) {

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

}
