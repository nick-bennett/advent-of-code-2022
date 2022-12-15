package com.nickbenn.adventofcode.day15;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

record SensorReading(Point sensor, Point beacon) {

  private static final Set<Integer> EMPTY_SET = Collections.emptySet();

  public int distanceToBeacon() {
    return sensor.distance(beacon);
  }

  public Set<Integer> excludedSetInRow(int row) {
    Range excluded = excludedRangeInRow(row);
    return (excluded != null)
        ? IntStream.range(excluded.from(), excluded.to())
        .boxed()
        .collect(Collectors.toSet())
        : EMPTY_SET;
  }

  public Range excludedRangeInRow(int row) {
    int distanceToRow = Math.abs(row - sensor().y());
    int horizontalHalfRange = distanceToBeacon() - distanceToRow;
    return (horizontalHalfRange >= 0)
        ? new Range(sensor().x() - horizontalHalfRange, sensor().x() + horizontalHalfRange + 1)
        : Range.EMPTY_RANGE;
  }

  public boolean isPointExcluded(Point point) {
    return sensor().distance(point) <= distanceToBeacon();
  }

}
