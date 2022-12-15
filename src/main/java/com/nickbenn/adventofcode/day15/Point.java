package com.nickbenn.adventofcode.day15;

record Point(int x, int y) {

  public int distance(Point other) {
    return distance(other.x, other.y);
  }

  public int distance(int x, int y) {
    return Math.abs(x - this.x) + Math.abs(y - this.y);
  }

}
