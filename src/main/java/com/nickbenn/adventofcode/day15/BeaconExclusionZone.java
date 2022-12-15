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
  private static final long FREQUENCY_X_MULTIPLIER = 4_000_000;

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
    Set<Integer> excluded = new HashSet<>();
    for (SensorReading reading : readings) {
      if (reading.beacon().y() == row) {
        beaconsInRow.add(reading.beacon().x());
      }
      excluded.addAll(reading.excludedSetInRow(row));
    }
    excluded.removeAll(beaconsInRow);
    return excluded.size();
  }

  public long findMissingTuningFrequency(int lowerBound, int upperBound) {
    long result = 0;
    Range low = new Range(Integer.MIN_VALUE, lowerBound);
    Range high = new Range(upperBound, Integer.MAX_VALUE);
    for (int y = 0; y < upperBound; y++) {
      List<Range> gaps = new LinkedList<>();
      var ignored = Stream.concat(Stream.of(low, high), exclusionsInRow(y))
          .sorted()
          .reduce((r1, r2) -> {
            if (!r1.isContiguous(r2)) {
              gaps.add(r1.gap(r2));
            }
            return r1.extend(r2);
          });
      if (!gaps.isEmpty()) {
        result = FREQUENCY_X_MULTIPLIER * gaps.get(0).from() + y;
        break;
      }
    }
    return result;
  }

  private Stream<Range> exclusionsInRow(int row) {
    return readings
        .stream()
        .map((reading) -> reading.excludedRangeInRow(row))
        .filter(Predicate.not(Range::isEmpty));
  }

}
