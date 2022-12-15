package com.nickbenn.adventofcode.day15;

import com.nickbenn.adventofcode.util.DataSource;
import com.nickbenn.adventofcode.util.Defaults;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BeaconExclusionZone {

  private static final Pattern SENSOR_READING =
      Pattern.compile("^.*?x=([-+]?\\d+), y=([-+]?\\d+):.*?x=([-+]?\\d+), y=([-+]?\\d+)\\D*$");
  private static final int EXCLUSION_ROW = 2_000_000;
  private static final int SEARCH_UPPER_BOUND = 4_000_001;
  private static final int FREQUENCY_X_MULTIPLIER = 4_000_000;

  private final List<SensorReading> readings;

  public BeaconExclusionZone() throws IOException {
    this(Defaults.INPUT_FILE);
  }

  public BeaconExclusionZone(String inputFile) throws IOException {
    try (Stream<String> lines = DataSource.simpleLines(inputFile, getClass())) {
      readings = lines
          .map(SENSOR_READING::matcher)
          .filter(Matcher::matches)
          .map((matcher) ->
              new SensorReading(
                  new Point(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2))),
                  new Point(Integer.parseInt(matcher.group(3)), Integer.parseInt(matcher.group(4)))
              )
          )
          .collect(Collectors.toList());
    }
  }

  public static void main(String[] args) throws IOException {
    BeaconExclusionZone zone = new BeaconExclusionZone();
    System.out.println(zone.countExcluded(EXCLUSION_ROW));
    System.out.println(zone.findMissingTuningFrequency(0, SEARCH_UPPER_BOUND));
  }

  public int countExcluded(int row) {
    Set<Integer> beaconsInRow = new HashSet<>();
    Set<Integer> excludedBeacons = new HashSet<>();
    for (SensorReading reading : readings) {
      if (reading.beacon().y() == row) {
        beaconsInRow.add(reading.beacon().x());
      }
      excludedBeacons.addAll(reading.excludedSetInRow(row));
    }
    excludedBeacons.removeAll(beaconsInRow);
    return excludedBeacons.size();
  }

  public long findMissingTuningFrequency(int lowerBound, int upperBound) {
    long result = 0;
    Range low = new Range(Integer.MIN_VALUE, lowerBound);
    Range high = new Range(upperBound, Integer.MAX_VALUE);
    for (int y = 0; y < upperBound; y++) {
      int row = y;
      List<Range> notExcluded = new LinkedList<>();
      Range excluded = Stream.concat(
              Stream.of(low, high),
              readings
                  .stream()
                  .map((reading) -> reading.excludedRangeInRow(row))
                  .filter(Predicate.not(Range::isEmpty))
          )
          .sorted()
          .reduce((r1, r2) -> {
            if (r1.to() < r2.from()) {
              notExcluded.add(new Range(r1.to(), r2.from()));
            }
            return r1.extend(r2);
          })
          .orElse(Range.EMPTY_RANGE);
      if (!notExcluded.isEmpty()) {
        result = (long) notExcluded.get(0).from() * FREQUENCY_X_MULTIPLIER + row;
        break;
      }
    }
    return result;
  }

}
