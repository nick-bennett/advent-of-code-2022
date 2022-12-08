package com.nickbenn.adventofcode.day6;

import com.nickbenn.adventofcode.util.DataSource;
import com.nickbenn.adventofcode.util.Defaults;
import java.io.IOException;
import java.util.stream.IntStream;

public class TuningTrouble {

  public static final int PACKET_MARKER_LENGTH = 4;
  public static final int MESSAGE_MARKER_LENGTH = 14;

  private final String inputFile;

  public TuningTrouble() {
    this(Defaults.INPUT_FILE);
  }

  public TuningTrouble(String inputFile) {
    this.inputFile = inputFile;
  }

  public static void main(String[] args) throws IOException {
    TuningTrouble tuning = new TuningTrouble();
    tuning.printMarkerLocations(PACKET_MARKER_LENGTH);
    tuning.printMarkerLocations(MESSAGE_MARKER_LENGTH);
  }

  public void printMarkerLocations(int markerLength) throws IOException {
    try (
        IntStream lines = DataSource.simpleLines(inputFile, getClass())
            .mapToInt((line) -> afterMarker(line, markerLength))
    ) {
      lines.forEach(System.out::println);
    }
  }

  public int afterMarker(String input, int count) {
    StringBuilder buffer = new StringBuilder(input);
    int length = input.length();
    int position = -1;
    for (int i = 0; i <= length - count; i++) {
      if (distinctChars(buffer.subSequence(i, i + count))) {
        position = i + count;
        break;
      }
    }
    return position;
  }

  private boolean distinctChars(CharSequence input) {
    return input.length() == input
        .chars()
        .distinct()
        .count();
  }

}
