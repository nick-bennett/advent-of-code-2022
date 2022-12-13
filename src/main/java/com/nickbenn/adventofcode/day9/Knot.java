package com.nickbenn.adventofcode.day9;

import com.nickbenn.adventofcode.util.Direction;
import java.util.Comparator;
import org.jetbrains.annotations.NotNull;

public record Knot(int row, int column) implements Comparable<Knot> {

  private static final Comparator<Knot> NATURAL_ORDER =
      Comparator.comparing(Knot::row).thenComparing(Knot::column);

  public Knot() {
    this(0, 0);
  }

  public Knot move(Direction direction) {
    return new Knot(row + direction.rowIncrement(), column + direction.columnIncrement());
  }

  public Knot follow(Knot knot) {
    Knot next;
    int deltaRows = knot.row - row;
    int deltaColumns = knot.column - column;
    if (Math.round(Math.hypot(deltaRows, deltaColumns)) >= 2) {
      int followRows = (int) Math.signum(deltaRows);
      int followColumns = (int) Math.signum(deltaColumns);
      next = new Knot(row + followRows, column + followColumns);
    } else {
      next = this;
    }
    return next;
  }

  @Override
  public int compareTo(@NotNull Knot other) {
    return NATURAL_ORDER.compare(this, other);
  }

}
